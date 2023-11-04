const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#bookTable tbody');
const totalEl = document.getElementById('booksTotal');
let deleteModal;
let editBookModal;


window.onload = async (event) => {
    deleteModal = new bootstrap.Modal(document.getElementById('deleteQuery'));
    editBookModal = new bootstrap.Modal(document.getElementById('editBookModal'));
    document.getElementById('deleteBtn').onclick = doDeleteBook;
    bookNameItem.onblur = validateBookName;
    saveBtn.onclick = doSaveBook;

    await getLocalizedMessages(document.getElementById('lang').value, errorContainer);
    loadBooks().then();

}

let actionEdit;
let actionShowComments;
let actionHideComments;
let actionDelete;
let actionNewComment;
let actionEditComment;
let actionDeleteComment


function setLocalizedActions() {
    actionEdit = localizedMessages.has('book.action-edit')
        ? localizedMessages.get('book.action-edit')
        : 'Edit book';
    actionShowComments = localizedMessages.has('book.action-show-comments')
        ? localizedMessages.get('book.action-show-comments')
        : 'Show Comments';
    actionHideComments = localizedMessages.has('book.action-hide-comments')
        ? localizedMessages.get('book.action-hide-comments')
        : 'Hide Comments';
    actionDelete = localizedMessages.has('book.action-delete-book')
        ? localizedMessages.get('book.action-delete-book')
        : 'Delete book';
    actionNewComment = localizedMessages.has('comment.action-add')
        ? localizedMessages.get('comment.action-add')
        : 'New comment';
    actionEditComment = localizedMessages.has('comment.action-edit')
        ? localizedMessages.get('comment.action-edit')
        : 'Edit comment';
    actionDeleteComment = localizedMessages.has('comment.action-delete')
        ? localizedMessages.get('comment.action-delete')
        : 'Delete comment';
}

function clearBooks() {
    if (!noClear) {
        tbody.innerHTML = '';
        totalEl.innerHTML = '???';
    }
    setLocalizedActions();
}

async function loadBooks() {
    clearBooks();
    getDataAsJsonStreamAndApply(urlBookApi, errorContainer, showBook, displayCountBook).then();
}

function showBook(book) {
    let row = document.createElement('tr');
    row.setAttribute('id', 'row_' + book.id);
    fillBookRowContent(book, row);
    tbody.append(row);
}

function fillBookRowContent(book, row) {
    row.innerHTML = `<td>${book.id}</td>
                    <td>${book.name}</td>
                    <td>${book.authorName === null ? '' : book.authorName}</td>
                    <td>${book.genreName === null ? '' : book.genreName}</td>
                    <td class="text-end">
                        <button class="btn btn-outline-secondary btn-sm" title="${actionEdit}"
                           onclick="callEditBook('${book.id}');"> <i class="fa fa-edit"></i></button>
                        <button class="btn btn-outline-secondary btn-sm show-comment" title="${actionShowComments}"
                           onclick="showComments('${book.id}');"> <i class="fa fa-comment"></i></button>
                        <button class="btn btn-outline-secondary btn-sm" title="${actionDelete}"
                            onclick="callDeleteBook('${book.id}');"> <i class="fa fa-remove"></i></button>
                    </td>`;
}

function displayCountBook() {
    const couRows = tbody.querySelectorAll('tr').length -
        tbody.querySelectorAll('tr.comment-row').length;
    totalEl.innerHTML = couRows.toString();
}

function callDeleteBook(bookId) {
    document.getElementById('deleteId').value = bookId;
    deleteModal.toggle();
}

function doDeleteBook() {
    const bookId = document.getElementById('deleteId').value;
    deleteModal.toggle();
    window.scrollTo({top: 0, behavior: 'smooth'});
    doDelete(urlBookApi, bookId, errorContainer, deleteBookRow).then();
}

function deleteBookRow() {
    const bookId = document.getElementById('deleteId').value;
    const bookRow = document.getElementById('row_' + bookId);
    bookRow.remove();
    displayCountBook();
}

const bookNameItem = document.getElementById('nameBook');

