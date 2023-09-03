insert into genres (id, name)
values (0, 'unknown'),
       (1, 'known');

insert into authors (id, name)
values (0, 'unknown'),
       (1, 'known');

insert into books (id, name, author_id, genre_id)
values (0, 'Book 0', 0, 0);

insert into books (id, name, author_id, genre_id)
values (1, 'Book without author and genre', null, null);

insert into books (id, name, author_id, genre_id)
values (2, 'Book without genre', 1, null);

insert into books (id, name, author_id, genre_id)
values (3, 'Book without author', null, 1);