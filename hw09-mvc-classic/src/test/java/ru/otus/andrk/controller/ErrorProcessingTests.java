package ru.otus.andrk.controller;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.AuthorService;
import ru.otus.andrk.service.BookService;
import ru.otus.andrk.service.CommentService;
import ru.otus.andrk.service.GenreService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class ErrorProcessingTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private BookController bookController;

    @Autowired
    private CommentController commentController;

    @Autowired
    private AppErrorController errorController;

    @MockBean
    private BookService bookService;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private GenreService genreService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ExceptionToStringMapper exceptionMapper;


    @Disabled
    @Test
    public void shouldReturnPredefinedErrorPageAndNotFoundStatusWhenRequestedNotExistPage() throws Exception {
        var resultActions = mvc.perform(get("/no_exist_page"));
        var result = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(result).contains("class=\"btn btn-sm btn-outline-secondary\"");

        //@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
        //@ComponentScan(basePackages = {"ru.otus.andrk.controller", "ru.otus.andrk.exception", "ru.otus.andrk.dto"})
        //@TestPropertySource(properties = {"spring.sql.init.mode=never"})
        //@AutoConfigureMockMvc
    }

    @Test
    public void shouldReturnPredefinedErrorPageAndNotFoundStatusWhenRequestedNotExistPage2() throws Exception {
        mvc.perform(get("/error")
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Error 404: Not Found")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/book/edit?id=5555", "/book/144/comment", "/comment/edit?id=155",
            "/comment/delete?id=1000", "/book/255/comment/add"})
    public void shouldReturnPredefinedErrorPageAndBadRequestStatusWhenRequestForNoExistBookOrComment(
            String url    ) throws Exception {
        given(bookService.getBookById(anyLong())).willReturn(null);
        given(commentService.getCommentById(anyLong())).willReturn(null);
        var resultActions = mvc.perform(get("/book/edit?id=5555"));
        resultActions.andExpect(status().isBadRequest());
        var result = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(result).contains("class=\"btn btn-sm btn-outline-secondary\"");
    }
}
