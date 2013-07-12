-- create tables
create table item (
    id              bigint primary key
    ,name           varchar(256) not null
    ,price          decimal not null
    ,description    varchar(4000) not null
    ,category_id    bigint not null
);

create table category (
    id              bigint primary key
    ,name           varchar(256) not null
);

create table purchase (
    id              bigint primary key
    ,first_name     varchar(64) not null
    ,last_name      varchar(64) not null
    ,zip_code       varchar(16) not null
    ,address        varchar(1024) not null
    ,charge_for_delivery decimal not null
);

create table purchase_detail (
    id              bigint primary key
    ,purchase_id    bigint not null
    ,item_id        bigint not null
    ,amount         bigint not null
);

create sequence common_seq;

-- initial data
-- item
insert into item (id, name, price, description, category_id) values (1, 'strawberry', 5.00, 'strawberry ... xxx xxx xxx xxx xxx', 1);
insert into item (id, name, price, description, category_id) values (2, 'blueberry', 4.00, 'blueberry ... yyy yyy yyy yyy yyy', 1);
insert into item (id, name, price, description, category_id) values (3, 'cranberry', 6.00, 'cranberry ... zzz zzz zzz zzz zzz', 1);
insert into item (id, name, price, description, category_id) values (11, 'grapefruit', 3.00, 'grapefruit ... ppp ppp ppp ppp', 2);
insert into item (id, name, price, description, category_id) values (12, 'orange',2.00, 'orange ... qqq qqq qqq qqq', 2);
-- category
insert into category(id, name) values (1, 'berry');
insert into category(id, name) values (2, 'citrus');
