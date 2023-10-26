package ru.otus.andrk.controller.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.andrk.config.ApplicationSettings;
import ru.otus.andrk.controller.error.ApiExceptionHandler;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;
import ru.otus.andrk.exception.LocalizationException;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.service.i18n.MessageService;

import java.util.Date;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = MessageController.class)
@ContextConfiguration(classes = {MessageController.class})
class MessageControllerTest {

    private static final String DEFAULT_LANG = "none";

    @Autowired
    private MessageController messageController;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private MessageService messageService;

    @MockBean
    private ApplicationSettings appSettings;

    @SpyBean
    private ApiExceptionHandler apiExceptionHandler;

    @MockBean
    private ApiErrorMapper errorMapper;

    @BeforeEach
    public void configureDefaultLang() {
        given(appSettings.getDefaultLang()).willReturn(DEFAULT_LANG);
    }

    @ParameterizedTest
    @ValueSource(strings = {"ru", "de", ""})
    public void shouldReturnExceptMessages(String langVal) throws Exception {
        boolean langNull = langVal.equals("");
        String exceptLang = langNull ? DEFAULT_LANG : langVal;
        String url = "/api/v1/message" + (langNull ? "" : "/" + langVal);
        var exceptResult = new java.util.HashMap<>(getMessages());
        exceptResult.keySet().forEach(r -> exceptResult.put(r, exceptResult.get(r) + exceptLang));

        given(messageService.getMessages(anyString())).willReturn(exceptResult);

        mvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(exceptResult)));

        verify(messageService, times(1)).getMessages(exceptLang);
    }

    @Test
    public void shouldUseApiExceptionHandlerWhenRaiseError() throws Exception {
        var err = new LocalizationException(new RuntimeException("test"));
        given(messageService.getMessages(anyString())).willThrow(err);

        var dto = getApiErrorDto();
        given(errorMapper.fromOtherError(any())).willReturn(dto);

        mvc.perform(get("/api/v1/message/ru"))
                .andExpect(status().is(506))
                .andExpect(content().json(mapper.writeValueAsString(dto)));

        verify(apiExceptionHandler, times(1)).otherLibErr(err);
        verify(apiExceptionHandler, times(0)).knownLibErr(any());
    }

    private Map<String, String> getMessages() {
        return Map.of(
                "key1", "Message 1",
                "key2", "Message 2"
        );
    }

    private ApiErrorDto getApiErrorDto() {
        return new ApiErrorDto(new Date(), 506);
    }
}