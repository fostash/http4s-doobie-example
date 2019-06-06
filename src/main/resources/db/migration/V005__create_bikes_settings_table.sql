CREATE TABLE bikes_settings
(
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    circuit_id BIGINT NOT NULL,
    rider_bike_id BIGINT NOT NULL,
    front_preloading BIGINT NOT NULL,
    front_compression BIGINT NOT NULL,
    front_extension BIGINT NOT NULL,
    rear_preloading BIGINT NOT NULL,
    rear_compression BIGINT NOT NULL,
    rear_extension BIGINT NOT NULL,
    pinion INT NOT NULL,
    crown INT NOT NULL,
    height BIGINT NOT NULL,

    FOREIGN KEY (circuit_id) REFERENCES circuits(id),
    FOREIGN KEY (rider_bike_id) REFERENCES rider_bikes(id)
);
