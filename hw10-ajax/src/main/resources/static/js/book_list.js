const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#bookTable tbody');
const totalEl = document.getElementById('booksTotal');

let deleteModal;

async function loadBooks() {
    clearBooks();
    getDataAndApply(urlBookApi, errorContainer, showBooks, clearBooks).then();
}

function clearBooks() {
    if (!noClear){
        tbody.innerHTML = '';
        totalEl.innerHTML = '0';
    }
}

function showBooks(data) {
    let countBook = 0;

    const actionEdit = localizedMessages.has('book.action-edit')
        ? localizedMessages.get('book.action-edit')
        : 'Edit book';
    const actionViewComments = localizedMessages.has('book.action-view-comments')
        ? localizedMessages.get('book.action-view-comments')
        : 'View Comments';
    const actionDelete = localizedMessages.has('book.action-delete-book')
        ? localizedMessages.get('book.action-delete-book')
        : 'Delete book';
    data.forEach(book => {
        let row = document.createElement('tr');
        row.innerHTML = `<td>${book.id}</td>
                    <td>${book.name}</td>
                    <td>${book.authorName === null ? '' : book.authorName}</td>
                    <td>${book.genreName === null ? '' : book.genreName}</td>
                    <td>
                        <a class="btn btn-outline-secondary btn-sm" title="${actionEdit}"
                           href="/book?action=edit&id=${book.id}"> <i class="fa fa-edit"></i></a>
                        <a class="btn btn-outline-secondary btn-sm" title="${actionViewComments}"
                           href="/book?action=comments&id=${book.id}"> <i class="fa fa-comment"></i></a>
                        <btn class="btn btn-outline-secondary btn-sm" title="${actionDelete}"
                            onclick="callDeleteBook(${book.id});"> <i class="fa fa-remove"></i></btn>
                    </td>`;
        tbody.append(row);
        countBook++;
    });
    totalEl.innerHTML = countBook;
}

function callDeleteBook(bookId) {
    document.getElementById('deleteId').value = bookId;
    deleteModal.toggle();
}

async function doDelete() {
    const bookId = document.getElementById('deleteId').value;
    deleteModal.toggle();
    try{
        let response = await fetch(urlBookApi + '/' + bookId,
            {
                method: 'DELETE', headers: jsonRequestHeader,
            });
        if (response.ok){
            loadBooks().then();
        } else {
            let result = await response.json();
            showError(errorContainer, result);
        }
    } catch (e) {
        showError(errorContainer, makeErr(e));
    }
}

window.onload = async (event) => {
    document.getElementById('deleteBtn').onclick = doDelete;
    deleteModal = new bootstrap.Modal(document.getElementById('deleteQuery'));
    await getLocalizedMessages(document.getElementById('lang').value, errorContainer);
    await loadBooks();
}

