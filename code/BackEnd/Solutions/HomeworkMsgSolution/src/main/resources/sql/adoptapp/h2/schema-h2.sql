-- BigAdopter.  Everything is in one table, so an Adopter can
-- adopt only one pet.
DROP TABLE BIG_ADOPTER IF EXISTS;

CREATE USER IF NOT EXISTS LARKU SALT 'f2d97d5e5c194fe4' HASH 'bf9ac7082b79123183a1a58f3f23b3822cbedc5c1161394f43bd4d0d03237c59' ADMIN;

CREATE MEMORY TABLE BIG_ADOPTER(
                                   ID integer primary key auto_increment not null,
                                   NAME VARCHAR(255),
                                   PHONE_NUMBER VARCHAR(100),
                                   ADOPT_DATE DATE,
                                   PET_TYPE VARCHAR(25),
                                   PET_NAME VARCHAR(50),
                                   PET_BREED VARCHAR(50)
);


-- Adopter.  Tables are split up to allow a one to many
-- relationship.
DROP TABLE PET IF EXISTS;
DROP TABLE ADOPTER IF EXISTS;

CREATE
USER IF NOT EXISTS LARKU SALT 'f2d97d5e5c194fe4' HASH 'bf9ac7082b79123183a1a58f3f23b3822cbedc5c1161394f43bd4d0d03237c59' ADMIN;

CREATE
MEMORY TABLE ADOPTER(
    ID integer primary key auto_increment not null,
    NAME VARCHAR(255),
    PHONE_NUMBER VARCHAR(100)
);

CREATE
MEMORY TABLE PET (
    PET_ID integer primary key auto_increment not null,
    PET_TYPE VARCHAR(25),
    PET_NAME VARCHAR(50),
    PET_BREED VARCHAR(50),
    ADOPT_DATE DATE,

    ADOPTER_ID INTEGER
);


ALTER TABLE PET ADD CONSTRAINT PUBLIC.PET FOREIGN KEY(ADOPTER_ID) REFERENCES ADOPTER(ID);
ALTER TABLE PET ADD CONSTRAINT PUBLIC.PET_TO_ADOPTER UNIQUE(PET_ID, ADOPTER_ID);
