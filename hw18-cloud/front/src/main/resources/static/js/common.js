let manager = {};
let localizedMessages = new Map([
    ['error', 'Error'],
    ['error.detail', 'Details'],
    ['error.status.400', 'Bad Request'],
    ['error.status.404', 'Not Found'],
    ['error.status.405', 'Method Not Allowed'],
    ['error.status.500', 'Internal Server Error'],
    ['error.status.503', 'Service Unavailable'],
    ['error.validation', 'Validation error'],
]);
let bookManager = {};

class Manager {
    redirectUrl = '';

    async getJson(url, needAuth = true) {
        let ret = {success: true, response: {}, error: {}}
        try {
            let response = await fetch(url, {
                method: 'GET',
                headers: {
                    accept: 'application/json',
                }
            });
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
            let headers = {accept: 'application/json'};
            headers['Content-Type'] = 'application/json; charset=utf-8'
            response = await fetch(url, {
                method: method,
                body: data,
                headers: headers
            });
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

async function init() {
    manager = new Manager();
    manager.redirectUrl = window.location.href;

    initBackInfo().then();

    await manager.getJson('/api/v1/message/' + $('#lang').val())
        .then(data => {
            manager.applyApiExchangeResult(data, manager.applyLocalization)
        });

    bookManager = new BookManager();
    bookManager.initPage();
    bookManager.setLocalizedActions();

    return true;
}

init().then(status => {
    if (status) {
        bookManager.setBookModifier()
            .then(_ => bookManager.setCommentModifier())
            .then(_ => bookManager.getBookList());
    } else {
        bookManager.clearPage('0');
    }
})

let backEnds = {};

async function initBackInfo() {
    await manager.getJson('/api/v1/backend/info')
        .then(data => {
            const info = data.response.result;
            let counter = 0;
            for (const key of Object.keys(info)) {
                $('#backServer').append($('<option>', {
                    value: key,
                    text: (++counter) + ' (' + info[key]['db-type'] + ')'
                }));
            }
            backEnds = info;
            changeBack();
            $('#backServer').on('change', changeBack)
        });
}

function changeBack() {
    const id = $('#backServer').val();
    const btnH2 = $("#btH2");
    const btStatus = $('#btStatus');
    const btHateOAS = $('#btHateOAS');
    const host = backEnds[id]['host'];
    const port = backEnds[id]['port'];
    if (backEnds[id]['db-type'] === 'H2') {
        btnH2.removeClass('invisible');
    } else {
        btnH2.addClass('invisible');
    }
    btnH2.prop('href', 'http://localhost:' + port + '/h2-console');
    btnH2.prop('target', 'console_' + id);
    btStatus.removeClass('invisible');
    btStatus.prop('href', 'http://' + host + ':' + port + '/actuator');
    btStatus.prop('target', 'actuator_' + id);
    btHateOAS.removeClass('invisible');
    btHateOAS.prop('href', 'http://' + host + ':' + port
        + '/hateoas/explorer/index.html#uri=http://'+host + ':' + port +'/hateoas');
    btHateOAS.prop('target', 'hateoas');
}


