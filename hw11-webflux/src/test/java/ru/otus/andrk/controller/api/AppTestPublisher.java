package ru.otus.andrk.controller.api;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import ru.otus.andrk.dto.ApiErrorDto;
import ru.otus.andrk.dto.BookDto;
import ru.otus.andrk.dto.MessagePair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
public class AppTestPublisher<T> {

    public List<BookDto> getTwoBookSample() {
        return List.of(
                BookDto.builder()
                        .id("book_01").name("book 1 name")
                        .authorId("author_01").authorName("author 1 name")
                        .build(),
                BookDto.builder()
                        .id("book_02").name("book 2 name")
                        .genreId("genre_01").genreName("genre 1 name")
                        .build()
        );
    }

    public List<BookDto> getTenBookSample() {
        List<BookDto> ret = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var book = new BookDto();
            book.setId("q_" + i);
            book.setName("book " + i);
            ret.add(book);
        }
        return ret;
    }



    public Flux<T> getValidFlux(List<T> source) {
        return Flux.fromIterable(source);
    }

    public Flux<T> getFluxWithError(List<T> validSource, Exception raisedException, int exceptionPos) {
        return Flux.create(emitter -> {
            for (int i = 0; i < validSource.size(); i++) {
                if (i != exceptionPos) {
                    log.info("produce book {}",i);
                    emitter.next(validSource.get(i));
                } else {
                    log.info("produce error");
                    emitter.error(raisedException);
                }
            }
        });
    }

    public ApiErrorDto mapError(Exception e){
        var ret = new ApiErrorDto(new Date(), 501);
        ret.setStatusMessage(new MessagePair("status",e.getClass().getSimpleName()));
        ret.setDetails(e.getClass().getName());
        return ret;
    }


}
