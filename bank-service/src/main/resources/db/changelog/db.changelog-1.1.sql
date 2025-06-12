
--liquibase formatted sql

--changeset burgasvv:1
begin ;
create table if not exists card (
    id uuid default gen_random_uuid() unique not null ,
    identity_id uuid references identity(id) on delete set null on update cascade ,
    card_type varchar not null ,
    payment_system varchar not null ,
    number varchar unique not null ,
    valid_till date not null ,
    code bigint not null ,
    money money not null default 0 ,
    created_at timestamp not null ,
    updated_at timestamp not null
);
commit ;