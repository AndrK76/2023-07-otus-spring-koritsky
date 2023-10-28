package ru.otus.andrk.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.controller.error.ApiExceptionHandler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.CommentOnBookDto;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.service.data.CommentService;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(value = CommentController.class)
@ContextConfiguration(classes = {CommentController.class})
class CommentControllerTest {

    @Autowired
    private CommentController commentController;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CommentService commentService;

    @SpyBean
    private ApiExceptionHandler apiExceptionHandler;

    @MockBean
    private ApiErrorMapper errorMapper;

    @Test
    public void shouldReturnCorrectCommentById() throws Exception {
        var comment = getComments().get(0);
        given(commentService.getCommentById(anyLong())).willReturn(comment);

        mvc.perform(get("/api/v1/comment/952"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(comment)));

        verify(commentService, times(1)).getCommentById(952L);
        verify(commentService, times(1)).getCommentById(anyLong());
    }

    @Test
    public void shouldUseApiExceptionHandlerWhenRaiseError() throws Exception {

        var err = new OtherLibraryManipulationException(new RuntimeException("test"));
        var dto = getApiErrorDto();
        given(commentService.getCommentById(anyLong())).willThrow(err);
        given(errorMapper.fromOtherError(any())).willReturn(dto);

        mvc.perform(get("/api/v1/comment/999"))
                .andExpect(status().is(507))
                .andExpect(content().json(mapper.writeValueAsString(dto)));

        verify(apiExceptionHandler, times(1)).otherLibErr(err);
        verify(apiExceptionHandler, times(0)).knownLibErr(any());
    }

    @Test
    public void shouldAddGivenCommentOnlyOnceAndReturnExcept() throws Exception {
        var srcComment = getCommentOnBook();
        var resComment = getComments().get(1);
        given(commentService.addCommentForBook(anyLong(), any())).willReturn(resComment);

        mvc.perform(post("/api/v1/comment")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(srcComment)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(resComment)));

        verify(commentService, times(1)).addCommentForBook(srcComment.getBookId(), srcComment.getText());
        verify(commentService, times(1)).addCommentForBook(anyLong(), any());
    }

    @Test
    public void shouldDontAddInvalidComment() throws Exception {
        var srcComment = getCommentOnBook();
        srcComment.setText("");
        var resComment = getComments().get(1);
        given(commentService.addCommentForBook(anyLong(), any())).willReturn(resComment);

        mvc.perform(post("/api/v1/comment")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(srcComment)))
                .andExpect(status().isBadRequest());
        verify(commentService, times(0)).addCommentForBook(anyLong(), any());
    }

    @Test
    public void shouldModifyOnlyGivenCommentAndReturnExcept() throws Exception {
        var srcComment = getComments().get(0);
        var srcId = 99L;
        assert srcComment.id() != srcId;
        var resComment = getComments().get(1);
        given(commentService.modifyComment(anyLong(), any())).willReturn(resComment);

        mvc.perform(put("/api/v1/comment/" + srcId)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(srcComment)))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(resComment)));

        verify(commentService, times(1)).modifyComment(srcId, srcComment.text());
        verify(commentService, times(1)).modifyComment(anyLong(), any());
    }

    @Test
    public void shouldDontModifyInvalidComment() throws Exception {
        var comment = new CommentDto(5L,"");
        var id = 99L;
        given(commentService.modifyComment(anyLong(), any())).willAnswer(a -> a.getArgument(1));

        mvc.perform(put("/api/v1/comment/" + id)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isBadRequest());
        verify(commentService, times(0)).modifyComment(anyLong(), any());
    }

    @Test
    public void shouldDeleteComment() throws Exception {
        long commentId = 77L;
        mvc.perform(delete("/api/v1/comment/" + commentId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        verify(commentService, times(1)).deleteComment(commentId);
    }


    private List<CommentDto> getComments() {
        return List.of(
                new CommentDto(1L, "Comment 1"),
                new CommentDto(2L, "Comment 2")
        );
    }

    private ApiErrorDto getApiErrorDto() {
        return new ApiErrorDto(new Date(), 507);
    }

    private CommentOnBookDto getCommentOnBook() {
        var ret = new CommentOnBookDto();
        ret.setId(0L);
        ret.setBookId(1L);
        ret.setText("Text");
        return ret;
    }

}