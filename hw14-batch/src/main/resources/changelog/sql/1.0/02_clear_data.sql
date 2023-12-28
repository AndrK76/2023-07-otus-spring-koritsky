--liquibase formatted sql
--changeset andrk:2023-12-23-001-clear_library_data ignore:true

delete from comments;

delete from books;

delete from authors;

delete from genres;

/*
 runAlways:true
 */