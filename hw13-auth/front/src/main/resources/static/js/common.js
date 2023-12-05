let keycloak = {};
let manager = {};
let localizedMessages = new Map([
    ['error', 'Error'],
    ['error.detail', 'Details'],
    ['error.status.400', 'Bad Request'],
    ['error.status.404', 'Not Found'],
    ['error.status.405', 'Method Not Allowed'],
    ['error.status.500', 'Internal Server Error'],
]);
let bookManager = {};

class Manager {
    authenticated = false;
    redirectUrl = '';

    async checkState() {
        this.authenticated = await keycloak.init({
            checkLoginIframe: true,
            onLoad: 'check-sso',
        });
        if (!this.authenticated) {
            $('#loginBtn').removeClass('invisible');
            $('#logoutBtn').addClass('invisible');
            $('#logoutBtn').attr('title', '');
        } else {
            $('#loginBtn').addClass('invisible');
            $('#logoutBtn').removeClass('invisible');
            await keycloak.loadUserInfo();
            $('#logoutBtn').attr('title', keycloak.userInfo.name);
        }
    }

    async login() {
        await keycloak.login({
            redirectUri: this.redirectUrl,
            scope: 'profile'
        });
    }

    async logout() {
        await keycloak.logout({
            redirectUri: this.redirectUrl,
        });
    }

    async getJson(url, needAuth = true) {
        let ret = {success: true, response: {}, error: {}}
        try {
            const auth = `Bearer ${keycloak.token}`;
            let response = {};
            if (this.authenticated && needAuth) {
                await this.updateToken();
                response = await fetch(url, {
                    method: 'GET',
                    headers: {
                        accept: 'application/json',
                        Authorization: auth
                    }
                });
            } else {
                response = await fetch(url, {
                    method: 'GET',
                    headers: {
                        accept: 'application/json',
                    }
                });
            }
            ret.response.status = response.status;
            if (response.status === 200) {
                const respBody = await response.json()
                ret.response.result = respBody;
            } else {
                ret.success = false;
                const bodyText = await response.text();
                try {
                    ret.error = JSON.parse(bodyText);
                } catch (e) {
                    ret.error = bodyText;
                }
            }
        } catch (e) {
            this.formatFetchErr(e, ret, url);
        }
        return ret;
    }

    async sendJsonData(url, data, method = 'POST') {
        let ret = {success: true, response: {}, error: {}}
        let headers = {accept: 'application/json'}
        try {
            let response = {};
            if (this.authenticated) {
                await this.updateToken();
                let headers = {accept: 'application/json'};
                headers.Authorization = `Bearer ${keycloak.token}`;
                headers['Content-Type'] = 'application/json; charset=utf-8'
                response = await fetch(url, {
                    method: method,
                    body: data,
                    headers: headers
                });
            } else {
                this.formatFetchErr({message: 'need auth'}, ret, url);
                return ret;
            }
            ret.response.status = response.status;
            if (response.status === 200) {
                const respBody = await response.json()
                ret.response.result = respBody;
            } else {
                ret.success = false;
                const bodyText = await response.text();
                try {
                    ret.error = JSON.parse(bodyText);
                } catch (e) {
                    ret.error = bodyText;
                }
            }
        } catch (e) {
            this.formatFetchErr(e, ret, url);
        }
        return ret;
    }

    async updateToken() {
        await keycloak.updateToken(5).then(function (refreshed) {
            if (refreshed) {
                //console.log('Token was successfully refreshed');
            }
        }).catch(function () {
            //console.error('Failed to refresh the token, or the session has expired');
        });
    }

    formatFetchErr(err, ret, url) {
        ret.success = false;
        ret.response.status = 404;
        ret.error.status = 404;
        ret.error.statusMessage = {key: 'error.status.404'};
        ret.error.timestamp = (new Date()).toISOString();
        ret.error.errorMessage = {message: err.message};
        ret.error.detailMessage = {message: url};
    }

    getLocalizedMsg(errPair) {
        if (errPair === null | errPair === undefined) {
            return null;
        }
        if (errPair.key !== null && errPair.key !== undefined && errPair.key !== ''
            && localizedMessages.has(errPair.key)) {
            return localizedMessages.get(errPair.key);
        } else {
            return errPair.message;
        }
    }

    showError(error, errDiv = null) {
        let errContainer = $('#' + (errDiv ?? 'errorContainer'));
        //console.log(error);

        let alertDiv = document.createElement('div');

        let msg = '';
        if (manager.getLocalizedMsg(error.errorMessage) !== null) {
            msg = `<p class="mb-0">${manager.getLocalizedMsg(error.errorMessage)}</p>`;
        }

        let details = '';
        if (manager.getLocalizedMsg(error.detailMessage) !== null) {
            details = '<p class="mb-0">' + manager.getLocalizedMsg(error.detailMessage) + '</p>';
        }

        alertDiv.innerHTML =
            `<div class="alert alert-danger alert-dismissible fade show pb-0 pt-0" role="alert">
             <strong>${error.status + ': ' + manager.getLocalizedMsg(error.statusMessage)}</strong>
             ${msg}
             ${details}
             <button type="button" class="btn-close btn-sm pt-0 pb-0 mt-0" data-bs-dismiss="alert" aria-label="Close"></button>
         </div>`;
        errContainer.append(alertDiv);
        //console.error(error);
    }

    applyApiExchangeResult(result, successFunction, errorFunction = manager.showError) {
        if (result.success) {
            successFunction(result.response.result, result.response.status);
        } else {
            errorFunction(result.error);
        }
    }

    applyLocalization(data) {
        Object.keys(data).forEach(key => {
            localizedMessages.set(key, data[key]);
        });
    }

}

async function init(keyCloakConfig) {
    keycloak = new Keycloak({
        url: keyCloakConfig.url,
        realm: keyCloakConfig.realm,
        clientId: keyCloakConfig.client
    });
    manager = new Manager();
    manager.redirectUrl = window.location.href;
    await manager.getJson('/api/v1/message/' + $('#lang').val())
        .then(data => {
            manager.applyApiExchangeResult(data, manager.applyLocalization)
        });
    $('#loginBtn').on('click', manager.login);
    $('#logoutBtn').on('click', manager.logout);

    bookManager = new BookManager();
    bookManager.apiSettings = apiServerSettings;
    bookManager.initPage();
    bookManager.setLocalizedActions();

    await manager.checkState();
    return manager.authenticated;
}

init(keyCloakSettings).then(status => {
    if (status) {
        bookManager.setBookModifier()
            .then(_ => bookManager.setCommentModifier())
            .then(_ => bookManager.getBookList());
    } else {
        bookManager.clearPage('0');
    }
})