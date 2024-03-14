-- Mysql Larku Schema

-- DROP DATABASE IF EXISTS adoptapp;

CREATE DATABASE IF NOT EXISTS adoptapp;

USE adoptapp;

DROP TABLE if exists BIG_ADOPTER;

CREATE TABLE BIG_ADOPTER
(
    ID integer primary key auto_increment not null,
    NAME VARCHAR(255),
    PHONE_NUMBER VARCHAR(100),
    ADOPT_DATE DATE,
    PET_TYPE VARCHAR(25),
    PET_NAME VARCHAR(50),
    PET_BREED VARCHAR(50)

);

DROP TABLE if exists PET;
DROP TABLE if exists ADOPTER;

CREATE TABLE ADOPTER
(
    ID integer primary key auto_increment not null,
    NAME VARCHAR(255),
    PHONE_NUMBER VARCHAR(100)
);

CREATE TABLE PET
(
    PET_ID integer primary key auto_increment not null,
    PET_TYPE VARCHAR(25),
    PET_NAME VARCHAR(50),
    PET_BREED VARCHAR(50),
    ADOPT_DATE DATE,

    ADOPTER_ID INTEGER,

    CONSTRAINT FK_ADOPTER FOREIGN KEY(ADOPTER_ID) REFERENCES ADOPTER(ID)
);
