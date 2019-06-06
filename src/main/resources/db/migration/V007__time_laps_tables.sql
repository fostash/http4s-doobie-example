CREATE TABLE time_table
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    circuit_id BIGINT,
    rider_id BIGINT,
    bike_id BIGINT,
    session_date VARCHAR(10),
    FOREIGN KEY (circuit_id) REFERENCES circuits(id),
    FOREIGN KEY (rider_id) REFERENCES riders(id),
    FOREIGN KEY (bike_id) REFERENCES bikes(id)
);

CREATE TABLE laps
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    time_table_id BIGINT,
    lap_number INTEGER,
    minutes INTEGER,
    seconds INTEGER,
    millis INTEGER,

    FOREIGN KEY (time_table_id) REFERENCES time_table(id)
);