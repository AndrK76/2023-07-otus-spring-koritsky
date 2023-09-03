package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.andrk.dao.AuthorDao;
import ru.otus.andrk.dao.BookDao;
import ru.otus.andrk.dao.GenreDao;
import ru.otus.andrk.model.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LibraryServiceDao implements LibraryService {

    private final BookDao bookDao;

    private final GenreDao genreDao;

    private final AuthorDao authorDao;

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }
}
