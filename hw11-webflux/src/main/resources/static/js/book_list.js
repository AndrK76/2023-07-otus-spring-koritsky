const errorContainer = document.getElementById('errorContainer');
const tbody = document.querySelector('#bookTable tbody');
const totalEl = document.getElementById('booksTotal');

let deleteModal;


window.onload = async (event) => {
    //document.getElementById('deleteBtn').onclick = doDelete;
    deleteModal = new bootstrap.Modal(document.getElementById('deleteQuery'));
    await getLocalizedMessages(document.getElementById('lang').value, errorContainer);
    //await loadBooks();
    //getBooks();
    getDataAsJsonStreamAndApply(urlBookApi, errorContainer).then();
}

function getBooks() {
    const streamErr = e => {
        console.warn("error");
        console.warn(e);
    }

    fetch(urlBookApi, {method: "GET", headers: ndJsonRequestHeader})
        .then((response) => {
            return can.ndjsonStream(response.body);
        })
        .then(dataStream => {
            const reader = dataStream.getReader();
            const read = result => {
                if (result.done) {
                    return;
                }
                render(result.value);
                reader.read().then(read, streamErr);
            }
            reader.read().then(read, streamErr);
        });

    const render = value => {
        console.log(value)
        /*
        const div = document.createElement('div');
        div.append('stringValue:', JSON.stringify(value));
        document.getElementById('dataBlock').append(div);
         */
    };


}