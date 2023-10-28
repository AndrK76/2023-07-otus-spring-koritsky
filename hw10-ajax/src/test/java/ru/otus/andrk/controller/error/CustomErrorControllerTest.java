package ru.otus.andrk.controller.error;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.MessagePair;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(value = CustomErrorController.class)
@ContextConfiguration(classes = {CustomErrorController.class})
class CustomErrorControllerTest {

    @Autowired
    private CustomErrorController controller;

    @MockBean
    ApiErrorMapper errMapper;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objMapper;


    @Test
    public void shouldReturnJsonWithExceptedErrorDataWhenAcceptedJson() throws Exception {
        var exceptDto = makeDto();
        given(errMapper.fromErrorAttributes(any())).willReturn(exceptDto);

        var mvcRes = mvc.perform(post("/error")
                        .accept(APPLICATION_JSON))
                .andExpect(content().json(objMapper.writeValueAsString(exceptDto)))
                .andReturn();
        var modelAndView = mvcRes.getModelAndView();
        assertThat(modelAndView).isNull();
    }

    @Test
    public void shouldReturnMvcViewWithExceptedErrorDataWhenAcceptedHtml() throws Exception {
        var exceptDto = makeDto();
        given(errMapper.fromErrorAttributes(any())).willReturn(exceptDto);

        var mvcRes = mvc.perform(post("/error")
                        .accept(TEXT_HTML_VALUE))
                .andReturn();
        var modelAndView = mvcRes.getModelAndView();

        assertThat(modelAndView).isNotNull();
        assertThat(modelAndView.getViewName()).isNotNull().isEqualTo("error");
        assertThat(modelAndView.getModel())
                .isNotNull().containsKey("info")
                .extractingByKey("info").isNotNull()
                .extracting(r -> {
                    try {
                        return objMapper.writeValueAsString(r);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).isEqualTo(objMapper.writeValueAsString(exceptDto));
    }

    private ApiErrorDto makeDto() {
        var ret = new ApiErrorDto(new Date(), 500);
        ret.setStatusMessage(new MessagePair("known-error.bubu-error", "BU-BU-BU"));
        return ret;
    }

}