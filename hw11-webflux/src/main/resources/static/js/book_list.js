const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#bookTable tbody');
const totalEl = document.getElementById('booksTotal');

let deleteModal;


window.onload = async (event) => {
    //document.getElementById('deleteBtn').onclick = doDelete;
    deleteModal = new bootstrap.Modal(document.getElementById('deleteQuery'));
    await getLocalizedMessages(document.getElementById('lang').value, errorContainer);
    clearBooks();
    await getDataAsJsonStreamAndApply2(urlBookApi, errorContainer, showBook, afterLoadBooks);
}


let bookCounter = 0;
let actionEdit;
let actionViewComments;
let actionDelete;


function clearBooks() {
    if (!noClear){
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

function showBook(book){
    bookCounter++;
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
}

function afterLoadBooks(){
    totalEl.innerHTML = bookCounter.toString();
}
