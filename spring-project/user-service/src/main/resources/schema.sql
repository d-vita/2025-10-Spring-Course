create table if not exists tariffs (
    id bigserial,
    name varchar(255) not null unique,
    max_links bigint,
    max_clicks_per_link bigint,
    primary key (id)
);

create table if not exists users (
    id bigserial,
    username varchar(255),
    email varchar(255) not null unique,
    password varchar(255),
    tariff_id bigint references tariffs(id) on delete cascade,
    primary key (id)
);
