create table reservations
(
    id          uuid PRIMARY KEY,
    address     VARCHAR(100)             NOT NULL,
    description VARCHAR(100),
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone NOT NULL,
    owner_id    uuid                     NOT NULL,
)