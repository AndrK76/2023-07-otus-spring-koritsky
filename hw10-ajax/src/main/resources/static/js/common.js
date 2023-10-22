const urlGetLocalizedMessage = '/api/v1/message';

const urlGetAllAuthors = '/api/v1/author';
const urlGetAllGenres = '/api/v1/genre';

const urlBookApi = '/api/v1/book';
const urlValidateBook = '/api/v1/validation/book';

const urlCommentApi = '/api/v1/comment';

const jsonRequestHeader = {'Accept': 'application/json', 'Content-Type': 'application/json'}

let localizedMessages = new Map([
    ['error', 'Error'],
    ['error.detail', 'Details'],
    ['error.status.400', 'Bad Request'],
    ['error.status.404', 'Not Found'],
    ['error.status.405', 'Method Not Allowed'],
    ['error.status.500', 'Internal Server Error'],
]);


async function getLocalizedMessages(lang, errDiv = null) {
    try {
        let response = await fetch(urlGetLocalizedMessage + '/' + lang,
            {method: "GET", headers: jsonRequestHeader});
        let result = await response.json();
        if (response.ok) {
            Object.keys(result).forEach(key => {
                localizedMessages.set(key, result[key]);
            });
        } else {
            if (errDiv !== null) {
                showError(errDiv, result);
            } else {
                console.log(JSON.stringify(result));
            }
        }
    } catch (e) {
        if (errDiv !== null) {
            showError(errDiv, makeErr(e));
        } else {
            console.log(e);
        }
    }
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
    //console.log(errorData);
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

async function getDataAndApply(url, errorContainer, successFunction, errorFunction) {
    try {
        let response = await fetch(url,
            {
                method: "GET", headers: jsonRequestHeader
            });
        let data = await response.json();
        if (response.ok) {
            if (successFunction != null) {
                successFunction(data);
            }
        } else {
            showError(errorContainer, data);
            if (errorFunction != null) {
                errorFunction(response.status);
            }
        }
    } catch (e) {
        showError(errorContainer, makeErr(e));
        if (errorFunction != null) {
            errorFunction(404);
        }
    }
}
