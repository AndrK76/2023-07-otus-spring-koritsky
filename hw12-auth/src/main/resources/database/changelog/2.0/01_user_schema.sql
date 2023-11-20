--liquibase formatted sql
--changeset andrk:2023-11-20-003-create_security_schema

CREATE TABLE users
(
    id        IDENTITY primary key,
    name     VARCHAR(255) NOT NULL,
    password VARCHAR(255),
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uc_users_name UNIQUE (name)
);