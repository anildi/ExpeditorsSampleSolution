INSERT INTO BIG_ADOPTER (NAME, PHONE_NUMBER, ADOPT_DATE, PET_TYPE, PET_NAME, PET_BREED)
VALUES ('H2-Joey', '383 9999 9393', '1960-06-09', 'DOG', 'woofie', 'mixed'),
       ('H2-Francine', '383 9339 9999 9393', '2020-05-09', 'DOG', 'slinky', 'dalmation'),
       ('H2-Darlene', '4484 9339 77939', '2020-05-09', 'TURTLE', 'swifty', null),
       ('H2-Miguel', '77 888 93938', '2022-03-09', 'DOG', 'woofwoof', 'Terrier');

INSERT INTO ADOPTER (NAME, PHONE_NUMBER)
VALUES ('H2-Joey', '383 9999 9393'),
       ('H2-Francine', '383 9339 9999 9393'),
       ('H2-Darlene', '4484 9339 77939'),
       ('H2-Miguel', '77 888 93938');


INSERT INTO PET (PET_TYPE, PET_NAME, PET_BREED, ADOPT_DATE, ADOPTER_ID)
VALUES ('DOG', 'woofie', 'mixed', '1959-06-09', 1),
       ('DOG', 'slinky', 'dalmation', '2020-05-09', 1),
       ('DOG', 'woofwoof', 'Terrier', '2020-05-09', 2),
        ('TURTLE', 'swifty', null, '2022-03-09', 3);

