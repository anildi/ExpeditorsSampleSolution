-- Postgres custapp schema file

SET ROLE larku;

DROP TABLE if exists PHONENUMBER;
DROP TABLE if exists CUSTOMER;

CREATE TABLE CUSTOMER
(
    id          serial primary key not null,
    name        VARCHAR(255) NOT NULL,
    dob         DATE,
    status      VARCHAR(20)
);

CREATE TABLE PHONENUMBER
(
    id          serial primary key not null,
    phoneNumber VARCHAR(20),
    type      VARCHAR(20),
    customer_id INTEGER,

    CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES customer(id)
);