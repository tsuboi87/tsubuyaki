SET SESSION FOREIGN_KEY_CHECKS=0;

/* Drop Indexes */





/* Drop Tables */

DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS users;




/* Create Tables */

CREATE TABLE messages
(
	id int NOT NULL AUTO_INCREMENT,
	user_id int NOT NULL,
	text text NOT NULL,
	image mediumblob,
	image_file_name varchar(64),
	is_public boolean NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	PRIMARY KEY (id)
);


CREATE TABLE users
(
	id int NOT NULL AUTO_INCREMENT,
	account varchar(32) NOT NULL,
	name varchar(64) NOT NULL,
	password varchar(256) NOT NULL,
	salt varchar(64) NOT NULL,
	is_admin boolean NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	PRIMARY KEY (id),
	UNIQUE (account)
);



/* Create Foreign Keys */

ALTER TABLE messages
	ADD FOREIGN KEY (user_id)
	REFERENCES users (id)
	ON UPDATE RESTRICT
	ON DELETE RESTRICT
;



/* Create Indexes */

CREATE INDEX index_account_users ON users (account ASC);



