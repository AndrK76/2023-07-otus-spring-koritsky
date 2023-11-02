const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#bookTable tbody');
const totalEl = document.getElementById('booksTotal');
let deleteModal;
let editBookModal;


window.onload = async (event) => {
    deleteModal = new bootstrap.Modal(document.getElementById('deleteQuery'));
    editBookModal = new bootstrap.Modal(document.getElementById('editBookModal'));
    document.getElementById('deleteBtn').onclick = doDeleteBook;

    await getLocalizedMessages(document.getElementById('lang').value, errorContainer);
    loadBooks().then();

}

let bookCounter = 0;
let actionEdit;
let actionViewComments;
let actionDelete;


function clearBooks() {
    if (!noClear) {
        tbody.innerHTML = '';
        totalEl.innerHTML = '???';
        bookCounter = 0;
    }
    actionEdit = localizedMessages.has('book.action-edit')
        ? localizedMessages.get('book.action-edit')
        : 'Edit book';
    actionViewComments = localizedMessages.has('book.action-view-comments')
        ? localizedMessages.get('book.action-view-comments')
        : 'View Comments';
    actionDelete = localizedMessages.has('book.action-delete-book')
        ? localizedMessages.get('book.action-delete-book')
        : 'Delete book';
}

async function loadBooks() {
    clearBooks();
    getDataAsJsonStreamAndApply(urlBookApi, errorContainer, showBook, afterLoadBooks).then();
}

function showBook(book) {
    bookCounter++;
    let row = document.createElement('tr');
    row.setAttribute('id', 'row_' + book.id);
    row.innerHTML = `<td>${book.id}</td>
                    <td>${book.name}</td>
                    <td>${book.authorName === null ? '' : book.authorName}</td>
                    <td>${book.genreName === null ? '' : book.genreName}</td>
                    <td>
                        <btn class="btn btn-outline-secondary btn-sm" title="${actionEdit}"
                           onclick="callEditBook('${book.id}');"> <i class="fa fa-edit"></i></btn>
                        <a class="btn btn-outline-secondary btn-sm" title="${actionViewComments}"
                           href="/book?action=comments&id=${book.id}"> <i class="fa fa-comment"></i></a>
                        <btn class="btn btn-outline-secondary btn-sm" title="${actionDelete}"
                            onclick="callDeleteBook('${book.id}');"> <i class="fa fa-remove"></i></btn>
                    </td>`;
    tbody.append(row);
}

function afterLoadBooks() {
    totalEl.innerHTML = bookCounter.toString();
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
}

function callEditBook(bookId) {
    const bookNameItem = document.getElementById('nameBook');
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