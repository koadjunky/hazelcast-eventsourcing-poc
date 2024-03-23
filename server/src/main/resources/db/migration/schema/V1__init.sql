create table order_status
(
    id              serial,
    order_id        text,
    int_id          text,
    ext_id          text,
    venue_account   text,
    closed          boolean not null default false,
    payload         bytea
)
