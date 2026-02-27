INSERT INTO authors(full_name) VALUES
    ('Author_4'),
    ('Author_5'),
    ('Author_6');

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