function callEditBook(bookId) {
    saveBtn.disabled = false;
    clearBookNameError();
    document.getElementById('errorContainerModalBook').innerHTML = '';
    getDataAsJsonAndApply(urlGetAllAuthors, errorContainer, applyAuthorList).then();
    getDataAsJsonAndApply(urlGetAllGenres, errorContainer, applyGenreList).then();

    const bookAuthorItem = document.getElementById('authorName');
    const bookGenreItem = document.getElementById('genreName');
    if (bookId !== undefined) {
        document.getElementById('editBookId').value = bookId;
        document.getElementById('editBookAction').value = 'edit';
        document.getElementById('editBookTitle').innerText =
            (localizedMessages.has('book.edit-title')
                ? localizedMessages.get('book.edit-title')
                : 'Edit book');
        const bookRow = document.getElementById('row_' + bookId);
        bookNameItem.value = bookRow.cells[1].innerText;
        bookAuthorItem.value = bookRow.cells[2].innerText;
        bookGenreItem.value = bookRow.cells[3].innerText;

    } else {
        document.getElementById('editBookId').value = '';
        document.getElementById('editBookAction').value = 'add';
        document.getElementById('editBookTitle').innerText =
            (localizedMessages.has('book.add-title')
                ? localizedMessages.get('book.add-title')
                : 'New book');
        bookNameItem.value = '';
        bookAuthorItem.value = '';
        bookGenreItem.value = '';
    }
    editBookModal.toggle();
}

