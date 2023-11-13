package ru.otus.andrk.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.BookWithCommentsDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.exception.converter.ExceptionToStringMapper;
import ru.otus.andrk.service.BookService;
import ru.otus.andrk.service.CommentService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
public class CommentControllerTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CommentController controller;

    @MockBean
    private BookService bookService;

    @MockBean
    private CommentService commentService;

    @MockBean
    private ExceptionToStringMapper exceptionMapper;

    @Test
    public void shouldReturnCorrectBookWithCommentList() throws Exception {
        var bookWithComments = getBookWithComments();
        given(bookService.getBookWithCommentsById(anyLong())).willReturn(bookWithComments);

        var resultActions = mvc.perform(get("/book/1/comment"));
        resultActions.andExpect(status().isOk());

        var modelAndView = resultActions.andReturn().getModelAndView();
        assert modelAndView != null;
        assertThat(modelAndView.getModel().get("book"))
                .isNotNull()
                .isExactlyInstanceOf(BookWithCommentsDto.class)
                .returns(bookWithComments.getName(), o -> ((BookWithCommentsDto) o).getName())
                .extracting("comments")
                .asList()
                .hasSize(2)
                .hasOnlyElementsOfType(CommentDto.class)
                .extracting("text").containsAll(
                        bookWithComments.getComments().stream().map(CommentDto::text).toList());

        var content = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(content)
                .contains("Book with comments")
                .contains("~COMMENT~1")
                .contains("~COMMENT~2");
    }

    @ParameterizedTest
    @ValueSource(strings = {"/book/1/comment", "/book/1/comment/add", "/comment/edit?id=1", "/comment/delete?id=1"})
    public void shouldReturnOkResultForAllGetUrls(String url) throws Exception {
        var bookWithComments = getBookWithComments();
        given(bookService.getBookWithCommentsById(anyLong())).willReturn(bookWithComments);
        given(commentService.getCommentById(anyLong())).willReturn(getCommentForBook());
        given(bookService.getBookById(anyLong())).willReturn(getBook());

        mvc.perform(get(url)).andExpect(status().isOk());
    }

    @Test
    public void shouldAddCommentAndRedirectToBookWithComments_whenCommentIsValid() throws Exception {
        var comment = getCommentForBook();
        comment.setId(0L);
        comment.setText("New text for comment");
        mvc.perform(post("/book/1/comment/add")
                        .flashAttr("comment", comment))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/1/comment"));
        verify(commentService, times(1)).addCommentForBook(eq(1L), eq("New text for comment"));
    }

    @Test
    public void shouldDontAddCommentAndReturnGivenCommentInModel_whenCommentIsInvalid() throws Exception {
        var comment = getCommentForBook();
        comment.setId(0L);
        comment.setText("");

        var resultActions = mvc.perform(post("/book/1/comment/add")
                .flashAttr("comment", comment));
        resultActions.andExpect(status().isOk());

        verify(commentService, times(0)).addCommentForBook(anyLong(), any());

        var modelAndView = resultActions.andReturn().getModelAndView();
        assert modelAndView != null;

        assertThat(modelAndView.getModel().get("comment"))
                .isNotNull()
                .isExactlyInstanceOf(CommentOnBookDto.class)
                .returns(0L, o -> ((CommentOnBookDto) o).getId())
                .returns("", o -> ((CommentOnBookDto) o).getText());
    }

    @Test
    public void shouldModifyCommentAndRedirectToToBookWithComments_whenCommentIsValid() throws Exception {
        var comment = getCommentForBook();
        comment.setText("New text for comment");
        mvc.perform(post("/comment/edit")
                        .flashAttr("comment", comment))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/" + comment.getBookId() + "/comment"));
        verify(commentService, times(1))
                .modifyComment(eq(comment.getId()), eq(comment.getText()));
    }

    @Test
    public void shouldDontModifyAndReturnGivenCommentInModel_whenCommentIsInvalid() throws Exception {
        var comment = getCommentForBook();
        comment.setText("");

        var resultActions = mvc.perform(post("/comment/edit")
                .flashAttr("comment", comment));
        resultActions.andExpect(status().isOk());

        verify(commentService, times(0)).modifyComment(anyLong(), any());

        var modelAndView = resultActions.andReturn().getModelAndView();
        assert modelAndView != null;

        assertThat(modelAndView.getModel().get("comment"))
                .isNotNull()
                .isExactlyInstanceOf(CommentOnBookDto.class)
                .returns(comment.getId(), o -> ((CommentOnBookDto) o).getId())
                .returns("", o -> ((CommentOnBookDto) o).getText());
    }

    @Test
    public void shouldDeleteCommentAndRedirectToToBookWithComments() throws Exception {
        var comment = getCommentForBook();
        given(commentService.getCommentById(anyLong())).willReturn(comment);

        mvc.perform(post("/comment/delete")
                        .param("id","1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/book/" + comment.getBookId() + "/comment"));
        verify(commentService, times(1))
                .deleteComment(eq(1L));
    }

    private BookWithCommentsDto getBookWithComments() {
        return BookWithCommentsDto.builder()
                .id(1L)
                .name("Book with comments")
                .comments(
                        List.of(
                                new CommentDto(1L, "~COMMENT~1"),
                                new CommentDto(2L, "~COMMENT~2")
                        ))
                .build();
    }

    private BookDto getBook() {
        var book = getBookWithComments();
        return BookDto.builder().id(book.getId()).name(book.getName()).build();
    }

    private CommentOnBookDto getCommentForBook() {
        var book = getBookWithComments();
        var ret = new CommentOnBookDto(
                BookDto.builder().id(book.getId()).name(book.getName()).build()
        );
        ret.setId(book.getComments().get(0).id());
        ret.setText(book.getComments().get(0).text());
        return ret;
    }

}
