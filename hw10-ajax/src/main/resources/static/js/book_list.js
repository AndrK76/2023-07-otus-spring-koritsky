const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#bookTable tbody');
const totalEl = document.getElementById('booksTotal');

async function loadBooks() {
    clearBooks();
    getDataAndApply(urlBookApi, errorContainer, showBooks, clearBooks).then();
}

function clearBooks() {
    tbody.innerHTML = '';
    totalEl.innerHTML = '0';
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
                           href="/book/edit/${book.id}"> <i class="fa fa-edit"></i></a>
                        <a class="btn btn-outline-secondary btn-sm" title="${actionViewComments}"
                           href="#"> <i class="fa fa-comment"></i></a>
                        <a class="btn btn-outline-secondary btn-sm" title="${actionDelete}"
                           href="#"> <i class="fa fa-remove"></i></a>
                    </td>`;
        tbody.append(row);
        countBook++;
    });
    totalEl.innerHTML = countBook;
}

window.onload = async (event) => {
    await getLocalizedMessages(document.getElementById('lang').value, errorContainer);
    await loadBooks();
}