const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#commentTable tbody');
const totalEl = document.getElementById('commentsTotal');
const bookIdEl = document.getElementById('bookId');

let deleteModal;

async function loadComments() {
    clearComments();
    getDataAndApply(urlBookApi + '/' + bookIdEl.value + '/comments', errorContainer, showComments, clearComments).then();
    console.log(bookIdEl.value);
}

function clearComments() {
    if (!noClear) {
        tbody.innerHTML = '';
        totalEl.innerHTML = '0';
    }
}

function showComments(data) {
    let countComments = 0;
    bookIdEl.value = data.id;
    document.getElementById('bookName').value = data.name;
    document.getElementById('authorName').value = data.authorName;
    document.getElementById('genreName').value = data.genreName;

    const actionEdit = localizedMessages.has('comment.action-edit')
        ? localizedMessages.get('comment.action-edit')
        : 'Edit comment';
    const actionDelete = localizedMessages.has('comment.action-delete')
        ? localizedMessages.get('comment.action-delete')
        : 'Delete comment';

    for (const comment of data.comments) {
        countComments++;
        let row = document.createElement('tr');
        row.innerHTML = `                    <td>1</td>
                    <td>${comment.text}</td>
                    <td class="text-end">
                        <a class="btn btn-outline-secondary btn-sm" title="${actionEdit}"
                           href="/comment?action=edit&id=${comment.id}&book=${bookIdEl.value}"> <i class="fa fa-edit"></i></a>
                        <btn class="btn btn-outline-secondary btn-sm" title="${actionDelete}"
                             onclick="callDeleteComment(${comment.id});"><i class="fa fa-remove"></i></btn>
                    </td>`
        tbody.append(row);
    }
    totalEl.innerHTML = countComments;
}


function callDeleteComment(commentId) {
    document.getElementById('deleteId').value = commentId;
    deleteModal.toggle();
}

async function doDelete() {
    const commentId = document.getElementById('deleteId').value;
    deleteModal.toggle();
    try {
        let response = await fetch(urlCommentApi + '/' + commentId,
            {
                method: 'DELETE', headers: jsonRequestHeader,
            });
        if (response.ok) {
            loadComments().then();
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
    await loadComments();
}