package ru.otus.andrk.controller.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.CommentDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = ValidationController.class)
@ContextConfiguration(classes = {ValidationController.class})
class ValidationControllerTest {
    @Autowired
    private ValidationController validationController;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;


    @MockBean
    private ApiErrorMapper errorMapper;

    @Test
    public void shouldReturnOkWhenGivenBookIsValid() throws Exception {
        BookDto book = BookDto.builder().name("valid name").build();

        mvc.perform(post("/api/v1/validation/book")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    public void shouldReturnExceptDataWithBadRequestResultWhenGivenBookDontValid() throws Exception {
        BookDto book = BookDto.builder().name("").build();
        var exceptResult = getErrData();
        given(errorMapper.fromNotValidArgument(any())).willReturn(exceptResult);

        mvc.perform(post("/api/v1/validation/book")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(exceptResult)));
    }


    @Test
    public void shouldReturnOkWhenGivenCommentIsValid() throws Exception {
        CommentDto comment = new CommentDto(0L, "valid text");

        mvc.perform(post("/api/v1/validation/comment")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));
    }

    @Test
    public void shouldReturnExceptDataWithBadRequestResultWhenGivenCommentDontValid() throws Exception {
        CommentDto comment = new CommentDto(0L, "");
        var exceptResult = getErrData();
        given(errorMapper.fromNotValidArgument(any())).willReturn(exceptResult);

        mvc.perform(post("/api/v1/validation/comment")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(mapper.writeValueAsString(exceptResult)));
    }


    Map<String, MessagePair> getErrData() {
        return Map.of(
                "key1", new MessagePair("msg1", "message 1"),
                "key2", new MessagePair("msg2", "message 2"));
    }
}