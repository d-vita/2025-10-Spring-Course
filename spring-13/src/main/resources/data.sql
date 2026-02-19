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
    ('ROLE_ADMIN', 'System Administrator'),
    ('ROLE_EDITOR', 'Can edit book'),
    ('ROLE_USER', 'Regular user');

INSERT INTO users (username, password, enabled) VALUES
    ('admin', '$2a$10$DowJZlXhMZ0N6T0/W5p6sOzQ2kI6Q6tRwYB6G7z6qrxq2YKqF3vQe', true),
    ('editor', '$2a$10$DowJZlXhMZ0N6T0/W5p6sOzQ2kI6Q6tRwYB6G7z6qrxq2YKqF3vQe', true),
    ('user', '$2a$10$314MEUjIUx.Rvx6rPhS4uOLXD0OnIPATWmw/h1w26uAGAbO6Imei', true);

INSERT INTO user_roles (user_id, role_id) VALUES
    (1, 1),
    (1, 3),
    (2, 2),
    (3, 3);