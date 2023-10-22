const errorContainer = document.getElementById('errorContainer');
const textItem = document.getElementById('textComment');
const textErrDiv = document.getElementById('textCommentError');
const saveBtn = document.getElementById('btnAccept');

function disableSave() {
    saveBtn.disabled = true;
}

async function validateComment() {

}

window.onload = async (event) => {
    textItem.onblur = validateComment;
    //saveBtn.onclick = saveBook;
    await getLocalizedMessages(document.getElementById('lang').value, errorContainer)

    let id = document.getElementById('idBook').value;


    if (document.getElementById('action').value !== '') {
        console.log('action=' + document.getElementById('action').value);
        //     getDataAndApply(urlBookApi + '/' + id, errorContainer, showBook, disableSave).then();
    }
}