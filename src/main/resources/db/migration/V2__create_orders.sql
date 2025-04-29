create table orders
(
    id                  uuid not null primary key,
    delivery_location_x int not null,
    delivery_location_y int not null,
    status              varchar(100) not null,
    courier_id          uuid
    created_at          timestamp not null,
    updated_at          timestamp not null
);
