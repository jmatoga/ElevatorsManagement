INSERT INTO users (id, name, surname, email, created, password)
VALUES ('07bf115f-2049-11ef-b0c9-74563c18f0cb', 'Wiktor', 'Rogal', 'wrogal@example.com', '2024-06-01 20:59:11',
        '$2a$10$HQDbQLUZtEIdVIWAT2SvnuO7sqlmt1kNNDj0qgcMmaQLvm01K7HP6'),
       ('74d7971b-2048-11ef-b0c9-74563c18f0cb', 'Jan', 'gfsaga', 'jsfa@example.com', '2024-06-01 20:55:05',
        '$2a$10$iYpmIz84JdftSp.xSNg7QeKUCAw.0J6UJ7UCJPRKMbefhMIujpJau'),
       ('ab5bd236-2048-11ef-b0c9-74563c18f0cb', 'Anna', 'Wesołowska', 'awesolowska@example.com', '2024-06-01 20:56:36',
        '$2a$10$HQDbQLUZtEIdVIWAT2SvnuO7sqlmt1kNNDj0qgcMmaQLvm01K7HP6'),
       ('d39177c3-2048-11ef-b0c9-74563c18f0cb', 'Maks', 'Kolonko', 'mkolonko@example.com', '2024-06-01 20:57:44',
        '$2a$10$FYI32F1lmtIb0.TWhK.iouoby1HKxitORkV0aB7hgSqzwMGuTrQPy'),
       ('e8a05af4-2048-11ef-b0c9-74563c18f0cb', 'Ewa', 'Naczelna', 'enaczelna@example.com', '2024-06-01 20:58:19',
        '$2a$10$HQDbQLUZtEIdVIWAT2SvnuO7sqlmt1kNNDj0qgcMmaQLvm01K7HP6');

INSERT INTO user_role (role, user_id)
VALUES ('ROLE_USER', '07bf115f-2049-11ef-b0c9-74563c18f0cb'),
       ('ROLE_USER', '74d7971b-2048-11ef-b0c9-74563c18f0cb'),
       ('ROLE_USER', 'ab5bd236-2048-11ef-b0c9-74563c18f0cb'),
       ('ROLE_ADMIN', 'd39177c3-2048-11ef-b0c9-74563c18f0cb'),
       ('ROLE_USER', 'e8a05af4-2048-11ef-b0c9-74563c18f0cb');