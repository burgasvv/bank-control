
--liquibase formatted sql

--changeset burgasvv:1
begin ;
create table if not exists operation (
    id uuid default gen_random_uuid() unique not null ,
    card_id uuid references card(id) on delete set null on update cascade ,
    operation_type varchar not null ,
    money decimal not null ,
    completed_at timestamp not null
);
commit ;

--changeset burgasvv:2
begin ;
create table if not exists transfer (
    id uuid default gen_random_uuid() unique not null ,
    sender_id uuid references card(id) on delete set null on update cascade ,
    recipient_id uuid references card(id) on delete set null on update cascade ,
    money decimal not null ,
    completed_at timestamp not null
);
commit ;