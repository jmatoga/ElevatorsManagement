INSERT INTO users (id, name, surname, email, created, password, current_floor)
VALUES ('07bf115f-2049-11ef-b0c9-74563c18f0cb', 'Wiktor', 'Rogal', 'wrogal@example.com', '2024-06-01 20:59:11',
        '$2a$10$HQDbQLUZtEIdVIWAT2SvnuO7sqlmt1kNNDj0qgcMmaQLvm01K7HP6', 0),
       ('74d7971b-2048-11ef-b0c9-74563c18f0cb', 'Jan', 'gfsaga', 'jsfa@example.com', '2024-06-01 20:55:05',
        '$2a$10$iYpmIz84JdftSp.xSNg7QeKUCAw.0J6UJ7UCJPRKMbefhMIujpJau', 1),
       ('ab5bd236-2048-11ef-b0c9-74563c18f0cb', 'Anna', 'Wesołowska', 'awesolowska@example.com', '2024-06-01 20:56:36',
        '$2a$10$HQDbQLUZtEIdVIWAT2SvnuO7sqlmt1kNNDj0qgcMmaQLvm01K7HP6', 2),
       ('d39177c3-2048-11ef-b0c9-74563c18f0cb', 'Maks', 'Kolonko', 'mkolonko@example.com', '2024-06-01 20:57:44',
        '$2a$10$FYI32F1lmtIb0.TWhK.iouoby1HKxitORkV0aB7hgSqzwMGuTrQPy', 3),
       ('e8a05af4-2048-11ef-b0c9-74563c18f0cb', 'Ewa', 'Naczelna', 'enaczelna@example.com', '2024-06-01 20:58:19',
        '$2a$10$HQDbQLUZtEIdVIWAT2SvnuO7sqlmt1kNNDj0qgcMmaQLvm01K7HP6', 4);

INSERT INTO user_role (role, user_id)
VALUES ('ROLE_USER', '07bf115f-2049-11ef-b0c9-74563c18f0cb'),
       ('ROLE_USER', '74d7971b-2048-11ef-b0c9-74563c18f0cb'),
       ('ROLE_USER', 'ab5bd236-2048-11ef-b0c9-74563c18f0cb'),
       ('ROLE_ADMIN', 'd39177c3-2048-11ef-b0c9-74563c18f0cb'),
       ('ROLE_USER', 'e8a05af4-2048-11ef-b0c9-74563c18f0cb');

INSERT INTO elevators (id, number, current_floor, current_direction, status)
VALUES
    ('e943af36-2048-11ef-b0c9-74563c18f0cb', 1, 0, 'NONE', 'IDLE'),
    ('f053da59-2048-11ef-b0c9-74563c18f0cb', 2, 0, 'NONE', 'IDLE'),
    ('f1d1007c-2048-11ef-b0c9-74563c18f0cb', 3, 0, 'NONE', 'IDLE'),
    ('f2a59392-2048-11ef-b0c9-74563c18f0cb', 4, 0, 'NONE', 'IDLE'),
    ('f36c3f1b-2048-11ef-b0c9-74563c18f0cb', 5, 0, 'NONE', 'IDLE'),
    ('f4385d7c-2048-11ef-b0c9-74563c18f0cb', 6, 0, 'NONE', 'IDLE'),
    ('f4fcf12f-2048-11ef-b0c9-74563c18f0cb', 7, 0, 'NONE', 'IDLE'),
    ('f5c49fe8-2048-11ef-b0c9-74563c18f0cb', 8, 0, 'NONE', 'IDLE'),
    ('f697eb8a-2048-11ef-b0c9-74563c18f0cb', 9, 0, 'NONE', 'IDLE'),
    ('f7587883-2048-11ef-b0c9-74563c18f0cb', 10, 0, 'NONE', 'IDLE'),
    ('f81a9e9e-2048-11ef-b0c9-74563c18f0cb', 11, 0, 'NONE', 'IDLE'),
    ('f8e72725-2048-11ef-b0c9-74563c18f0cb', 12, 0, 'NONE', 'IDLE'),
    ('f9aa6d76-2048-11ef-b0c9-74563c18f0cb', 13, 0, 'NONE', 'IDLE'),
    ('fa6eeb77-2048-11ef-b0c9-74563c18f0cb', 14, 0, 'NONE', 'IDLE'),
    ('fb415f08-2048-11ef-b0c9-74563c18f0cb', 15, 0, 'NONE', 'IDLE'),
    ('fc126a25-2048-11ef-b0c9-74563c18f0cb', 16, 0, 'NONE', 'IDLE');


-- INSERT INTO users_in_elevators (elevator_id, user_id)
-- VALUES
--     ('e943af36-2048-11ef-b0c9-74563c18f0cb', '07bf115f-2049-11ef-b0c9-74563c18f0cb'),
--     ('e943af36-2048-11ef-b0c9-74563c18f0cb', '74d7971b-2048-11ef-b0c9-74563c18f0cb'),
--     ('e943af36-2048-11ef-b0c9-74563c18f0cb', 'ab5bd236-2048-11ef-b0c9-74563c18f0cb'),
--     ('e943af36-2048-11ef-b0c9-74563c18f0cb', 'd39177c3-2048-11ef-b0c9-74563c18f0cb'),
--     ('e943af36-2048-11ef-b0c9-74563c18f0cb', 'e8a05af4-2048-11ef-b0c9-74563c18f0cb');