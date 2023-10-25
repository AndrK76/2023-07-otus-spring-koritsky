package ru.otus.andrk.controller.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.controller.error.ApiExceptionHandler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.service.data.AuthorService;
import ru.otus.andrk.service.data.BookService;
import ru.otus.andrk.service.data.GenreService;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = BookController.class)
@ContextConfiguration(classes = {BookController.class})
public class BookControllerTest {

    @Autowired
    private BookController controller;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @SpyBean
    private ApiExceptionHandler apiExceptionHandler;
    @MockBean
    private ApiErrorMapper errorMapper;


    @Test
    public void shouldReturnCorrectBookList() throws Exception {
        var bookList = getBookDtoList();
        given(bookService.getAllBooks()).willReturn(bookList);

        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(bookList)));
    }

    @Test
    public void shouldUseApiExceptionHandlerWhenRaiseError() throws Exception {
        var err = new OtherLibraryManipulationException(new RuntimeException("test"));
        var dto = getApiErrorDto();
        given(bookService.getAllBooks()).willThrow(err);
        given(errorMapper.fromOtherError(any())).willReturn(dto);

        mvc.perform(get("/api/v1/book"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(dto)));

        verify(apiExceptionHandler, times(1)).otherLibErr(err);
        verify(apiExceptionHandler, times(0)).knownLibErr(any());
    }

    @Test
    public void shouldReturnCorrectBookById() throws Exception {
        var book = getBookDtoList().get(0);
        given(bookService.getBookById(anyLong())).willReturn(book);

        mvc.perform(get("/api/v1/book/55"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));

        verify(bookService, times(1)).getBookById(55L);
        verify(bookService, times(1)).getBookById(anyLong());
    }

    @Test
    public void shouldReturnCorrectBookWithComments() throws Exception {
        var book = getDtoWithComments();
        given(bookService.getBookWithCommentsById(anyLong())).willReturn(book);

        mvc.perform(get("/api/v1/book/99/comments"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(book)));

        verify(bookService, times(1)).getBookWithCommentsById(99L);
        verify(bookService, times(1)).getBookWithCommentsById(anyLong());
    }

    @Test
    public void shouldAddBookAndReturnIt() throws Exception {
        var srcBook = getBookDtoList().get(0);
        var resBook = getBookDtoList().get(1);
        given(bookService.addBook(any())).willReturn(resBook);

        mvc.perform(post("/api/v1/book")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(srcBook)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(resBook)));

        verify(bookService, times(1)).addBook(srcBook);
        verify(bookService, times(0)).addBook(resBook);
        verify(bookService, times(1)).addBook(any());
    }

    @Test
    public void shouldDontAddInvalidBook() throws Exception {
        var book = getDtoWithComments();
        book.setName("");
        given(bookService.addBook(any())).willAnswer(a -> a.getArgument(0));

        mvc.perform(post("/api/v1/book")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
        verify(bookService, times(0)).addBook(any());
    }

    @Test
    public void shouldModifyGivenBookAndReturnIt() throws Exception {
        var srcBook = getBookDtoList().get(0);
        var srcId = 99L;
        assert srcBook.getId() != srcId;
        var resBook = getBookDtoList().get(1);
        given(bookService.modifyBook(anyLong(), any())).willReturn(resBook);

        mvc.perform(put("/api/v1/book/" + srcId)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(srcBook)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(resBook)));

        verify(bookService, times(1)).modifyBook(srcId, srcBook);
    }

    @Test
    public void shouldDontModifyInvalidBook() throws Exception {
        var book = getDtoWithComments();
        var id = 99L;
        book.setName("");
        given(bookService.modifyBook(anyLong(), any())).willAnswer(a -> a.getArgument(1));

        mvc.perform(put("/api/v1/book/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
        verify(bookService, times(0)).modifyBook(anyLong(), any());
    }

    @Test
    public void shouldDeleteBook() throws Exception {
        long bookId = 77L;
        mvc.perform(delete("/api/v1/book/" + bookId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        verify(bookService, times(1)).deleteBook(bookId);
    }

    @Test
    public void shouldReturnCorrectAuthorList() throws Exception {
        var authorList = getAuthors();
        given(authorService.getAllAuthors()).willReturn(authorList);

        mvc.perform(get("/api/v1/author"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(authorList)));
    }

    @Test
    public void shouldReturnCorrectGenreList() throws Exception {
        var genreList = getGenres();
        given(genreService.getAllGenres()).willReturn(genreList);

        mvc.perform(get("/api/v1/genre"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(genreList)));
    }


    private List<BookDto> getBookDtoList() {
        return List.of(
                BookDto.builder().id(1L)
                        .name("~NAME~1~")
                        .authorId(1L).authorName("~AUTHOR~1")
                        .build(),
                BookDto.builder().id(2L)
                        .name("~NAME~2~")
                        .authorId(1L).authorName("~AUTHOR~1")
                        .genreId(1L).genreName("~GENRE~1")
                        .build());
    }

    private ApiErrorDto getApiErrorDto() {
        return new ApiErrorDto(new Date(), 400);
    }

    private BookWithCommentsDto getDtoWithComments() {
        return BookWithCommentsDto.builder()
                .id(1L)
                .name("~NAME~1~")
                .authorName("~AUTHOR~1")
                .comments(List.of(
                        new CommentDto(6L, "~COMMENT~")
                ))
                .build();
    }

    private List<AuthorDto> getAuthors(){
        return List.of(
                new AuthorDto(1L, "Author 1"),
                new AuthorDto(17L, "Author 71")
        );
    }

    private List<GenreDto> getGenres(){
        return List.of(
                new GenreDto(6L, "Genre 2"),
                new GenreDto(26L, "Author 62")
        );
    }

}