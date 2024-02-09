class BookManager {
    bookApi = '/api/v1/book';
    authorApi = '/api/v1/author';
    genreApi = '/api/v1/genre';
    validateApi = '/api/v1/validation'
    commentApi = '/api/v1/comment';

    localizedActions = new Map();
    isBookModifier = false;
    isCommentModifier = false;


    tbody = document.querySelector('#bookTable tbody');
    totalEl = document.getElementById('booksTotal');
    editBookModal = {}
    deleteModal = {}
    editCommentModal = {}


    setLocalizedActions() {
        bookManager.localizedActions.set('actionEdit',
            localizedMessages.has('book.action-edit')
                ? localizedMessages.get('book.action-edit')
                : 'Edit book');
        bookManager.localizedActions.set('actionShowComments',
            localizedMessages.has('book.action-show-comments')
                ? localizedMessages.get('book.action-show-comments')
                : 'Show Comments');
        bookManager.localizedActions.set('actionHideComments',
            localizedMessages.has('book.action-hide-comments')
                ? localizedMessages.get('book.action-hide-comments')
                : 'Hide Comments');
        bookManager.localizedActions.set('actionDelete',
            localizedMessages.has('book.action-delete-book')
                ? localizedMessages.get('book.action-delete-book')
                : 'Delete book');
        bookManager.localizedActions.set('actionNewComment',
            localizedMessages.has('comment.action-add')
                ? localizedMessages.get('comment.action-add')
                : 'New comment');
        bookManager.localizedActions.set('actionEditComment',
            localizedMessages.has('comment.action-edit')
                ? localizedMessages.get('comment.action-edit')
                : 'Edit comment');
        bookManager.localizedActions.set('actionDeleteComment',
            localizedMessages.has('comment.action-delete')
                ? localizedMessages.get('comment.action-delete')
                : 'Delete comment');
    }

    async setBookModifier() {
        bookManager.isBookModifier = true;
    }

    async setCommentModifier() {
        bookManager.isCommentModifier = true;
    }

    initPage() {
        bookManager.clearPage('???');
        $('#btnAdd').on('click', bookManager.callAddBook);
        bookManager.editBookModal = new bootstrap.Modal(document.getElementById('editBookModal'));
        bookManager.deleteModal = new bootstrap.Modal(document.getElementById('deleteQuery'));
        bookManager.editCommentModal = new bootstrap.Modal(document.getElementById('editCommentModal'));
        $('#nameBook').on('blur', bookManager.validateBookName);
        $('#saveBookBtn').on('click', bookManager.doSaveBook);
        $('#textComment').on('blur', bookManager.validateCommentText);
        $('#saveCommentBtn').on('click', bookManager.doSaveComment);
    }

    clearPage(couBook) {
        bookManager.tbody.innerHTML = '';
        bookManager.totalEl.innerHTML = couBook;
    }

    getBookList() {
        $('#btnAdd').addClass('invisible');
        manager.getJson(bookManager.bookApi).then(data => {
            manager.applyApiExchangeResult(data, bookManager.paintBookList);
        });
    }

    paintBookList(data) {
        for (const book of data) {
            bookManager.showBook(book);
        }
        if (bookManager.isBookModifier) {
            $('#btnAdd').removeClass('invisible');
        }
        bookManager.showCountBook();
    }

    showBook(book) {
        let row = document.getElementById('row_' + book.id);
        if (row === null) {
            row = document.createElement('tr');
            row.setAttribute('id', 'row_' + book.id);
            row.setAttribute('class', 'main-row');
            bookManager.tbody.append(row);
            let btnCell = '';
            if (bookManager.isBookModifier) {
                btnCell = `<button class="btn btn-outline-secondary btn-sm" 
                        title="${bookManager.localizedActions.get('actionEdit')}"> <i class="fa fa-edit"></i></button>
                        <button class="btn btn-outline-secondary btn-sm show-comment" tag="closed"
                        title="${bookManager.localizedActions.get('actionShowComments')}"> <i class="fa fa-comment"></i></button>
                        <button class="btn btn-outline-secondary btn-sm" 
                        title="${bookManager.localizedActions.get('actionDelete')}"><i class="fa fa-remove"></i></button>`;
            } else {
                btnCell = `<button class="btn btn-outline-secondary btn-sm show-comment" tag="closed"
                        title="${bookManager.localizedActions.get('actionShowComments')}"> <i class="fa fa-comment"></i></button>`;
            }
            row.innerHTML = `<td>${book.id}</td>
                    <td>${book.name}</td>
                    <td>${book.authorName === null ? '' : book.authorName}</td>
                    <td>${book.genreName === null ? '' : book.genreName}</td>
                    <td class="text-end">${btnCell}</td>`;
            if (bookManager.isBookModifier) {
                let buttons = jQuery(row.cells[4]).children()
                jQuery(buttons[0]).on('click', bookManager.callEditBook);
                jQuery(buttons[1]).on('click', bookManager.callShowComments);
                jQuery(buttons[2]).on('click', bookManager.callDeleteBook);
            } else {
                jQuery(row.cells[4]).children().eq(0).on('click', bookManager.callShowComments);
            }
        } else {
            row.cells[1].innerText = book.name;
            row.cells[2].innerText = book.authorName === null ? '' : book.authorName;
            row.cells[3].innerText = book.genreName === null ? '' : book.genreName;
        }
        return row;
    }

    showCountBook() {
        const couRows = bookManager.tbody.querySelectorAll('tr.main-row').length;
        bookManager.totalEl.innerHTML = couRows.toString();
    }

    callShowComments() {
        let btn = jQuery(this);
        let tag = btn.attr('tag');
        let row = btn.parent().parent();
        let id = row.attr('id').replace('row_', '');
        if (tag === 'closed') {
            btn.off('click');
            manager.getJson(bookManager.bookApi + '/' + id + '/comment')
                .then(data => {
                    manager.applyApiExchangeResult(data, (comment) => bookManager.showComments(id, comment));
                });
        } else {
            bookManager.hideComments(id);
        }
    }

    showComments(bookId, comments) {
        const bookRow = $('#row_' + bookId);
        const commentText = localizedMessages.has('comment.text')
            ? localizedMessages.get('comment.text') : 'Comment text';
        const commentTotal = localizedMessages.has('comment.total')
            ? localizedMessages.get('comment.total') : 'Total comments for book:';
        (bookManager.tbody.insertRow(bookRow[0].rowIndex))
            .setAttribute('id', 'row_comments_' + bookId);
        const commentsRow = $('#row_comments_' + bookId);
        let addBtnText = '';
        if (bookManager.isCommentModifier) {
            addBtnText = `<button class="btn btn-outline-secondary btn-sm text-nowrap">
                            <i class="fa fa-add"></i>
                            <span>${bookManager.localizedActions.get('actionNewComment')}</span></button>`;
        }
        commentsRow.html(`<td colspan=5><div class="row comment-row"><div class="col border rounded ms-5 me-1">
            <table class="table table-hover w-100"><thead>
                <tr><th>Id</th><th>${commentText}</th>
                    <th class="text-end">${addBtnText}</th>
                </tr></thead><tbody></tbody>
                <tfoot><tr>
                    <td colspan="2" class="fst-italic">${commentTotal}</td>
                    <th class="text-end">???</th>
                </tr></tfoot>
            </table>
        </div></div></td>`);
        if (bookManager.isCommentModifier) {
            const addBtn = jQuery(commentsRow[0].querySelector('table thead tr :nth-child(3) button'));
            addBtn.on('click', _ => bookManager.callAddComment(bookId));
        }

        const btn = jQuery(bookRow[0].querySelector('.show-comment'));
        btn.attr('tag', 'showed');
        btn.attr('title', bookManager.localizedActions.get('actionHideComments'));
        btn.on('click', bookManager.callShowComments);
        for (const comment of comments) {
            bookManager.showComment(bookId, comment);
        }
        bookManager.showCouComments(bookId);
    }

    hideComments(bookId) {
        const commentsRow = $('#row_comments_' + bookId);
        if (commentsRow.length > 0) {
            commentsRow[0].remove();
        }
        const bookRow = $('#row_' + bookId);
        const btn = jQuery(bookRow[0].querySelector('.show-comment'));
        btn.attr('tag', 'closed');
        btn.attr('title', bookManager.localizedActions.get('actionShowComments'));
    }

    showComment(bookId, comment) {
        const commentsRow = $('#row_comments_' + bookId);
        const commentsBody = jQuery(commentsRow[0].querySelector('td div table tbody'));
        let row = $('#commentRow_' + comment.id);
        if (row.length === 0) {
            (commentsBody[0].insertRow())
                .setAttribute('id', 'commentRow_' + comment.id);
            row = $('#commentRow_' + comment.id);
            row.attr('book', bookId);
            let btnModifyText = '';
            if (bookManager.isCommentModifier) {
                btnModifyText = `<button class="btn btn-outline-secondary btn-sm" 
                            title="${bookManager.localizedActions.get('actionEditComment')}">
                            <i class="fa fa-edit"></i></button>
                        <button class="btn btn-outline-secondary btn-sm"
                             title="${bookManager.localizedActions.get('actionDeleteComment')}">
                             <i class="fa fa-remove"></i></button>`;
            }
            row.html(`<td></td><td></td>
                    <td class="text-end">${btnModifyText}</td>`);
            if (bookManager.isCommentModifier) {
                const btnEdit = jQuery(row[0].querySelector(':nth-child(3)').querySelector(':nth-child(1)'));
                const btnDel = jQuery(row[0].querySelector(':nth-child(3)').querySelector(':nth-child(2)'));
                btnEdit.on('click', _ => bookManager.callEditComment(bookId, comment.id));
                btnDel.on('click', _ => bookManager.callDeleteComment(bookId, comment.id));
            }
        }
        const commentRow = row[0];
        commentRow.cells[0].innerText = comment.id;
        commentRow.cells[1].innerText = comment.text;
    }

    showCouComments(bookId) {
        const commentsRow = $('#row_comments_' + bookId);
        const commentsBody = commentsRow[0].querySelector('td div table tbody');
        const couRows = commentsBody.querySelectorAll('tr').length;
        const totalEl = jQuery(commentsRow[0].querySelector('td div table tfoot tr :nth-child(2)'));
        totalEl.html(couRows.toString());
    }

    callAddBook() {
        bookManager.showEditBookDialog().then();
    }

    callEditBook() {
        let row = jQuery(this).parent().parent();
        let id = row.attr('id').replace('row_', '');
        bookManager.showEditBookDialog(id).then();
    }

    async showEditBookDialog(bookId = null) {
        if (bookId == null) {
            $('#editBookId').val('');
            $('#editBookAction').val('add');
            $('#editBookTitle').text(
                localizedMessages.has('book.add-title')
                    ? localizedMessages.get('book.add-title') : 'New book');
            $('#nameBook').val('');
            $('#authorName').val('');
            $('#genreName').val('');
        } else {
            $('#editBookId').val(bookId);
            $('#editBookAction').val('edit');
            $('#editBookTitle').text(
                localizedMessages.has('book.edit-title')
                    ? localizedMessages.get('book.edit-title') : 'Edit book');
            const bookRow = $('#row_' + bookId)[0];
            $('#nameBook').val(bookRow.cells[1].innerText);
            $('#authorName').val(bookRow.cells[2].innerText);
            $('#genreName').val(bookRow.cells[3].innerText);
        }
        let canContinue = true;
        bookManager.clearBookNameError();
        $('#errorContainerModalBook').html('');
        await manager.getJson(bookManager.authorApi)
            .then(data => {
                manager.applyApiExchangeResult(data, (authors) => {
                    const authorList = $('#authorList');
                    authorList.html('');
                    for (const author of authors) {
                        const option = document.createElement('option');
                        option.value = author.name;
                        authorList[0].append(option);
                    }
                }, (error) => {
                    canContinue = false;
                    manager.showError(error);
                });
            });
        await manager.getJson(bookManager.genreApi)
            .then(data => {
                manager.applyApiExchangeResult(data, (genres) => {
                    const genreList = $('#genreList');
                    genreList.html('');
                    for (const genre of genres) {
                        const option = document.createElement('option');
                        option.value = genre.name;
                        genreList[0].append(option);
                    }
                }, (error) => {
                    canContinue = false;
                    manager.showError(error);
                });
            });
        if (canContinue) {
            bookManager.editBookModal.toggle();
        }
    }

    clearBookNameError() {
        const errBookNameDiv = $('#nameBookError');
        errBookNameDiv.html('');
        errBookNameDiv.addClass('invisible');
    }

    async validateBookName() {
        bookManager.clearBookNameError();
        let ret = false;
        const bookName = $('#nameBook').val();
        const book = {name: bookName}
        await manager.sendJsonData(bookManager.validateApi + '/book',
            JSON.stringify(book)).then(response => {
            manager.applyApiExchangeResult(response, (success) => {
                ret = true;
            }, (error) => {
                if (error.errorMessage !== null && error.errorMessage !== undefined
                    && error.errorMessage.key === 'error.validation'
                    && error.details !== null && error.details !== undefined
                    && error.details.name !== undefined) {
                    const errBookNameDiv = $('#nameBookError');
                    let errMsg = error.details.name;
                    if (localizedMessages.has(errMsg)) {
                        errMsg = localizedMessages.get(errMsg);
                    }
                    errBookNameDiv.html(`<div class="alert alert-danger align-items-center m-0 p-0">${errMsg}</div>`);
                    errBookNameDiv.removeClass('invisible');
                } else {
                    manager.showError(error, 'errorContainerModalBook');
                }
            });
        });
        return ret;
    }

    makeBook() {
        let book = {};
        if ($('#editBookAction').val() === 'edit') {
            book.id = parseInt($('#editBookId').val());
        }
        book.name = $('#nameBook').val();
        let authorName = $('#authorName').val();
        if (authorName !== null && authorName.trim().length > 0) {
            book.authorName = authorName.trim();
        }
        let genreName = $('#genreName').val();
        if (genreName !== null && genreName.trim().length > 0) {
            book.genreName = genreName.trim();
        }
        return book;
    }

    async doSaveBook() {
        if (!await bookManager.validateBookName()) {
            return;
        }
        const book = bookManager.makeBook();
        let url = bookManager.bookApi;
        let method = 'POST';
        if ($('#editBookAction').val() === 'edit') {
            method = 'PUT';
            url = url + '/' + book.id;
        }
        manager.sendJsonData(url, JSON.stringify(book), method).then(response => {
            manager.applyApiExchangeResult(response, (data) => {
                bookManager.editBookModal.toggle();
                bookManager.showBook(data);
                bookManager.showCountBook();
            }, (error) => {
                manager.showError(error, 'errorContainerModalBook');
            });
        });
    }

    callDeleteBook() {
        let row = jQuery(this).parent().parent();
        let id = row.attr('id').replace('row_', '');
        $('#deleteId').val(id);
        $('#deleteBtn').off('click');
        $('#deleteBtn').on('click', bookManager.doDeleteBook);
        $('#deleteQueryTitle').text(bookManager.getDeleteHeader('book'));
        $('#errorContainerDeleteQuery').html('');
        bookManager.deleteModal.toggle();
    }

    getDeleteHeader(mode) {
        let ret = localizedMessages.has('action.confirm-delete')
            ? localizedMessages.get('action.confirm-delete')
            : 'Confirm delete {0}';
        if (ret.includes('{0}')) {
            const subj = localizedMessages.has(mode) ? localizedMessages.get(mode) : mode;
            ret = ret.replace('{0}', subj);
        }
        return ret;
    }

    async doDeleteBook() {
        let id = $('#deleteId').val();
        manager.sendJsonData(bookManager.bookApi + '/' + id,
            null, 'DELETE').then(response => {
            manager.applyApiExchangeResult(response, _ => {
                bookManager.deleteModal.toggle();
                const commentsRow = $('#row_comments_' + id);
                if (commentsRow.length > 0) {
                    commentsRow[0].remove();
                }
                const bookRow = $('#row_' + id);
                if (bookRow.length > 0) {
                    bookRow[0].remove();
                }
                bookManager.showCountBook();
            }, (error) => {
                manager.showError(error, 'errorContainerDeleteQuery');
            });
        });
    }

    callAddComment(bookId) {
        bookManager.showEditCommentDialog(bookId).then();
    }

    callEditComment(bookId, commentId) {
        bookManager.showEditCommentDialog(bookId, commentId).then();
    }

    async showEditCommentDialog(bookId, commentId = null) {
        const bookRow = $('#row_' + bookId)[0];
        $('#bookForComment').val(bookRow.cells[1].innerText);
        $('#editComment_bookId').val(bookId);
        let titleKey = 'comment.edit-title';
        let titleVal = 'Edit comment';
        if (commentId === null) {
            $('#textComment').val('');
            $('#editComment_id').val('');
            $('#editComment_action').val('add');
            titleKey = 'comment.add-title';
            titleVal = 'New comment';
        } else {
            const commentRow = $('#commentRow_' + commentId)[0];
            $('#textComment').val(commentRow.cells[1].innerText);
            $('#editComment_id').val(commentId);
            $('#editComment_action').val('edit');
        }
        titleVal = (localizedMessages.has(titleKey)
            ? localizedMessages.get(titleKey)
            : titleVal);
        $('#editCommentTitle').text(titleVal);

        bookManager.clearCommentTextError();
        $('#errorContainerModalComment').html('');
        bookManager.editCommentModal.toggle();
    }

    clearCommentTextError() {
        const errCommentTextDiv = $('#textCommentError');
        errCommentTextDiv.html('');
        errCommentTextDiv.addClass('invisible');
    }

    async validateCommentText() {
        bookManager.clearCommentTextError();
        let ret = false;
        const commentText = $('#textComment').val();
        const comment = {text: commentText}
        await manager.sendJsonData(bookManager.validateApi + '/comment',
            JSON.stringify(comment)).then(response => {
            manager.applyApiExchangeResult(response, (success) => {
                ret = true;
            }, (error) => {
                if (error.errorMessage !== null && error.errorMessage !== undefined
                    && error.errorMessage.key === 'error.validation'
                    && error.details !== null && error.details !== undefined
                    && error.details.text !== undefined) {
                    const errCommentTextDiv = $('#textCommentError');
                    let errMsg = error.details.text;
                    if (localizedMessages.has(errMsg)) {
                        errMsg = localizedMessages.get(errMsg);
                    }
                    errCommentTextDiv.html(`<div class="alert alert-danger align-items-center m-0 p-0">${errMsg}</div>`);
                    errCommentTextDiv.removeClass('invisible');
                } else {
                    manager.showError(error, 'errorContainerModalComment');
                }
            });
        });
        return ret;
    }

    async doSaveComment() {
        if (!await bookManager.validateCommentText()) {
            return;
        }
        const bookId = $('#editComment_bookId').val();
        const comment = bookManager.makeComment();
        let url = bookManager.bookApi + '/' + bookId + '/comment';
        let method = 'POST';
        if ($('#editComment_action').val() === 'edit') {
            method = 'PUT';
            url = bookManager.commentApi + '/' + comment.id;
        }
        manager.sendJsonData(url, JSON.stringify(comment), method).then(response => {
            manager.applyApiExchangeResult(response, (data) => {
                bookManager.editCommentModal.toggle();
                bookManager.showComment(bookId, data);
                bookManager.showCouComments(bookId);
            }, (error) => {
                manager.showError(error, 'errorContainerModalComment');
            });
        });
    }

    makeComment() {
        const includeId = ($('#editComment_action').val() === 'edit');
        let ret = {};
        if (includeId) {
            ret.id = $('#editComment_id').val();
        }
        ret.text = $('#textComment').val();
        return ret;
    }

    callDeleteComment(bookId, commentId) {
        $('#deleteId').val(bookId);
        $('#deleteDopId').val(commentId);
        $('#deleteBtn').off('click');
        $('#deleteBtn').on('click', bookManager.doDeleteComment);
        $('#deleteQueryTitle').text(bookManager.getDeleteHeader('comment'));
        $('#errorContainerDeleteQuery').html('');
        bookManager.deleteModal.toggle();
    }

    async doDeleteComment() {
        const bookId = $('#deleteId').val();
        const commentId = $('#deleteDopId').val();
        manager.sendJsonData(bookManager.commentApi + '/' + commentId,
            null, 'DELETE').then(response => {
            manager.applyApiExchangeResult(response, _ => {
                bookManager.deleteModal.toggle();
                const commentRow = $('#commentRow_' + commentId);
                if (commentRow.length > 0) {
                    commentRow[0].remove();
                }
                bookManager.showCouComments(bookId);
            }, (error) => {
                manager.showError(error, 'errorContainerDeleteQuery');
            });
        });
    }
}