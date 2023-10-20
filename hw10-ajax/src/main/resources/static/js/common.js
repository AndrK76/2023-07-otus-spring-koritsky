const urlGetLocalizedMessage = '/api/v1/message'
const urlGetAllBook = '/api/v1/book'


const jsonRequestHeader = {'Accept': 'application/json', 'Content-Type': 'application/json'}

let localizedMessages = new Map([
    ['error', 'Error'],
    ['error.detail', 'Details'],
    ['error.status.400', 'Bad Request'],
    ['error.status.404', 'Not Found'],
    ['error.status.405', 'Method Not Allowed'],
    ['error.status.500', 'Internal Server Error'],
]);


async function getLocalizedMessages(lang, errDiv=null) {
    let response = await fetch(urlGetLocalizedMessage + '/' + lang,
        {method: "GET", headers: jsonRequestHeader});
    let result = await response.json();
    if (response.ok) {
        Object.keys(result).forEach(key => {
            localizedMessages.set(key, result[key]);
        });
    } else {
        if (errDiv !== null){
            showError(errDiv, result);
        } else {
            console.log(JSON.stringify(result));
        }
    }
}

function showError(place, errorData) {
    //console.log(errorData);
    const title = errorData.errorMessage === null
        ? errorData.status
        : 'title';
    const message = errorData.errorMessage === null
        ? (localizedMessages.has(errorData.statusMessageKey)
            ? localizedMessages.get(errorData.statusMessageKey)
            : errorData.statusMessage)
        : 'message';

    const detail = errorData.detailMessage !== null
        ? (localizedMessages.has(errorData.detailMessageKey)
            ? localizedMessages.get(errorData.detailMessageKey)
            : errorData.detailMessage)
        : null;

    let detailText = '';
    if (detail !== null){
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
