package ru.otus.andrk.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.dto.AuthorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.GenreDto;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.AuthorService;
import ru.otus.andrk.service.BookService;
import ru.otus.andrk.service.GenreService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookController controller;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private ExceptionToStringMapper exceptionMapper;

    @Test
    public void shouldReturnCorrectBookList() throws Exception {
        var listBook = getBookDtoList();
        given(bookService.getAllBooks()).willReturn(listBook);

        var resultActions = mvc.perform(get("/book"));
        resultActions.andExpect(status().isOk());

        var modelAndView = resultActions.andReturn().getModelAndView();
        assert modelAndView != null;
        assertThat(modelAndView.getModel().get("books"))
                .isNotNull()
                .asList()
                .hasSize(2)
                .hasOnlyElementsOfType(BookDto.class)
                .extracting("name").containsAll(
                        listBook.stream().map(BookDto::getName).toList());

        var content = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(content)
                .contains("~NAME~1~")
                .contains("~NAME~2~");
    }


    @Test
    public void shouldReturnCorrectBookList2() throws Exception {
        var listBook = getBookDtoList();
        given(bookService.getAllBooks()).willReturn(listBook);

        mvc.perform(get("/book"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"))
                .andExpect(model().attribute("books", listBook))
                .andExpect(content().string(containsString("~NAME~1~")))
                .andExpect(content().string(containsString("~NAME~2~")))
        ;
    }



    @ParameterizedTest
    @ValueSource(strings = {"/", "/book", "/book/add", "/book/edit?id=1", "/book/delete?id=1"})
    public void shouldReturnOkResultForAllGetUrls(String url) throws Exception {
        given(bookService.getAllBooks()).willReturn(getBookDtoList());
        given(bookService.getBookById(1L)).willReturn(getBookDtoList().get(0));
        mvc.perform(get(url)).andExpect(status().isOk());
    }

    @Test
    public void shouldAddBookAndRedirectToBookList_whenBookIsValid() throws Exception {
        String bookName = "New book name";
        mvc.perform(post("/book/add")
                        .flashAttr("book", BookDto.builder().name(bookName).build()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1)).addBook(bookName, null, null);
    }

    @Test
    public void shouldDontAddBookAndReturnGivenBookInModel_whenBookIsInvalid() throws Exception {
        String bookName = "";
        String authorName = "AUTHOR";

        var resultActions = mvc.perform(post("/book/add")
                .flashAttr("book", BookDto.builder()
                        .name(bookName).authorName(authorName).build()));
        resultActions.andExpect(status().isOk());

        verify(bookService, times(0)).addBook(any(), any(), any());

        var modelAndView = resultActions.andReturn().getModelAndView();
        assert modelAndView != null;

        assertThat(modelAndView.getModel().get("book"))
                .isNotNull()
                .isExactlyInstanceOf(BookDto.class)
                .returns(0L, o -> ((BookDto) o).getId())
                .returns("", o -> ((BookDto) o).getName())
                .returns(authorName, o -> ((BookDto) o).getAuthorName());
    }

    @Test
    public void shouldModifyBookAndRedirectToBookList_whenBookIsValid() throws Exception {
        String bookName = "New book name";
        var book = BookDto.builder().id(1L).name(bookName).build();
        mvc.perform(post("/book/edit")
                        .flashAttr("book", book))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1))
                .modifyBook(eq(1L), eq(bookName), any(), any());
    }

    @Test
    public void shouldDontModifyBookAndReturnGivenBookInModel_whenBookIsInvalid() throws Exception {
        String bookName = "";
        String authorName = "AUTHOR";
        var book = BookDto.builder().id(1L).name(bookName).authorName(authorName).build();

        var resultActions = mvc.perform(post("/book/edit")
                .flashAttr("book", book));
        resultActions.andExpect(status().isOk());

        verify(bookService, times(0)).modifyBook(anyLong(), any(), any(), any());

        var modelAndView = resultActions.andReturn().getModelAndView();
        assert modelAndView != null;

        assertThat(modelAndView.getModel().get("book"))
                .isNotNull()
                .isExactlyInstanceOf(BookDto.class)
                .returns(1L, o -> ((BookDto) o).getId())
                .returns("", o -> ((BookDto) o).getName())
                .returns(authorName, o -> ((BookDto) o).getAuthorName());
    }

    @Test
    public void shouldAddAuthorAndGenreIfThatNotExists() throws Exception {
        var book = getBookDtoList().get(1);
        book.setId(0L);
        given(authorService.getAuthorByName(any())).willReturn(null);
        given(genreService.getGenreByName(any())).willReturn(null);
        given(authorService.addAuthor(any())).willReturn(new AuthorDto(1L, book.getAuthorName()));
        given(genreService.addGenre(any())).willReturn(new GenreDto(1L, book.getGenreName()));

        mvc.perform(post("/book/add")
                        .flashAttr("book", book))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));

        verify(authorService, times(1))
                .addAuthor(eq(book.getAuthorName()));
        verify(genreService, times(1))
                .addGenre(eq(book.getGenreName()));
    }

    @Test
    public void shouldDontAddAuthorAndGenreIfThatExists() throws Exception {
        var book = getBookDtoList().get(1);
        book.setId(0L);
        given(authorService.getAuthorByName(any())).willReturn(new AuthorDto(1L,book.getAuthorName()));
        given(genreService.getGenreByName(any())).willReturn(new GenreDto(1L,book.getGenreName()));

        mvc.perform(post("/book/add")
                        .flashAttr("book", book))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));

        verify(authorService, times(0))
                .addAuthor(any());
        verify(genreService, times(0))
                .addGenre(any());
    }

    @Test
    public void shouldDeleteBookAndRedirectToBookList() throws Exception {
        mvc.perform(post("/book/delete")
                        .param("id","1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book"));
        verify(bookService, times(1))
                .deleteBook(eq(1L));
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
}
