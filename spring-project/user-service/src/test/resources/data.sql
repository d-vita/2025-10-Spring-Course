insert into tariffs (name, max_links, max_clicks_per_link)
values ('FREE_TEST', 100, 1000),
       ('BASIC_TEST', 1000, 10000),
       ('PRO_TEST', 10000, 100000);

insert into users (username, email, password, tariff_id)
values ('john_doe_test', 'john_test@example.com', 'password_hash_here',
        (select id from tariffs where name='FREE_TEST'));
