DROP SEQUENCE customerseq;
DROP SEQUENCE addressseq;
DROP SEQUENCE creditcardseq;
DROP SEQUENCE bankseq;

DROP TABLE creditcard CASCADE CONSTRAINTS;
DROP TABLE customer CASCADE CONSTRAINTS;
DROP TABLE address CASCADE CONSTRAINTS;
DROP TABLE bank CASCADE CONSTRAINTS;
DROP TABLE bank_customer CASCADE CONSTRAINTS;

CREATE TABLE address (
  id       NUMBER(32) PRIMARY KEY,
  postcode NUMBER(5),
  street   VARCHAR2(100),
  city     VARCHAR2(100)
);

CREATE SEQUENCE addressseq;

CREATE TABLE customer (
  id              NUMBER(32) PRIMARY KEY,
  name            VARCHAR2(50),
  surname         VARCHAR2(50),
  home_address_id NUMBER(32) REFERENCES address (id)
);

CREATE SEQUENCE customerseq;

CREATE TABLE cardissuer (
  id              NUMBER(32) PRIMARY KEY,
  name            VARCHAR2(50)
);

CREATE SEQUENCE cardissuerseq;

CREATE TABLE card (
  id          NUMBER(32) PRIMARY KEY,
  ccnumber    VARCHAR2(16) UNIQUE,
  cardtype        VARCHAR2(6),
  customer_id NUMBER(32) REFERENCES customer (id),
  cardissuer_id NUMBER(32) REFERENCES cardissuer (id)
);

CREATE SEQUENCE creditcardseq;

CREATE TABLE bank (
  id   NUMBER(32) PRIMARY KEY,
  name VARCHAR2(200) UNIQUE
);

CREATE SEQUENCE bankseq;

CREATE TABLE bank_customer (
  bank_id     NUMBER(32) REFERENCES bank (id),
  customer_id NUMBER(32) REFERENCES customer (id),
  CONSTRAINT BANK_CUSTOMER_PK PRIMARY KEY (bank_id, customer_id)
);

