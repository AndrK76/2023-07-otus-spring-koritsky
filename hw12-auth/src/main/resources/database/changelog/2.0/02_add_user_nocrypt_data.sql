--liquibase formatted sql
--changeset andrk:2023-11-20-004-add_nocrypted_users_info
insert into users(name, password)
values ('test', 'test'),
       ('user', 'password');