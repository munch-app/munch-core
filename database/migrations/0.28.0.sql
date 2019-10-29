create table placepost
(
    id          varchar(16)  not null
        constraint placepost_pkey
            primary key,
    content     varchar(500),
    createdat   timestamp    not null,
    status      varchar(100) not null,
    updatedat   timestamp    not null,
    place_id    varchar(13)  not null
        constraint fk6xp3uu8mm3xr4tfc675omxdk3
            references place,
    profile_uid varchar(26)  not null
        constraint fkcycla7kombhija2es4c75wct8
            references profile
);

alter table placepost
    owner to munch;

create table placepost_image
(
    placepost_id varchar(16) not null
        constraint fk9x5cmqnsvt7e2xrltxs7hxuwr
            references placepost,
    images_uid   varchar(26) not null
        constraint fk1erfqqnnhbd8wjagka3ro2k4h
            references image
);

alter table placepost_image
    owner to munch;

alter table mention
	add post_id varchar(16);

alter table mention
	add constraint fk4j1xamk36lggodqx1ylksbwlk
		foreign key (post_id) references placepost;

alter table mention
    add constraint uk_nvs8jmndqnhmzn8q
        unique (place_id, post_id);
