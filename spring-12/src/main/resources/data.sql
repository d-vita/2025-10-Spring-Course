insert into authors(full_name)
values
    ('Author_1'),
    ('Author_2'),
    ('Author_3');

insert into genres(name)
values
    ('Genre_1'),
    ('Genre_2'),
    ('Genre_3');

insert into books(title, author_id, genre_id)
values
    ('BookTitle_1', 1, 1),
    ('BookTitle_2', 2, 2),
    ('BookTitle_3', 3, 3);

insert into comments(message, book_id)
values
    ('Comment_1', 1), ('Comment_2', 2),
    ('Comment_3', 3), ('Comment_4', 1);

insert into users(username, password, enabled)
values
    ('admin', '$2a$10$DOWSDmY7pN0m5zFZ3L2EWeFhZJfZpZr6ZpL5M8nJkF4Z2J1L1y', true),
    ('user1', '2a$10$DOWSDmY7pN0m5zFZ3L2EWeFhZJfZpZr6ZpL5M8nJkF4Z2J1L1y', true);
