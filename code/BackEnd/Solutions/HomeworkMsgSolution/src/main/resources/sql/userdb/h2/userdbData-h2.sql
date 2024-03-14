--User Data

INSERT INTO users (username, password, enabled) values
('manoj','$2a$10$2KtlBG9s4.QckPpFaM13b.3jeC6D/gYcWxFOcCVkpk7k/9Iz03THy', true),
('ana','$2a$10$2KtlBG9s4.QckPpFaM13b.3jeC6D/gYcWxFOcCVkpk7k/9Iz03THy', true),
('roberta','$2a$10$2KtlBG9s4.QckPpFaM13b.3jeC6D/gYcWxFOcCVkpk7k/9Iz03THy', true),
('madhu','$2a$10$2KtlBG9s4.QckPpFaM13b.3jeC6D/gYcWxFOcCVkpk7k/9Iz03THy', true),
('bobby','$2a$10$2KtlBG9s4.QckPpFaM13b.3jeC6D/gYcWxFOcCVkpk7k/9Iz03THy', true);

INSERT INTO authorities (username, authority) values
('manoj','ROLE_USER'),
('ana','ROLE_USER'),
('roberta','ROLE_USER'),
('madhu','ROLE_USER'),
('bobby','ROLE_ADMIN');