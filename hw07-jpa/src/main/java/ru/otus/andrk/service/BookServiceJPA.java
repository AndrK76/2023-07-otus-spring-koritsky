package ru.otus.andrk.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.excepton.KnownLibraryManipulationException;
import ru.otus.andrk.excepton.NoExistAuthorException;
import ru.otus.andrk.excepton.NoExistBookException;
import ru.otus.andrk.excepton.NoExistGenreException;
import ru.otus.andrk.excepton.OtherLibraryManipulationException;
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
public class BookServiceJPA implements BookService {
    private final BookRepository bookRepo;

    private final AuthorRepository authorRepo;

    private final GenreRepository genreRepo;

    private final ConversionService conversionService;

    @Override
    @Transactional(readOnly = true)
    public List<BookDto> getAllBooks() {
        try {
            return bookRepo.findAll().stream().map(r -> conversionService.convert(r, BookDto.class)).toList();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookDto getBookById(long id) {
        try {
            var book = bookRepo.findById(id);
            return book.map(value -> conversionService.convert(value, BookDto.class)).orElse(null);
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
            return savedBook.map(v -> conversionService.convert(v, BookDto.class))
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
