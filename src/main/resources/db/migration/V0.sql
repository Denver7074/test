create table if not exists text_entity
(
    id          bigserial primary key,
    text        varchar,
    create_date timestamp
);

create table if not exists result
(
    id                 bigserial primary key,
    text_id            bigint,
    key_column         varchar(2),
    value_column       numeric
)