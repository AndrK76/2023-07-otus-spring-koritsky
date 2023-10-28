const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#commentTable tbody');
const totalEl = document.getElementById('commentsTotal');
const bookIdEl = document.getElementById('bookId');
const saveBtn = document.getElementById('saveBtn');
const textCommentItem = document.getElementById('textComment');

let deleteModal;
let editModal;

async function loadComments() {
    clearComments();
    getDataAndApply(urlBookApi + '/' + bookIdEl.value + '/comments', errorContainer, showComments, clearComments).then();
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
        row.innerHTML = `                    <td>${comment.id}</td>
                    <td>${comment.text}</td>
                    <td class="text-end">
                        <button class="btn btn-outline-secondary btn-sm" title="${actionEdit}"
                           onclick="callEditComment(${comment.id});"" > <i class="fa fa-edit"></i></button>
                        <button class="btn btn-outline-secondary btn-sm" title="${actionDelete}"
                             onclick="callDeleteComment(${comment.id});"><i class="fa fa-remove"></i></button>
                    </td>`
        tbody.append(row);
    }
    totalEl.innerHTML = countComments;
}


function callDeleteComment(commentId) {
    document.getElementById('deleteId').value = commentId;
    deleteModal.toggle();
}

async function callEditComment(commentId) {
    document.getElementById('editId').value = (commentId === undefined ? null : commentId);
    textCommentItem.value = '';
    saveBtn.disabled = false;
    textCommentItem.disabled = false;
    if (commentId === undefined) {
        const modalTitle = localizedMessages.has('comment.add-title')
            ? localizedMessages.get('comment.add-title')
            : 'New comment';
        document.getElementById('editCommentTitle').innerText = modalTitle;
        editModal.show();
    } else {
        const modalTitle = localizedMessages.has('comment.edit-title')
            ? localizedMessages.get('comment.edit-title')
            : 'Edit comment';
        document.getElementById('editCommentTitle').innerText = modalTitle;
        await getDataAndApply(urlCommentApi + '/' + commentId, errorContainer, showComment);
    }
}

function showComment(data) {
    editModal.show();
    textCommentItem.value = data.text;
}

async function validateComment() {
    const comment = {'id': '0', 'text': textCommentItem.value};
    saveBtn.disabled = true;
    textCommentItem.disabled = true;
    try {
        let response = await fetch(urlValidateComment,
            {
                method: "POST", headers: jsonRequestHeader,
                body: JSON.stringify(comment)
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
    const errShowDiv = document.getElementById('textCommentError');
    errShowDiv.innerHTML = '';
    if (result.text === undefined) {
        if (!errShowDiv.classList.contains('invisible')) {
            errShowDiv.classList.add('invisible');
        }
    } else {
        let errDiv = document.createElement('div');
        let message = localizedMessages.has(result.text.key) ? localizedMessages.get(result.text.key) : result.text.message;
        errDiv.innerHTML = `<div class="alert alert-danger align-items-center m-0 p-0">${message}</div>`;
        errShowDiv.append(errDiv);
        if (errShowDiv.classList.contains('invisible')) {
            errShowDiv.classList.remove('invisible');
        }
        saveBtn.disabled = false;
        textCommentItem.disabled = false;
    }
}

async function doSaveComment() {
    if (await validateComment()) {
        const commentId = document.getElementById('editId').value;
        const errorEditContainer = document.getElementById('errorContainerModal');

        let comment = {'id': commentId, 'text': textCommentItem.value};
        let method = 'POST';
        let urlAction = urlCommentApi;
        if (commentId === '') {
            comment.id = 0;
            comment.bookId = bookIdEl.value;
        } else {
            urlAction = urlCommentApi + '/' + comment.id;
            method = 'PUT';
        }
        try {
            let response = await fetch(urlAction,
                {
                    method: method, headers: jsonRequestHeader,
                    body: JSON.stringify(comment)
                });
            if (response.ok) {
                editModal.hide();
                loadComments().then();
            } else {
                let result = await response.json();
                showError(errorEditContainer, result);
                saveBtn.disabled = false;
                textCommentItem.disabled = false;
            }
        } catch (e) {
            showError(errorEditContainer, makeErr(e));
            saveBtn.disabled = false;
            textCommentItem.disabled = false;
        }
    }

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
    editModal = new bootstrap.Modal(document.getElementById('editComment'));
    saveBtn.onclick = doSaveComment;
    await getLocalizedMessages(document.getElementById('lang').value, errorContainer);
    await loadComments();
}