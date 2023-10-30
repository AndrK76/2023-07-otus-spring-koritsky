package ru.otus.andrk.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class BookWithCommentsDto implements Serializable {
    private String id;

    private String name;

    private String authorName;

    private String genreName;

    private List<CommentDto> comments;
}
