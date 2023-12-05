class BookManager {
    apiSettings;
    bookApi = '/api/v1/book';
    authorApi = '/api/v1/author';
    genreApi = '/api/v1/genre';
    validateApi = '/api/v1/validation'

    localizedActions = new Map();
    isBookModifier = false;
    isCommentModifier = false;


    tbody = document.querySelector('#bookTable tbody');
    totalEl = document.getElementById('booksTotal');
    editBookModal = {}


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
        await manager.getJson(bookManager.apiSettings.url + '/api/v1/info/access?action=modify_book').then(data => {
            manager.applyApiExchangeResult(data, (res) => {
                bookManager.isBookModifier = res;
            });
        });
    }

    async setCommentModifier() {
        await manager.getJson(bookManager.apiSettings.url + '/api/v1/info/access?action=modify_comment').then(data => {
            manager.applyApiExchangeResult(data, (res) => {
                bookManager.isCommentModifier = res;
            });
        });
    }

    initPage() {
        bookManager.clearPage('???');
        $('#btnAdd').on('click', bookManager.callAddBook);
        bookManager.editBookModal = new bootstrap.Modal(document.getElementById('editBookModal'));
        $('#nameBook').on('blur', bookManager.validateBookName);
    }

    clearPage(couBook) {
        bookManager.tbody.innerHTML = '';
        bookManager.totalEl.innerHTML = couBook;
    }

    getBookList() {
        $('#btnAdd').addClass('invisible');
        manager.getJson(bookManager.apiSettings.url + bookManager.bookApi).then(data => {
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
        const couRows = bookManager.tbody.querySelectorAll('tr').length -
            bookManager.tbody.querySelectorAll('tr.comment-row').length;
        bookManager.totalEl.innerHTML = couRows.toString();
    }

    callShowComments() {
        let btn = jQuery(this);
        let tag = btn.attr('tag');
        let row = btn.parent().parent();
        let id = row.attr('id').replace('row_', '');
        if (tag === 'closed') {
            btn.off('click');
            manager.getJson(bookManager.apiSettings.url + bookManager.bookApi + '/' + id + '/comment')
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
        await manager.getJson(bookManager.apiSettings.url + bookManager.authorApi)
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
        await manager.getJson(bookManager.apiSettings.url + bookManager.genreApi)
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
        await manager.sendJsonData(bookManager.apiSettings.url + bookManager.validateApi + '/book', book)
            .then(response => {
                manager.applyApiExchangeResult(response, (success) => {
                    ret = true;
                }, (error) => {
                    const errBookNameDiv = $('#nameBookError');
                    errBookNameDiv.text(JSON.stringify(error));
                    errBookNameDiv.removeClass('invisible');
                });
            });
        return ret;
    }


    callDeleteBook() {
        console.log('Delete')
        let row = jQuery(this).parent().parent();
        let id = row.attr('id').replace('row_', '');
        console.log(id);
    }

    callAddComment(bookId) {
        console.log('Add comment for ' + bookId);
    }

    callEditComment(bookId, commentId) {
        console.log('Edit comment ' + commentId + ' for ' + bookId);
    }

    callDeleteComment(bookId, commentId) {
        console.log('Delete comment ' + commentId + ' for ' + bookId);
    }

}