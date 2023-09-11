insert into genres (name)
values ('unknown'),
       ('known');

insert into authors (name)
values ('unknown'),
       ('known');


insert into books (name, author_id, genre_id)
values ('Book 1', 1, 1);

insert into books (name)
values ('Book without author and genre');


insert into books (name, author_id)
values ('Book without genre', 2);

insert into books (name, genre_id)
values ('Book without author', 2);

insert into comments (text, book_id)
values ('Comment 1 for book 1', 1),
       ('Comment 2 for book 1', 1),
       ('Comment 1 for book 2', 2),
       ('Comment 1 for book 3', 3),
       ('Comment 2 for book 3', 3),
       ('Comment 3 for book 3', 3);