create table couriers
(
    id         uuid not null primary key,
    name       varchar(500) not null,
    location_x bigint not null,
    location_y bigint not null,
    status     varchar(100) not null
    created_at timestamp not null,
    updated_at timestamp
);

create table transports
(
    id         uuid not null primary key,
    name       varchar(500) not null,
    speed      bigint not null,
    courier_id uuid constraint fk_couriers_transport references couriers,
    created_at timestamp not null,
    updated_at timestamp
);
