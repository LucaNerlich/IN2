DROP SEQUENCE customerseq;
DROP TABLE customer;

CREATE TABLE customer (
  id      NUMBER(32) PRIMARY KEY,
  name    VARCHAR2(50),
  surname VARCHAR2(50),
  optlock NUMBER(32) DEFAULT 1 NOT NULL
);

CREATE SEQUENCE customerseq;

