drop sequence customerseq;
drop sequence addressseq;
drop sequence creditcardseq;
drop sequence bankseq;

DROP TABLE creditcard CASCADE CONSTRAINTS;
DROP TABLE customer CASCADE CONSTRAINTS;
DROP TABLE address CASCADE CONSTRAINTS;
DROP TABLE bank CASCADE CONSTRAINTS;
DROP TABLE bank_customer CASCADE CONSTRAINTS;

create table address (
	id number(32) primary key,
	street varchar2(100)
);

create sequence addressseq;

create table customer (
	id number(32) primary key,
	name varchar2(50),
	surname varchar2(50),
	home_address_id number(32) references address(id)
);

create sequence customerseq;

create table creditcard (
	id number(32) primary key,
	ccnumber varchar2(16) unique,
	customer_id number(32) references customer(id)
);

create sequence creditcardseq;

create table bank (
	id number(32) primary key,
	name varchar2(200) unique
);

create sequence bankseq;

create table bank_customer (
	bank_id number(32) references bank(id),
	customer_id number(32) references customer(id),
	constraint BANK_CUSTOMER_PK primary key (bank_id, customer_id) 
);

