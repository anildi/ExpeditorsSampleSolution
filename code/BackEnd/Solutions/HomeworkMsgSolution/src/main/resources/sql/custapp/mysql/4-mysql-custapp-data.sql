-- Mysql Data

USE adoptapp;

INSERT INTO Customer (NAME, DOB, STATUS) VALUES
('Manoj-Mysql','1956-08-15', 'PRIVILEGED'),
('Ana-Mysql', '1978-03-10', 'NORMAL'),
('Roberta-Mysql','2000-07-15', 'RESTRICTED'),
('Madhu-Mysql','1994-10-07', 'NORMAL');

INSERT INTO PhoneNumber (PHONENUMBER, TYPE, CUSTOMER_ID) VALUES
('222 333-4444', 'MOBILE', 1),
('222 333-5555', 'WORK', 1),
('222 333-7900', 'HOME', 2),
('383 343-5879', 'HOME', 3),
('00345 598-8279 389', 'SATELLITE', 4),
('383 598-8279', 'MOBILE', 4);
