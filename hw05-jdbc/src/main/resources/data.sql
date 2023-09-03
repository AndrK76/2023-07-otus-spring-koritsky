insert into genres (id, name)
values (0, 'Не указан'),
       (1, 'Стихи'),
       (2, 'Проза')
;
insert into authors (id, name)
values (1, 'А.С. Пушкин'),
       (2, 'D.E. Knuth')
;
insert into books (id, name, author_id, genre_id)
values (1, 'test', null, 2),
       (2, 'Евгений онегин', 1, 1),
       (3, 'The Art of Computer Programming', 2, 0)
;