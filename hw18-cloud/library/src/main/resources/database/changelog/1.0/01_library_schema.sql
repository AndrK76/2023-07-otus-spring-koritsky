--liquibase formatted sql
--changeset andrk:2023-11-20-001-create_library_schema

create table books
(
    id BIGSERIAL primary key,
    name      varchar(255),
    author_id bigInt,
    genre_id  bigInt
);

create table genres
(
    id BIGSERIAL primary key,
    name varchar(255)
);

create table authors
(
    id BIGSERIAL primary key,
    name varchar(255)
);

alter table books
    add constraint book_author_fk
        foreign key (author_id) references authors (id);

alter table books
    add constraint book_genre_fk
        foreign key (genre_id) references genres (id);

create table comments
(
    id BIGSERIAL primary key,
    text    varchar(2000),
    book_id bigInt not null
);

alter table comments
    add constraint comment_book_fk
        foreign key (book_id) references books (id);