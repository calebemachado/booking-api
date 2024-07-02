create table persons
(
    id         uuid PRIMARY KEY,
    name       VARCHAR(100)             NOT NULL,
    surname    VARCHAR(100)             NOT NULL,
    active     BOOLEAN                  NOT NULL DEFAULT TRUE,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL
)