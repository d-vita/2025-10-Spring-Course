insert into tariffs (name, max_links, max_clicks_per_link)
values
    ('FREE', 100, 1000),
    ('BASIC', 1000, 10000),
    ('PRO', 10000, 100000),
    ON CONFLICT (name) DO NOTHING;

insert into users (username, email, password, tariff_id)
values (
           'john_doe',
           'john@example.com',
           'password_hash_here',
           (select id from tariffs where name = 'FREE')
       );