function makeBook() {
    let idBook = document.getElementById('editBookId').value;
    let nameBook = bookNameItem.value;
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

function applyAuthorList(data) {
    const authorList = document.getElementById('authorList');
    authorList.innerHTML = '';
    for (const author of data) {
        const option = document.createElement('option');
        option.value = author.name;
        authorList.append(option);
    }
}

function applyGenreList(data) {
    const genreList = document.getElementById('genreList');
    genreList.innerHTML = '';
    for (const genre of data) {
        const option = document.createElement('option');
        option.value = genre.name;
        genreList.append(option);
    }
}

const nameBookErrorDiv = document.getElementById('nameBookError');

function clearBookNameError() {
    nameBookErrorDiv.innerHTML = '';
    if (!nameBookErrorDiv.classList.contains('invisible')) {
        nameBookErrorDiv.classList.add('invisible');
    }
}

async function validateBookName() {
    const errDiv = document.getElementById('errorContainerModalBook');

    function showBookError(errDiv, data) {
        if (('errorMessage' in data) && ('key' in data.errorMessage)
            && data.errorMessage.key === 'argument-error'
            && ('details' in data) && ('name' in data.details)) {
            let msgKey = data.details.name.key;
            let message = localizedMessages.has(msgKey) ? localizedMessages.get(msgKey) : data.details.name.message;
            let msgDiv = document.createElement('div');
            msgDiv.innerHTML = `<div class="alert alert-danger align-items-center m-0 p-0">${message}</div>`;
            nameBookErrorDiv.append(msgDiv);
            if (nameBookErrorDiv.classList.contains('invisible')) {
                nameBookErrorDiv.classList.remove('invisible');
            }
        } else {
            showError(errDiv, data);
        }

    }

    clearBookNameError();

    return await sendDataAsJsonAndApply(urlValidateApi + '/book', 'POST', makeBook(),
        errDiv, null, showBookError);
}

const saveBtn = document.getElementById('saveBookBtn');

async function doSaveBook() {
    function afterSaveBook(book) {
        const bookId = book.id;
        let row = document.getElementById('row_' + bookId);
        if (action === 'add') {
            row = document.createElement('tr');
            row.setAttribute('id', 'row_' + book.id);
            tbody.append(row);
        }
        fillBookRowContent(book, row);
        displayCountBook();
        editBookModal.toggle();
        window.scrollTo({top: row.offsetTop, behavior: 'smooth'});
    }

    const action = document.getElementById('editBookAction').value;
    const errDiv = document.getElementById('errorContainerModalBook');
    saveBtn.disabled = true;

    const book = makeBook();
    if (await validateBookName()) {
        let method = 'POST';
        let url = urlBookApi;
        if (action === 'edit') {
            method = 'PUT';
            url += '/' + book.id;
        }
        if (!await sendDataAsJsonAndApply(url, method, book, errDiv, afterSaveBook)) {
            saveBtn.disabled = false;
        }
    } else {
        saveBtn.disabled = false;
    }
}

function showComment(comment, status, info) {
    const row = document.createElement('tr');
    row.setAttribute('id', 'commentRow_' + comment.id);
    row.innerHTML = `<td>${comment.id}</td>
                    <td>${comment.text}</td>
                    <td class="text-end">
                        <button class="btn btn-outline-secondary btn-sm" title="${actionEditComment}"
                           onclick="callEditComment('edit', '${info.id}', '${comment.id}');"><i class="fa fa-edit"></i></button>
                        <button class="btn btn-outline-secondary btn-sm" title="${actionDeleteComment}"
                             onclick="callDeleteComment('${info.id}', '${comment.id}');"><i class="fa fa-remove"></i></button>
                    </td>`
    info.dataBody.append(row);
}

function showCountComments(info) {
    const couRows = info.dataBody.querySelectorAll('tr').length;
    info.dataTotal.innerHTML = couRows.toString();
}

async function showComments(bookId) {
    function onGetCommentError(errorContainer, data, info) {
        showError(errorContainer, data);
    }

    function prepareCommentArea() {
        const row = document.getElementById('row_' + bookId);
        const commentText = localizedMessages.has('comment.text')
            ? localizedMessages.get('comment.text')
            : 'Comment text';
        const commentTotal = localizedMessages.has('comment.total')
            ? localizedMessages.get('comment.total')
            : 'Total comments for book:';
        const commentRow = tbody.insertRow(row.rowIndex);
        info.commentRow = commentRow;
        commentRow.setAttribute('id', 'commentRow_' + bookId);
        commentRow.setAttribute('class', 'comment-row');
        commentRow.innerHTML = `
        <td colspan=5><div class="row comment-row"><div class="col border rounded ms-5 me-1">
            <table class="table table-hover w-100">
                <thead>
                <tr>
                    <th>Id</th>
                    <th>${commentText}</th>
                    <th class="text-end">
                        <button class="btn btn-outline-secondary btn-sm text-nowrap"
                                onclick="callEditComment('add','${info.id}',null);">
                            <i class="fa fa-add"></i>
                            <span>${actionNewComment}</span></button>
                    </th>
                </tr>
                </thead>
                <tbody>
                </tbody>
                <tfoot>
                <tr>
                    <td colspan="2" class="fst-italic">${commentTotal}</td>
                    <th class="text-end">???</th>
                </tr>
                </tfoot>
            </table>
        </div></div></td>`;
        info.dataBody = commentRow.querySelector('td div table tbody');
        info.dataTotal = commentRow.querySelector('td div table tfoot tr :nth-child(2)');
        const btn = row.querySelector('.show-comment');
        btn.setAttribute('onclick', "hideComments('" + bookId + "');");
        btn.setAttribute('title', actionHideComments);
        info.commentBtn = btn;
    }

    let info = {id: bookId};
    prepareCommentArea();

    const url = urlBookApi + '/' + bookId + '/comment';
    await getDataAsJsonStreamAndApply(url, errorContainer, showComment,
        showCountComments, onGetCommentError, info);


}

function hideComments(bookId) {
    const row = document.getElementById('row_' + bookId);
    const btn = row.querySelector('.show-comment');
    btn.setAttribute('onclick', "showComments('" + bookId + "');");
    btn.setAttribute('title', actionShowComments);
    const commentRow = document.getElementById('commentRow_' + bookId);
    commentRow.remove();
}

function callEditComment(action, bookId, commentId) {
    console.log(action);
    console.log('book: ' + bookId);
    console.log('comment: ' + commentId);
}

function callDeleteComment(bookId, commentId) {
    console.log('delete');
    console.log('book: ' + bookId);
    console.log('comment: ' + commentId);
}


