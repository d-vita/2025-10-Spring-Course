INSERT INTO authors(full_name) VALUES
    ('Author_1'),
    ('Author_2'),
    ('Author_3');

INSERT INTO genres(name) VALUES
    ('Genre_1'),
    ('Genre_2'),
    ('Genre_3');

INSERT INTO books(title, author_id, genre_id) VALUES
    ('BookTitle_1', 1, 1),
    ('BookTitle_2', 2, 2),
    ('BookTitle_3', 3, 3);

INSERT INTO comments(message, book_id) VALUES
    ('Comment_1', 1), ('Comment_2', 2),
    ('Comment_3', 3), ('Comment_4', 1);

INSERT INTO roles (role_name, role_description) VALUES
    ('ADMIN', 'System Administrator'),
    ('EDITOR', 'Can edit book'),
    ('USER', 'Regular user');

INSERT INTO users (username, password, enabled) VALUES
    ('admin', '$2a$10$GOp7OmaKXowPEJyY5rJi0Ou7eOWknNWFkP1O6cGCIXDD.bP4dY8NO', true),
    ('editor', '$2a$10$k1l0NyGVzZ8vRtQiaxKXDesxZLE8k/Ai5ohSjy3FvA71DQKgEoEiy', true),
    ('user', '$2a$10$vTYKPNy5T40sCltNOMYcg.zW1q4jpuLykEnVTAySVmCkzlzL5H2zW', true);

INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1),
    (1, 3),
    (2, 2),
    (3, 3);