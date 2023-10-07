insert into genres (name)
values ('Не указан'),
       ('Стихи'),
       ('Проза')
;
insert into authors (name)
values ('А.С. Пушкин'),
       ('D.E. Knuth')
;
insert into books (name, author_id, genre_id)
values ('Евгений онегин', 1, 2),
       ('The Art of Computer Programming', 2, 1)
;

insert into comments (text, book_id)
values ('Клёва', 2),
       ('Нудно', 2);
