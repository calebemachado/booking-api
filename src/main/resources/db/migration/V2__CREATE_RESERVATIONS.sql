create table reservations
(
    id         uuid PRIMARY KEY,
    place_id   uuid                     NOT NULL,
    tenant_id  uuid,
    type       VARCHAR(100)             NOT NULL,
    status     VARCHAR(100),
    start_date timestamp with time zone NOT NULL,
    end_date   timestamp with time zone NOT NULL
)