package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistAuthorException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.NoExistGenreException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.AuthorRepository;
import ru.otus.andrk.repository.BookRepository;
import ru.otus.andrk.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;

    private final AuthorRepository authorRepo;

    private final GenreRepository genreRepo;

    private final DtoMapper mapper;


    @Override
    public List<BookDto> getAllBooks() {
        try {
            return bookRepo.findAll().stream().map(mapper::toDto).toList();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    //@Transactional(readOnly = true)
    public BookDto getBookById(long id) {
        try {
            return  bookRepo.findById(id).map(mapper::toDto).orElse(null);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }


    @Override
    @Transactional
    public BookDto addBook(String bookName, Long authorId, Long genreId) {
        return saveBook(0, bookName, authorId, genreId);
    }

    @Override
    @Transactional
    public BookDto modifyBook(long bookId, String newName, Long newAuthorId, Long newGenreId) {
        return saveBook(bookId, newName, newAuthorId, newGenreId);
    }

    @Override
    @Transactional
    public void deleteBook(long id) {
        try {
            var existBook = bookRepo.findById(id);
            existBook.ifPresent(bookRepo::delete);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    private BookDto saveBook(long oldBookId, String newName, Long newAuthorId, Long newGenreId) {
        try {
            Book book = (oldBookId == 0L) ? new Book() :
                    bookRepo.findById(oldBookId).orElseThrow(NoExistBookException::new);
            Author newAuthor = newAuthorId == null ? null :
                    authorRepo.findById(newAuthorId).orElseThrow(NoExistAuthorException::new);
            Genre newGenre = newGenreId == null ? null :
                    genreRepo.findById(newGenreId).orElseThrow(NoExistGenreException::new);
            book.setName(newName);
            book.setAuthor(newAuthor);
            book.setGenre(newGenre);
            var savedBook = Optional.of(bookRepo.save(book));
            return savedBook.map(mapper::toDto)
                    .orElse(null);
        } catch (KnownLibraryManipulationException e) {
            log.error(e);
            throw e;
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }
}
