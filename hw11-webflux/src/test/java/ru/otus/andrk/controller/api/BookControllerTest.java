package ru.otus.andrk.controller.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.test.StepVerifier;
import ru.otus.andrk.config.ControllerConfig;
import ru.otus.andrk.config.ThreadConfig;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.mapper.ApiErrorMapper;
import ru.otus.andrk.exception.OtherLibraryManipulationException;
import ru.otus.andrk.service.data.BookService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebFluxTest({BookController.class, RestAdvice.class})
@Import({ControllerConfig.class, ThreadConfig.class})
@ContextConfiguration(classes = {BookController.class, RestAdvice.class})
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private ControllerConfig config;

    @Autowired
    private Scheduler scheduler;

    @MockBean
    private ApiErrorMapper apiErrorMapper;

    @Autowired
    private WebTestClient testClient;

    @ParameterizedTest
    @ValueSource(strings = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_NDJSON_VALUE})
    void shouldReturnExceptedListOfBookAsRequestedType_whenGetAllBook(String acceptMediaType) {
        var testPublisher = new AppTestPublisher<BookDto>();
        var expectedData = testPublisher.getTwoBookSample();
        given(bookService.getAllBooks())
                .willReturn(testPublisher.getValidFlux(expectedData));

        var actualResult = testClient
                .get().uri("/api/v1/book")
                .accept(MediaType.valueOf(acceptMediaType))
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(acceptMediaType)
                .expectBodyList(BookDto.class)
                .hasSize(expectedData.size())
                .returnResult()
                .getResponseBody();
        assertThat(actualResult).containsExactlyElementsOf(expectedData);
    }



    @Test
    void shouldReturnApplyErrorAdviceAndReturnApiErrorDto_whenGetAllBook() {
        var testPublisher = new AppTestPublisher<BookDto>();
        var expectedData = testPublisher.getTenBookSample();

        var exceptedError = new OtherLibraryManipulationException(new RuntimeException());
        var exceptedErrorDto = testPublisher.mapError(exceptedError);


        given(bookService.getAllBooks())
                .willReturn(testPublisher.getFluxWithError(expectedData, exceptedError, 1));
        given(apiErrorMapper.fromOtherError(any())).willReturn(Mono.just(exceptedErrorDto));

        var actualResult = testClient
                .get().uri("/api/v1/book")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange()
                .expectStatus().isEqualTo(exceptedErrorDto.getStatus())
                .expectBody(ApiErrorDto.class).returnResult().getResponseBody();

        assertThat(actualResult)
                .returns(exceptedError.getClass().getName(), ApiErrorDto::getDetails)
                .returns(exceptedErrorDto.getTimestamp(), ApiErrorDto::getTimestamp);
        verify(bookService, times(1)).getAllBooks();
        verify(apiErrorMapper, times(1)).fromOtherError(exceptedError);
    }

    @Test
    void shouldReturnFirstValuesAndAfterRaiseError_whenGetAllBook() {
        var testPublisher = new AppTestPublisher<BookDto>();
        var expectedData = testPublisher.getTenBookSample();
        var exceptedError = new OtherLibraryManipulationException(new RuntimeException());
        var exceptedErrorDto = testPublisher.mapError(exceptedError);
        given(bookService.getAllBooks())
                .willReturn(testPublisher.getFluxWithError(expectedData, exceptedError, 8));
        given(apiErrorMapper.fromOtherError(any())).willReturn(Mono.just(exceptedErrorDto));

        var response = testClient
                .get().uri("/api/v1/book")
                .accept(MediaType.APPLICATION_NDJSON)
                .exchange();

        var actualBookResult = response
                .expectStatus().isOk()
                .returnResult(BookDto.class)
                .getResponseBody();

        StepVerifier
                .create(actualBookResult)
                .expectNext(expectedData.get(0))
                .expectNext(expectedData.get(1))
                .expectNextCount(5)
                .expectErrorMatches(throwable -> throwable instanceof OtherLibraryManipulationException)
                .verify();

        verify(bookService, times(1)).getAllBooks();
        verify(apiErrorMapper, times(0)).fromOtherError(exceptedError);
    }


}