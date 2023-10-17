async function loadBooks() {
    let response = await fetch(urlGetAllBook, {method: "GET", headers: jsonRequestHeader});
    let data = await response.json();
    showBooks({status: response.status, data: data});
}

async function loadLocalize(lang) {
    console.log(lang);
}

let localize = new Map([
    ['book.action-edit', 'Edit book'],
    ['book.action-view-comments', 'View comments'],
    ['book.action-delete-book', 'Delete book']
]);


function showBooks(response) {
    const tbody = document.querySelector('#bookTable tbody');
    const totalEl = document.getElementById('booksTotal');
    tbody.innerHTML = '';
    totalEl.innerHTML = '0';
    let countBook = 0;

    if (response.status === 200) {
        response.data.forEach(book => {
            let row = document.createElement('tr');
            row.innerHTML = `<td>${book.id}</td>
                    <td>${book.name}</td>
                    <td>${book.authorName === null ? '' : book.authorName}</td>
                    <td>${book.genreName === null ? '' : book.genreName}</td>
                    <td>
                        <a class="btn btn-outline-secondary btn-sm" title="${localize.get('book.action-edit')}"
                           href="#"> <i class="fa fa-edit"></i></a>
                        <a class="btn btn-outline-secondary btn-sm" title="${localize.get('book.action-view-comments')}"
                           href="#"> <i class="fa fa-comment"></i></a>
                        <a class="btn btn-outline-secondary btn-sm" title="${localize.get('book.action-delete-book')}"
                           href="#"> <i class="fa fa-remove"></i></a>
                    </td>`;
            tbody.append(row);
            countBook++;
        });
        totalEl.innerHTML = countBook;
    } else {
        console.log(response.status + ', ' + JSON.stringify(response.data, null, 4));
    }
}


window.onload = async (event) => {
    await loadLocalize(document.getElementById('lang').value)
    await loadBooks();
}