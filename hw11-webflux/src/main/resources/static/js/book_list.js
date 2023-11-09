const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#bookTable tbody');
const totalEl = document.getElementById('booksTotal');
let deleteModal;
let editBookModal;
let editCommentModal;


window.onload = async (event) => {
    deleteModal = new bootstrap.Modal(document.getElementById('deleteQuery'));
    editBookModal = new bootstrap.Modal(document.getElementById('editBookModal'));
    editCommentModal = new bootstrap.Modal(document.getElementById('editCommentModal'));
    bookNameItem.onblur = validateBookName;
    saveBtn.onclick = doSaveBook;
    commentTextItem.onblur = validateCommentText;
    saveCommentBtn.onclick = doSaveComment;

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
    fillBookRowContent(book);
}

function fillBookRowContent(book) {
    let row = document.getElementById('row_' + book.id);
    if (row===null){
        row = document.createElement('tr');
        row.setAttribute('id', 'row_' + book.id);
        tbody.append(row);
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
    } else {
        row.cells[1].innerText = book.name;
        row.cells[2].innerText = book.authorName === null ? '' : book.authorName;
        row.cells[3].innerText = book.genreName === null ? '' : book.genreName;
    }
    return row;

}

function displayCountBook() {
    const couRows = tbody.querySelectorAll('tr').length -
        tbody.querySelectorAll('tr.comment-row').length;
    totalEl.innerHTML = couRows.toString();
}

function getDeleteHeader(mode) {
    let ret = localizedMessages.has('action.confirm-delete')
        ? localizedMessages.get('action.confirm-delete')
        : 'Confirm delete {0}';
    if (ret.includes('{0}')) {
        const subj = localizedMessages.has(mode) ? localizedMessages.get(mode) : mode;
        ret = ret.replace('{0}', subj);
    }
    return ret;
}

function callDeleteBook(bookId) {
    document.getElementById('deleteBtn').onclick = doDeleteBook;
    document.getElementById('deleteId').value = bookId;
    document.getElementById('deleteQueryTitle').innerText = getDeleteHeader('book');
    deleteModal.toggle();
}

function doDeleteBook() {
    function afterDelete() {
        deleteModal.toggle();
        deleteBookRow();
    }

    const bookId = document.getElementById('deleteId').value;
    const errorDelContainer = document.getElementById('errorContainerDeleteQuery');
    doDelete(urlBookApi, bookId, errorDelContainer, afterDelete).then();
}

