CREATE TABLE rider_bikes
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    rider_id BIGINT NOT NULL,
    bike_id BIGINT NOT NULL,
    name VARCHAR(50) NOT NULL,
    MAX_SPEED DECIMAL,
    CC DECIMAL,

    FOREIGN KEY (rider_id) REFERENCES riders(id),
    FOREIGN KEY (bike_id) REFERENCES bikes(id)
);