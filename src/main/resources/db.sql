create table users
(
    id           serial PRIMARY KEY,
    email        VARCHAR(254) UNIQUE NOT NULL,
    first_name   varchar(254)        NOT NULL,
    last_name    varchar(254)        NOT NULL,
    reference_id integer             NOT NULL
);