create table if not exists click_stats (
    id varchar(255) primary key,
    user_id bigint not null,
    short_url varchar(255) not null,
    clicks bigint not null default 0,
    last_click_at timestamp
);

create table if not exists url_created_stats (
    user_id bigserial primary key,
    urls_created bigint not null default 0,
    last_created_at timestamp
);

create table if not exists notifications (
    id bigserial primary key,
    user_id bigint not null,
    short_url varchar(255) not null,
    message varchar(500) not null,
    read boolean not null default false,
    created_at timestamp not null
);

create index if not exists idx_notifications_user_id on notifications(user_id);
create index if not exists idx_notifications_user_read on notifications(user_id, read);

create table if not exists user_tariff_cache (
   user_id bigserial primary key,
   max_clicks_per_link bigint not null,
   max_links bigint not null,
   tariff_name varchar(100)
);