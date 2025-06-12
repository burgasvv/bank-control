
--liquibase formatted sql

--changeset burgasvv:1
begin ;
create table if not exists identity (
    id uuid default gen_random_uuid() unique not null ,
    authority varchar not null ,
    username varchar unique not null ,
    password varchar not null ,
    email varchar unique not null ,
    phone varchar unique not null ,
    passport varchar unique not null ,
    created_at timestamp not null ,
    updated_at timestamp not null
);
commit ;

--changeset burgasvv:2
begin ;
alter table if exists identity add column enabled boolean ;
alter table if exists identity alter column enabled set not null ;
commit ;

--changeset burgasvv:3
begin ;
alter table if exists identity drop column username ;
alter table if exists identity add column name varchar ;
alter table if exists identity alter column name set not null ;
alter table if exists identity add column surname varchar ;
alter table if exists identity alter column surname set not null ;
alter table if exists identity add column patronymic varchar ;
alter table if exists identity alter column patronymic set not null ;
commit ;