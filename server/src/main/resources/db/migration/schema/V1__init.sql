create table order_status
(
    id              serial,
    order_id        text,
    int_id          text,
    ext_id          text,
    venue_account   text,
    serial          integer,
    closed          boolean not null default false,
    payload         bytea
);

create index by_order_id on order_status (order_id, serial);
create index by_int_id on order_status (venue_account, int_id);
create index by_ext_id on order_status (venue_account, ext_id);
