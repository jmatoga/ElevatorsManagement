CREATE TABLE users
(
    id            UUID         NOT NULL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    surname       VARCHAR(255) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    created       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    password      VARCHAR(255) NOT NULL,
    current_floor int          NOT NULL,
    destination_floor int
);

CREATE TABLE user_role
(
    role    enum ('ROLE_USER','ROLE_ADMIN') NOT NULL,
    user_id UUID                            NOT NULL,
    CONSTRAINT `fk_user_user_role` FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE refreshtoken
(
    id          UUID         NOT NULL,
    expiry_date timestamp    NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
    token       varchar(255) NOT NULL,
    user_id     UUID         NOT NULL
);

CREATE TABLE elevators
(
    id                UUID                      NOT NULL PRIMARY KEY,
    number            int                       NOT NULL,
    current_floor     int                       NOT NULL,
    current_direction enum ('UP','DOWN','NONE') NOT NULL,
    status            enum ('BUSY','IDLE')      NOT NULL
);


CREATE TABLE users_in_elevators
(
    elevator_id UUID NOT NULL,
    user_id     UUID NOT NULL,
    CONSTRAINT `fk_user_user_in_elevator` FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT `fk_elevator_user_in_elevator` FOREIGN KEY (elevator_id) REFERENCES elevators (id)
);

CREATE TABLE destinations_floor
(
    elevator_id UUID NOT NULL,
    floor     int NOT NULL,
    CONSTRAINT `fk_elevator_destinations_floor` FOREIGN KEY (elevator_id) REFERENCES elevators (id)
);