function deleteBookRow() {
    const bookId = document.getElementById('deleteId').value;
    const bookRow = document.getElementById('row_' + bookId);
    bookRow.remove();
    const commentRow = document.getElementById('commentRow_' + bookId);
    if (commentRow !== null) {
        commentRow.remove();
    }
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
        const row = fillBookRowContent(book);
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
    let row = document.getElementById('commentRow_'+ comment.id);
    if (row==null){
        row = document.createElement('tr');
        row.setAttribute('id', 'commentRow_' + comment.id);
        info.dataBody.append(row);
    }
    row.innerHTML = `<td>${comment.id}</td>
                    <td>${comment.text}</td>
                    <td class="text-end">
                        <button class="btn btn-outline-secondary btn-sm" title="${actionEditComment}"
                           onclick="callEditComment('edit', '${info.id}', '${comment.id}');"><i class="fa fa-edit"></i></button>
                        <button class="btn btn-outline-secondary btn-sm" title="${actionDeleteComment}"
                             onclick="callDeleteComment('${info.id}', '${comment.id}');"><i class="fa fa-remove"></i></button>
                    </td>`
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

function callDeleteComment(bookId, commentId) {
    document.getElementById('deleteBtn').onclick = doDeleteComment;
    document.getElementById('deleteId').value = commentId;
    document.getElementById('deleteDopId').value = bookId;
    document.getElementById('deleteQueryTitle').innerText = getDeleteHeader('comment');
    deleteModal.toggle();
}

function doDeleteComment() {

    function afterDelete() {
        deleteModal.toggle();
        document.getElementById('commentRow_' + commentId).remove();
        const bookRow = document.getElementById('commentRow_' + bookId);
        const bodyEl = bookRow.querySelector('td div table tbody');
        const totalEl = bookRow.querySelector('td div table tfoot tr :nth-child(2)');
        const info = {dataBody: bodyEl, dataTotal: totalEl};
        showCountComments(info);
    }

    const errorDelContainer = document.getElementById('errorContainerDeleteQuery');

    const commentId = document.getElementById('deleteId').value;
    const bookId = document.getElementById('deleteDopId').value;

    doDelete(urlCommentApi, commentId, errorDelContainer, afterDelete).then();
}

const commentTextItem = document.getElementById('textComment');
const commentTextErrorDiv = document.getElementById('textCommentError');
const saveCommentBtn = document.getElementById('saveCommentBtn');

function callEditComment(action, bookId, commentId) {
    clearCommentTextError();
    document.getElementById('errorContainerModalComment').innerHTML = '';
    saveCommentBtn.disabled = false;
    const bookRow = document.getElementById("row_" + bookId);
    document.getElementById('bookForComment').value = bookRow.cells[1].innerText;
    document.getElementById('editComment_bookId').value = bookId;
    document.getElementById('editComment_action').value = action;
    if (commentId !== null) {
        document.getElementById('editComment_id').value = commentId;
        document.getElementById('editCommentTitle').innerText =
            (localizedMessages.has('comment.edit-title')
                ? localizedMessages.get('comment.edit-title')
                : 'Edit comment');
        const commentRow = document.getElementById("commentRow_" + commentId);
        commentTextItem.value = commentRow.cells[1].innerText;
    } else {
        document.getElementById('editComment_id').value = '';
        document.getElementById('editCommentTitle').innerText =
            (localizedMessages.has('comment.add-title')
                ? localizedMessages.get('comment.add-title')
                : 'New comment');
        commentTextItem.value = '';
    }

    editCommentModal.toggle();
}

function clearCommentTextError() {
    commentTextErrorDiv.innerHTML = '';
    if (!commentTextErrorDiv.classList.contains('invisible')) {
        commentTextErrorDiv.classList.add('invisible');
    }
}

function makeComment() {
    let idComment = document.getElementById('editComment_id').value;
    let textComment = commentTextItem.value;

    let comment = {
        'id': (idComment === '') ? '' : idComment, 'text': textComment
    };
    return comment;
}

async function validateCommentText() {
    const errDiv = document.getElementById('errorContainerModalComment');

    function showCommentError(errDiv, data) {
        if (('errorMessage' in data) && ('key' in data.errorMessage)
            && data.errorMessage.key === 'argument-error'
            && ('details' in data) && ('text' in data.details)) {
            let msgKey = data.details.text.key;
            let message = localizedMessages.has(msgKey) ? localizedMessages.get(msgKey) : data.details.name.message;
            let msgDiv = document.createElement('div');
            msgDiv.innerHTML = `<div class="alert alert-danger align-items-center m-0 p-0">${message}</div>`;
            commentTextErrorDiv.append(msgDiv);
            if (commentTextErrorDiv.classList.contains('invisible')) {
                commentTextErrorDiv.classList.remove('invisible');
            }
        } else {
            showError(errDiv, data);
        }
    }

    clearCommentTextError();
    return await sendDataAsJsonAndApply(urlValidateApi + '/comment', 'POST', makeComment(),
        errDiv, null, showCommentError);
}

async function doSaveComment() {
    const errDiv = document.getElementById('errorContainerModalComment');
    saveCommentBtn.disabled = true;

    const action = document.getElementById('editComment_action').value;
    const bookId = document.getElementById('editComment_bookId').value;
    const commentId = document.getElementById('editComment_id').value;

    function afterSaveComment(comment) {
        const bookRow = document.getElementById('commentRow_' + bookId);
        const bodyEl = bookRow.querySelector('td div table tbody');
        const totalEl = bookRow.querySelector('td div table tfoot tr :nth-child(2)');
        const info = {id: bookId, dataBody: bodyEl, dataTotal: totalEl};
        showComment(comment, null, info)
        showCountComments(info);
        editCommentModal.toggle();
    }

    if (await validateCommentText()) {
        const comment = makeComment();
        let method = 'POST';
        let url = urlCommentApi;
        if (action === 'edit') {
            method = 'PUT';
            url += '/' + commentId;
        } else {
            url = urlBookApi + '/' + bookId + '/comment';
        }
        if (!await sendDataAsJsonAndApply(url, method, comment, errDiv, afterSaveComment)) {
            saveCommentBtn.disabled = false;
        }
    } else {
        saveCommentBtn.disabled = false;
    }
}



