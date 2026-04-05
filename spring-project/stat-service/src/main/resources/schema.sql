create table if not exists click_stats (
    id varchar(255) primary key,
    user_id bigint not null,
    short_url varchar(255) not null,
    clicks bigint not null default 0,
    last_click_at timestamp
);

create table if not exists url_created_stats (
    user_id bigint primary key,
    urls_created bigint not null default 0,
    last_created_at timestamp
);

