create table books
(
    id        long primary key,
    name      varchar(255),
    author_id long,
    genre_id  long
);

create table genres
(
    id   long primary key,
    name varchar(255)
);

create table authors
(
    id   long primary key,
    name varchar(255)
);

alter table books
    add constraint book_author_fk
        foreign key (author_id) references authors (id);

alter table books
    add constraint book_genre_fk
        foreign key (genre_id) references genres (id);