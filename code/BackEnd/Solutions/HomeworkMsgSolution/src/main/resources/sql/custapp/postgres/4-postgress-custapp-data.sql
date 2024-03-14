-- Postgres CustApp data

INSERT INTO Customer (NAME, DOB, STATUS) VALUES
('Manoj-Postgres','1956-08-15', 'PRIVILEGED'),
('Ana-Postgres', '1978-03-10', 'NORMAL'),
('Roberta-Postgres','2000-07-15', 'RESTRICTED'),
('Madhu-Postgres','1994-10-07', 'NORMAL');

INSERT INTO PHONENUMBER (PHONENUMBER, TYPE, CUSTOMER_ID) VALUES
('222 333-4444', 'MOBILE', 1),
('222 333-5555', 'WORK', 1),
('222 333-7900', 'HOME', 2),
('383 343-5879', 'HOME', 3),
('00345 598-8279 389', 'SATELLITE', 4),
('383 598-8279', 'MOBILE', 4);

