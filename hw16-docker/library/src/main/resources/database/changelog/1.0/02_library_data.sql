--liquibase formatted sql
--changeset andrk:2024-01-15-002-insert_initial_library_data

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
values ('Евгений Онегин', 1, 2),
       ('The Art of Computer Programming', 2, 1)
;

insert into comments (text, book_id)
values ('Роман Евгений Онегин на мой взгляд должен прочитать каждый! Интересно наблюдать за размышлениями героев! '||
        'Проследить безответную любовь, которую уже не получится вернуть. Обожаю письма от Онегина и Татьяны, '||
        'они наполнены чувствами и пропитаны смыслом, правда для каждого своим! Несмотря на то, что роман написан '||
        'был очень давно, многие мысли актуальны и по сей день. Тем кто не читал-желаю этого, а те кто читал– советую '||
        'ещё раз. Такие произведения никогда не стареют! '||
        'https://www.litres.ru/book/aleksandr-pushkin/evgeniy-onegin-171966/otzivi/', 1),
       ('Аффтар пеши исчо', 1);
