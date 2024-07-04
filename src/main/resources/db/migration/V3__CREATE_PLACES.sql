CREATE TABLE places
(
    id          uuid PRIMARY KEY,
    address     VARCHAR(255)             NOT NULL,
    description VARCHAR(255),
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone NOT NULL,
    owner_id    uuid                     NOT NULL
);
