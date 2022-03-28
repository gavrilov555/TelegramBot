create sequence hibernate_sequence start 1 increment 1;

create table users (
id int8 not null,
admin boolean,
chat_id int8,
user_name varchar(255),
email varchar(255),
notified boolean,
phone varchar(255),
state_id int4,
primary key (id));