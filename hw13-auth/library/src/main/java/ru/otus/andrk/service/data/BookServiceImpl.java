package ru.otus.andrk.service.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.mapper.DtoMapper;
import ru.otus.andrk.exception.KnownLibraryManipulationException;
import ru.otus.andrk.exception.NoExistBookException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.model.Author;
import ru.otus.andrk.model.Book;
import ru.otus.andrk.model.Genre;
import ru.otus.andrk.repository.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepo;

    private final AuthorService authorService;

    private final GenreService genreService;

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
    public Optional<Book> getBookById(long id) {
        try {
            return bookRepo.findById(id);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole(@roleService.getRolesForAction('modify_book'))")
    public BookDto addBook(BookDto book) {
        return saveBook(0, book);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole(@roleService.getRolesForAction('modify_book'))")
    public BookDto modifyBook(long bookId, BookDto book) {
        return saveBook(bookId, book);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyRole(@roleService.getRolesForAction('modify_book'))")
    public void deleteBook(long id) {
        try {
            bookRepo.deleteById(id);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    private BookDto saveBook(long oldBookId, BookDto dto) {
        try {
            Book book = (oldBookId == 0L) ? new Book(dto.getName()) :
                    bookRepo.findById(oldBookId).orElseThrow(NoExistBookException::new);

            Author newAuthor = dto.getAuthorName() == null ? null :
                    authorService.getAuthorByName(dto.getAuthorName())
                            .orElseGet(() -> authorService.addAuthor(dto.getAuthorName()));
            Genre newGenre = dto.getGenreName() == null ? null :
                    genreService.getGenreByName(dto.getGenreName())
                            .orElseGet(() -> genreService.addGenre(dto.getGenreName()));
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
