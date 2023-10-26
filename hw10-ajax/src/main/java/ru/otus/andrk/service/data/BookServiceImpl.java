package ru.otus.andrk.service.data;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
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

    private final AuthorService authorService;

    private final GenreService genreService;

    private final BuBuService buBuService;


    @Override
    public List<BookDto> getAllBooks() {
        try {
            buBuService.tryBuBu();
            return bookRepo.findAll().stream().map(mapper::toDto).toList();
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    @Override
    public BookDto getBookById(long id) {
        Optional<BookDto> book;
        try {
            buBuService.tryBuBu();
            book = bookRepo.findById(id).map(mapper::toDto);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
        return book.orElseThrow(NoExistBookException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public BookWithCommentsDto getBookWithCommentsById(long id) {
        Optional<BookWithCommentsDto> book;
        try {
            buBuService.tryBuBu();
            book = bookRepo.findById(id).map(mapper::toDtoWithComments);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
        return book.orElseThrow(NoExistBookException::new);
    }

    @Override
    @Transactional
    public BookDto addBook(BookDto book) {
        actualizeAuthorAndGenre(book);
        return saveBook(0, book.getName(), book.getAuthorId(), book.getGenreId());
    }

    @Override
    @Transactional
    public BookDto modifyBook(long bookId, BookDto book) {
        actualizeAuthorAndGenre(book);
        return saveBook(book.getId(), book.getName(), book.getAuthorId(), book.getGenreId());
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
            buBuService.tryBuBu();
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

    private void actualizeAuthorAndGenre(BookDto book) {
        try {
            actualizeAuthor(book);
            actualizeGenre(book);
        } catch (Exception e) {
            log.error(e);
            throw new OtherLibraryManipulationException(e);
        }
    }

    private void actualizeAuthor(BookDto book) {
        if (!Strings.isNullOrEmpty(book.getAuthorName())) {
            var author = authorService.getAuthorByName(book.getAuthorName());
            if (author == null) {
                author = authorService.addAuthor(book.getAuthorName());
            }
            book.setAuthorId(author.id());
        } else {
            book.setAuthorId(null);
        }
    }

    private void actualizeGenre(BookDto book) {
        if (!Strings.isNullOrEmpty(book.getGenreName())) {
            var genre = genreService.getGenreByName(book.getGenreName());
            if (genre == null) {
                genre = genreService.addGenre(book.getGenreName());
            }
            book.setGenreId(genre.id());
        } else {
            book.setGenreId(null);
        }
    }

}
