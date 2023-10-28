const errorContainer = document.getElementById('errorContainer');
const nameItem = document.getElementById('nameBook');
const nameErrDiv = document.getElementById('nameBookError');
const saveBtn = document.getElementById('btnAccept');

const authorList = document.getElementById('authorList');
const genreList = document.getElementById('genreList');

function clearLists() {
    authorList.innerHTML = '';
    genreList.innerHTML = '';
}

function applyAuthorList(data) {
    for (const author of data) {
        const option = document.createElement('option');
        option.value = author.name;
        authorList.append(option);
    }
}

function applyGenreList(data) {
    for (const genre of data) {
        const option = document.createElement('option');
        option.value = genre.name;
        genreList.append(option);
    }
}

async function validateBook() {
    let book = makeBook();
    try {
        let response = await fetch(urlValidateBook,
            {
                method: "POST", headers: jsonRequestHeader,
                body: JSON.stringify(book)
            });
        if (response.ok) {
            showValidateResult({});
            return true;
        } else {
            let result = await response.json();
            showValidateResult(result);
            return false;
        }
    } catch (e) {
        showValidateResult({});
        return false;
    }

}

function showValidateResult(result) {
    nameErrDiv.innerHTML = '';
    if (result.name === undefined) {
        if (!nameErrDiv.classList.contains('invisible')) {
            nameErrDiv.classList.add('invisible');
        }
    } else {
        let errDiv = document.createElement('div');
        let message = localizedMessages.has(result.name.key) ? localizedMessages.get(result.name.key) : result.name.message;
        errDiv.innerHTML = `<div class="alert alert-danger align-items-center m-0 p-0">${message}</div>`;
        nameErrDiv.append(errDiv);
        if (nameErrDiv.classList.contains('invisible')) {
            nameErrDiv.classList.remove('invisible');
        }
    }
}

function makeBook() {
    let idBook = document.getElementById('idBook').value;
    let nameBook = document.getElementById('nameBook').value;
    let authorId = document.getElementById('authorId').value;
    let authorName = document.getElementById('authorName').value;
    let genreId = document.getElementById('genreId').value;
    let genreName = document.getElementById('genreName').value;

    let book = {
        'id': (idBook === '') ? '0' : idBook, 'name': nameBook,
        'authorId': authorId, 'authorName': authorName,
        'genreId': genreId, 'genreName': genreName
    };
    return book;
}

async function saveBook() {
    if (!await validateBook()) {
        return;
    }
    let book = makeBook();
    let urlAction = urlBookApi;
    let method = 'POST';
    if (document.getElementById('action').value === 'edit'){
        urlAction = urlBookApi + '/' + book.id;
        method = 'PUT';
    }
    try {
        let response = await fetch(urlAction,
            {
                method: method, headers: jsonRequestHeader,
                body: JSON.stringify(book)
            });
        if (response.ok) {
            window.location.href = document.getElementById('backUrl').value;
        } else {
            let result = await response.json();
            showError(errorContainer, result);
        }
    } catch (e) {
        showError(errorContainer, makeErr(e));
    }
}

function showBook(data) {
    document.getElementById('idBook').value = data.id;
    document.getElementById('nameBook').value = data.name;
    document.getElementById('authorId').value = data.authorId;
    document.getElementById('authorName').value = data.authorName;
    document.getElementById('genreId').value = data.genreId;
    document.getElementById('genreName').value = data.genreName;
}

function disableSave(){
    saveBtn.disabled = true;
}


window.onload = async (event) => {
    nameItem.onblur = validateBook;
    saveBtn.onclick = saveBook;
    clearLists();
    await getLocalizedMessages(document.getElementById('lang').value, errorContainer)

    getDataAndApply(urlGetAllAuthors, errorContainer, applyAuthorList).then();
    getDataAndApply(urlGetAllGenres, errorContainer, applyGenreList).then();

    let id = document.getElementById('idBook').value;
    if (id !== '0') {
        getDataAndApply(urlBookApi + '/' + id, errorContainer, showBook, disableSave).then();
    }
}