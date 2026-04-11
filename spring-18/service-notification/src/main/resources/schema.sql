create table if not exists notifications (
    id bigserial,
    user_id varchar(255),
    message varchar(255) not null,
    primary key (id)
);
