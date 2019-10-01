alter table placeimage
    add constraint uk_my3rcdy96e9fdwfs
        unique (place_id, image_uid);

create table profilesocial
(
    uid         varchar(26)  not null
        constraint profilesocial_pkey
            primary key,
    connectedat timestamp    not null,
    createdat   timestamp    not null,
    eid         varchar(512) not null,
    name        varchar(100) not null,
    secrets     jsonb        not null,
    status      varchar(100) not null,
    type        varchar(100) not null,
    updatedat   timestamp    not null,
    profile_uid varchar(26)  not null
        constraint fklx406av986hdln6jlumxchfge
            references profile
);

alter table profilesocial
    owner to munch;

create table profilemedia
(
    uid         varchar(26)  not null
        constraint profilemedia_pkey
            primary key,
    content     jsonb        not null,
    createdat   timestamp    not null,
    eid         varchar(512) not null,
    metric      jsonb        not null,
    type        varchar(100) not null,
    updatedat   timestamp    not null,
    profile_uid varchar(26)  not null
        constraint fkk10up286cdfvb7f44ju5cy6eu
            references profile,
    social_uid  varchar(26)  not null
        constraint fkj14hpm2pk59kmqpdvp28l47vj
            references profilesocial,
    constraint uk_an27370fqpajyefr
        unique (type, eid)
);

alter table profilemedia
    owner to munch;

create table profilemedia_image
(
    profilemedia_uid varchar(26) not null
        constraint fk5adja6fhtc9fy0qxgj245us4
            references profilemedia,
    images_uid       varchar(26) not null
        constraint fk9bi7kls8xox8k23p11soi2o5w
            references image
);

alter table profilemedia_image
    owner to munch;

create table mention
(
    id            varchar(16)  not null
        constraint mention_pkey
            primary key,
    createdat     timestamp    not null,
    status        varchar(100) not null,
    type          varchar(100) not null,
    updatedat     timestamp    not null,
    article_id    varchar(13)
        constraint fksal18m1wnwlw4u994lnppshrw
            references article,
    createdby_uid varchar(26)  not null
        constraint fk8sixvsyxlyibd660bah64147g
            references profile,
    media_uid     varchar(26)
        constraint fkrna4ftrdrmk130g6kf7ktoj1p
            references profilemedia,
    place_id      varchar(13)
        constraint fk4n1l3c7a4o5c1qs7b1w44iy8i
            references place,
    profile_uid   varchar(26)  not null
        constraint fklkyfeg5245pu2l96rsor557kr
            references profile,
    constraint uk_2rjxx36ptzagatrp
        unique (place_id, article_id),
    constraint uk_88jnqnzqvndsmhnm
        unique (place_id, media_uid)
);

alter table mention
    owner to munch;