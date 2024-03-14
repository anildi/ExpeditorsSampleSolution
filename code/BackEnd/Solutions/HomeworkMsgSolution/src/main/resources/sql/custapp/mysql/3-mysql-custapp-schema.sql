-- Mysql Larku Schema

-- DROP DATABASE IF EXISTS custapp;

CREATE DATABASE IF NOT EXISTS adoptapp;

USE adoptapp;

DROP TABLE if exists PhoneNumber;
DROP TABLE if exists Customer;


CREATE TABLE Customer
(
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name        VARCHAR(255) NOT NULL,
    dob         DATE,
    status      VARCHAR(20)
    -- CONSTRAINT customer_pk PRIMARY KEY (id)
);

CREATE TABLE PhoneNumber
(
    id          INT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    phoneNumber VARCHAR(20),
    type      VARCHAR(20),
    customer_id INTEGER,

    -- CONSTRAINT phoneumber_pk PRIMARY KEY (id),
    CONSTRAINT fk_customer FOREIGN KEY(customer_id) REFERENCES Customer(id)
);
