create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (id)
);

create table comments (
    id bigserial,
    message varchar(255),
    book_id bigint references books(id) on delete cascade,
    primary key (id)
);

create table users (
   id bigserial,
   username varchar(255) not null unique,
   password varchar(255) not null,
   enabled boolean not null default true,
   primary key (id)
);

create table roles (
   id bigserial,
   role_name varchar(50) not null unique,
   role_description varchar(255),
   primary key (id)
);


create table user_roles (
   user_id bigint not null,
   role_id bigint not null,
   primary key (user_id, role_id),

   constraint fk_user_roles_user
       foreign key (user_id)
       references users (id)
       on delete cascade,

   constraint fk_user_roles_role
       foreign key (role_id)
       references roles (id)
       on delete cascade
);

create index idx_users_username
    on users (username);

create index idx_roles_role_name
    on roles (role_name);

create index idx_user_roles_user
    on user_roles (user_id);

create index idx_user_roles_role
    on user_roles (role_id);