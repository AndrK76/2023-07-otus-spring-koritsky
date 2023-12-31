const jsonRequestHeader = {'Accept': 'application/json', 'Content-Type': 'application/json'}
const ndJsonRequestHeader = {'Accept': 'application/x-ndjson', 'Content-Type': 'application/x-ndjson'}


const urlGetLocalizedMessage = '/api/v1/message';
const urlBookApi = '/api/v1/book';
const urlValidateApi = '/api/v1/validation';
const urlCommentApi = '/api/v1/comment';

const urlGetAllAuthors = '/api/v1/author';
const urlGetAllGenres = '/api/v1/genre';

let localizedMessages = new Map([
    ['error', 'Error'],
    ['error.detail', 'Details'],
    ['error.status.400', 'Bad Request'],
    ['error.status.404', 'Not Found'],
    ['error.status.405', 'Method Not Allowed'],
    ['error.status.500', 'Internal Server Error'],
]);


async function getLocalizedMessages(lang, errDiv = null) {
    const url = urlGetLocalizedMessage + '/' + lang;

    function applyLocalizeMessages(result) {
        Object.keys(result).forEach(key => {
            localizedMessages.set(key, result[key]);
        });
    }

    await getDataAsJsonAndApply(url, errDiv, applyLocalizeMessages);
}

function makeErr(e) {
    return {
        'status': 'error',
        'errorMessage': {'key': null, 'message': null},
        'statusMessage': {'key': '', 'message': e.message},
        'detailMessage': {key: '', 'message': e.stack}
    };
}

function showError(place, errorData) {
    const title = errorData.status;
    const message = (errorData.errorMessage === undefined || errorData.errorMessage === null || errorData.errorMessage.message === null)
        ? (localizedMessages.has(errorData.statusMessage.key)
            ? localizedMessages.get(errorData.statusMessage.key)
            : errorData.statusMessage.message)
        : (localizedMessages.has(errorData.errorMessage.key)
            ? localizedMessages.get(errorData.errorMessage.key)
            : errorData.errorMessage.message);

    const detail = (errorData.detailMessage !== undefined
        && errorData.detailMessage !== null
        && errorData.detailMessage.message !== null)
        ? (localizedMessages.has(errorData.detailMessage.key)
            ? localizedMessages.get(errorData.detailMessage.key)
            : errorData.detailMessage.message)
        : null;

    let detailText = '';
    if (detail !== null) {
        detailText = `<p class="text-decoration-underline mb-0">${localizedMessages.get('error.detail')}:</p>
                        <p class="mt-0 mb-0">${detail}</p>`
    }


    let alertDiv = document.createElement('div');
    alertDiv.innerHTML =
        `<div class="alert alert-danger alert-dismissible fade show" role="alert">
             <strong>${title}</strong>
             ${message}
             ${detailText}
             <button type="button" class="btn-close btn-sm" data-bs-dismiss="alert" aria-label="Close"></button>
         </div>`;
    place.append(alertDiv);
}

async function getDataAsJsonAndApply(url, errorContainer, successFunction,
                                     errorFunction = showError, additionalContent) {
    try {
        let response = await fetch(url,
            {
                method: "GET", headers: jsonRequestHeader
            });
        let data = await response.json();
        if (response.ok) {
            if (successFunction != null) {
                successFunction(data, additionalContent);
            }
        } else {
            errorFunction(errorContainer, data, additionalContent);
        }
    } catch (e) {
        errorFunction(errorContainer, makeErr(e), additionalContent);
    }
}

async function getDataAsJsonStreamAndApply(url, errorContainer,
                                           itemFunction, completeFunction,
                                           errorFunction = showError, additionalContent) {
    try {
        function onMessage(data, status) {
            if (itemFunction !== undefined) {
                itemFunction(data, status, additionalContent);
            }
        }

        function onComplete() {
            if (completeFunction !== undefined) {
                completeFunction(additionalContent);
            }
        }

        function onError(data, status) {
            function makeErr(e) {
                return {
                    'status': '200',
                    'errorMessage': {'key': null, 'message': null},
                    'statusMessage': {'key': '', 'message': e.message},
                };
            }
            if (status === undefined) {
                data = makeErr(data);
            }
            errorFunction(errorContainer, data, additionalContent);
        }


        const stream = fetch(url, {method: "GET", headers: ndJsonRequestHeader});
        stream
            .then(readStream(onMessage, onError))
            .then(onComplete)
            .catch(onError)
        ;
    } catch (e) {
        errorFunction(errorContainer, makeErr(e), additionalContent);
    }
}

async function doDelete(url, deleteId, errorContainer, successFunction) {
    try {
        let response = await fetch(url + '/' + deleteId,
            {
                method: 'DELETE', headers: jsonRequestHeader,
            });
        if (response.ok) {
            if (successFunction !== undefined) {
                successFunction();
            }
        } else {
            let result = await response.json();
            showError(errorContainer, result);
        }
    } catch (e) {
        showError(errorContainer, makeErr(e));
    }
}

async function sendDataAsJsonAndApply(url, method, dataToSend, errorContainer, successFunction,
                                      errorFunction = showError) {
    try {
        let response = await fetch(url,
            {
                method: method, headers: jsonRequestHeader,
                body: JSON.stringify(dataToSend)
            });
        let data = await response.json();
        if (response.ok) {
            if (successFunction != null) {
                successFunction(data);
            }
            return true;
        } else {
            errorFunction(errorContainer, data);
            return false;
        }
    } catch (e) {
        errorFunction(errorContainer, makeErr(e));
        return false;
    }
}