create table if not exists click_stats (
id varchar(255) primary key, -- shortUrl:userId
user_id bigint not null,
short_url varchar(255) not null,
clicks bigint not null default 0,
last_click_at timestamp
);
