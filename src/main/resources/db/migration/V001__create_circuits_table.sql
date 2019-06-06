CREATE TABLE circuits
(id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(50),
 country VARCHAR(2),
 location VARCHAR(256),
 length DOUBLE,
 left_curves INT,
 right_curves INT,
 description VARCHAR(256)
 );
