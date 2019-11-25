create table if not exists location
(
    id varchar(13) not null
        constraint location_pkey
            primary key,
    address varchar(255) not null,
    description varchar(250),
    geometry jsonb not null,
    latlng varchar(255) not null,
    name varchar(100) not null,
    postcode varchar(100),
    synonyms jsonb not null,
    type varchar(100) not null,
    createdat timestamp not null,
    interactedat timestamp not null,
    slug varchar(200) not null,
    updatedat timestamp not null,
    createdby_uid varchar(26) not null
        constraint fk2iygc2r3akec7ux04p6t4tqv3
            references profile,
    image_uid varchar(26)
        constraint fk3700378ahu53ji0cyngslw64r
            references image
);

alter table location owner to munch;

create table if not exists locationrevision
(
    uid varchar(26) not null
        constraint locationrevision_pkey
            primary key,
    address varchar(255) not null,
    description varchar(250),
    geometry jsonb not null,
    latlng varchar(255) not null,
    name varchar(100) not null,
    postcode varchar(100),
    synonyms jsonb not null,
    type varchar(100) not null,
    createdat timestamp not null,
    id varchar(13) not null,
    changegroup_uid varchar(26)
        constraint fkc4x6tsb6mj1ww9knxw4jbdi0g
            references changegroup,
    createdby_uid varchar(26) not null
        constraint fkb03j0qlq18qb5aq1ijrjj31pa
            references profile,
    location_id varchar(13) not null
        constraint fknu94sttbp1r3lsj2q3y3gq2c7
            references location
);

alter table locationrevision owner to munch;

