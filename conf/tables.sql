CREATE TABLE USER_INFO(
ID SERIAL PRIMARY KEY,
EMAIL TEXT NOT NULL,
FIRSTNAME TEXT NOT NULL,
LASTNAME TEXT NOT NULL,
PASSWORD TEXT NOT NULL, 
TYPE INT NOT NULL
);

create table rooms(id SERIAL primary key,no_of_rooms int not null, address text not null, no_of_beds int not null,state text not null, city text not null);

insert into rooms(no_of_rooms,address,no_of_beds,state,city) values(2,'1000 whaley street',2,'SC','columbia');
insert into rooms(no_of_rooms,address,no_of_beds,state,city) values(2,'1001 whaley street',2,'OH','columbus');

