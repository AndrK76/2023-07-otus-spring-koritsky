REVOKE ALL ON database postgres from public;

create user keycloak with encrypted password 'keycloak';
create user librarer with encrypted password 'librarer';

create database sec_db with owner keycloak;
create database lib_db with owner librarer;
