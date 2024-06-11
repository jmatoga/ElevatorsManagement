CREATE TABLE users
(
    id       UUID         NOT NULL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    surname  VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    created  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE user_role
(
    role    enum ('ROLE_USER','ROLE_ADMIN') NOT NULL,
    user_id UUID                            NOT NULL,
    CONSTRAINT `fk_user_user_role` FOREIGN KEY (user_id) REFERENCES users (id)
);