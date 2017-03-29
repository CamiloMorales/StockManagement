# --- !Ups

CREATE TABLE product (
  id        INTEGER PRIMARY KEY AUTOINCREMENT,
  name      TEXT NOT NULL UNIQUE,
  quantity  INTEGER NOT NULL
);

CREATE TABLE customer (
  id          INTEGER PRIMARY KEY AUTOINCREMENT,
  user_name   TEXT NOT NULL UNIQUE
);

CREATE TABLE trans (
  id            INTEGER PRIMARY KEY AUTOINCREMENT,
  customer_id   INTEGER NOT NULL,
  product_id    INTEGER NOT NULL,
  trans_type    TEXT NOT NULL,
  quantity      INTEGER NOT NULL,
  finished      INTEGER NOT NULL
);

# --- !Downs

DROP TABLE product;
DROP TABLE customer;
DROP TABLE trans;