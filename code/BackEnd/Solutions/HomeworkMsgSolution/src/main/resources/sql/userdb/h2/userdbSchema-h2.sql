DROP TABLE authorities IF EXISTS;

DROP TABLE users IF EXISTS;

-- User tables
create memory table users(
    id int auto_increment NOT NULL,
	username varchar_ignorecase(50) not null primary key,
	password varchar_ignorecase(500) not null,
	enabled boolean not null);

create memory table authorities (
	username varchar_ignorecase(50) not null,
	authority varchar_ignorecase(50) not null,
	constraint fk_authorities_users foreign key(username) references users(username));

create unique index ix_auth_username on authorities (username,authority);
