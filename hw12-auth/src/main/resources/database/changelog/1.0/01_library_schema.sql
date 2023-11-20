--liquibase formatted sql
--changeset andrk:2023-11-20-001-create_library_schema

create table books
(
    id IDENTITY primary key,
    name      varchar(255),
    author_id long,
    genre_id  long
);

create table genres
(
    id IDENTITY primary key,
    name varchar(255)
);

create table authors
(
    id IDENTITY primary key,
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
    id IDENTITY primary key,
    text    varchar(2000),
    book_id long not null
);

alter table comments
    add constraint comment_book_fk
        foreign key (book_id) references books (id);