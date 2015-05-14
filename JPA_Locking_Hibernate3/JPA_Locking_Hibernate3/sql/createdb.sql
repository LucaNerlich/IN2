drop sequence customerseq;
drop sequence addressseq;
drop sequence creditcardseq;
drop sequence bankseq;

drop table creditcard;
drop table address;
drop table bank_customer;
drop table customer;
drop table bank;
create table customer (
	id number(32) primary key,
	name varchar2(50),
	surname varchar2(50),
	optlock number(32) default 1 not null
);

create sequence customerseq;

