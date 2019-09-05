create table image
(
    uid         varchar(26)  not null
        constraint image_pkey
            primary key,
    bucket      varchar(32)  not null,
    createdat   timestamp    not null,
    ext         varchar(255),
    height      integer      not null
        constraint image_height_check
            check (height >= 1),
    loc         varchar(100) not null
        constraint uk_aro66uxyjy7iyj61mspkbtdxu
            unique,
    source      varchar(100) not null,
    width       integer      not null
        constraint image_width_check
            check (width >= 1),
    profile_uid varchar(26)
);

alter table image
    owner to munch;

create table profile
(
    uid       varchar(26)  not null
        constraint profile_pkey
            primary key,
    bio       varchar(250),
    createdat timestamp    not null,
    name      varchar(100),
    updatedat timestamp    not null,
    username  varchar(255) not null
        constraint uk_1wpe1j4lyc9m4yny8kjfv7y0s
            unique,
    image_uid varchar(26)
        constraint fkehqb83t4qjts86918i7pmkmrm
            references image
);

alter table profile
    owner to munch;

alter table image
    add constraint fk9d8m80dutrddf0neiva8bs6hf
        foreign key (profile_uid) references profile;

create table place
(
    id            varchar(13)      not null
        constraint place_pkey
            primary key,
    description   varchar(250),
    hours         jsonb            not null,
    location      jsonb            not null,
    name          varchar(100),
    phone         varchar(100),
    price         jsonb,
    status        jsonb            not null,
    synonyms      jsonb            not null,
    tags          jsonb            not null,
    website       varchar(1000),
    cid           varchar(100)
        constraint uk_hfntkkwqt601im1sb0xp8kem6
            unique,
    createdat     timestamp        not null,
    slug          varchar(200)     not null,
    updatedat     timestamp        not null,
    image_uid     varchar(26)
        constraint fkl7r7kndcbemd8h432qu76s8nc
            references image,
    important     double precision not null,
    interactedat  timestamp        not null,
    createdby_uid varchar(26)      not null
        constraint fk1cpvhn1htn1kocq0x9vgwsg8o
            references profile
);

alter table place
    owner to munch;

create table account
(
    id              varchar(255) not null
        constraint account_pkey
            primary key,
    authenticatedat timestamp    not null,
    createdat       timestamp    not null,
    email           varchar(320) not null,
    updatedat       timestamp    not null,
    profile_uid     varchar(26)
        constraint fkhl7rm54573h7sqwf6h1vascms
            references profile
);

alter table account
    owner to munch;

create table article
(
    id          varchar(13)  not null
        constraint article_pkey
            primary key,
    content     jsonb        not null,
    description varchar(250),
    options     jsonb        not null,
    slug        varchar(200) not null,
    tags        jsonb        not null,
    title       varchar(100),
    createdat   timestamp    not null,
    status      varchar(100) not null,
    updatedat   timestamp    not null,
    image_uid   varchar(26)
        constraint fka1yy6vw2aa0pq256d5kq0joo1
            references image,
    profile_uid varchar(26)  not null
        constraint fkiqhg4aly542ef95o1bqxwenrl
            references profile,
    publishedat timestamp
);

alter table article
    owner to munch;

create table articleplace
(
    uid        varchar(26) not null
        constraint articleplace_pkey
            primary key,
    createdat  timestamp   not null,
    position   bigint      not null,
    updatedat  timestamp   not null,
    article_id varchar(13) not null
        constraint fk8b0tbiikty3krv49e37ni2lw5
            references article,
    place_id   varchar(13) not null
        constraint fkbifssqucuyxd9rugyr9qe6j7w
            references place
);

alter table articleplace
    owner to munch;

create table articlerevision
(
    uid         varchar(26)  not null
        constraint articlerevision_pkey
            primary key,
    content     jsonb        not null,
    description varchar(250),
    options     jsonb        not null,
    slug        varchar(200) not null,
    tags        jsonb        not null,
    title       varchar(100),
    createdat   timestamp    not null,
    id          varchar(13)  not null,
    published   boolean      not null,
    version     varchar(10)  not null,
    image_uid   varchar(26)
        constraint fk9g8f70cebjfyivn23sp65lkno
            references image,
    profile_uid varchar(26)  not null
        constraint fka028u05m67uyeeqhj8qoqnix8
            references profile,
    article_id  varchar(13)  not null
        constraint fkm86rr92qh1o8qrpdp8qpcvpcj
            references article
);

alter table articlerevision
    owner to munch;

create table placeimage
(
    uid         varchar(26) not null
        constraint placeimage_pkey
            primary key,
    createdat   timestamp   not null,
    updatedat   timestamp   not null,
    image_uid   varchar(26) not null
        constraint fka3y9kf2ijkydyeqhxdsrpd2pv
            references image,
    place_id    varchar(13) not null
        constraint fk62jfar36g6bkqba8rio8g3apl
            references place,
    profile_uid varchar(26) not null
        constraint fka6uu0h5drwepomdakqgps38ui
            references profile
);

alter table placeimage
    owner to munch;

create table publication
(
    id          varchar(13) not null
        constraint publication_pkey
            primary key,
    body        varchar(800),
    createdat   timestamp   not null,
    description varchar(250),
    name        varchar(80),
    tags        jsonb       not null,
    updatedat   timestamp   not null,
    image_uid   varchar(26)
        constraint fkc2te3g8cdfy3s24vqhl2819le
            references image
);

alter table publication
    owner to munch;

create table publicationarticle
(
    uid            varchar(26) not null
        constraint publicationarticle_pkey
            primary key,
    createdat      timestamp   not null,
    position       bigint      not null,
    article_id     varchar(13) not null
        constraint fkm03ndday2mvkbyy5wvlonwygk
            references article,
    publication_id varchar(13) not null
        constraint fkkjehbdpq7o9ng2qgxxh26nlu2
            references publication
);

alter table publicationarticle
    owner to munch;

create table tag
(
    id        varchar(13)  not null
        constraint tag_pkey
            primary key,
    createdat timestamp    not null,
    name      varchar(100),
    type      varchar(100) not null,
    updatedat timestamp    not null
);

alter table tag
    owner to munch;

create table profilelink
(
    uid         varchar(26)   not null
        constraint profilelink_pkey
            primary key,
    createdat   timestamp     not null,
    name        varchar(50)   not null,
    position    bigint        not null,
    type        varchar(100)  not null,
    updatedat   timestamp     not null,
    url         varchar(1000) not null,
    profile_uid varchar(26)   not null
        constraint fkcte1axxoouanub1m3eg2t8rnq
            references profile
);

alter table profilelink
    owner to munch;

create table placelocking
(
    createdat   timestamp    not null,
    type        varchar(100) not null,
    updatedat   timestamp    not null,
    place_id    varchar(13)  not null
        constraint placelocking_pkey
            primary key
        constraint fksi7jpcgd8yoy746v8h314crfa
            references place,
    profile_uid varchar(26)  not null
        constraint fk8f6mcphho03qthvgvi17htrkd
            references profile
);

alter table placelocking
    owner to munch;

create table profilerestriction
(
    uid         varchar(26)  not null
        constraint profilerestriction_pkey
            primary key,
    createdat   timestamp    not null,
    type        varchar(100) not null,
    profile_uid varchar(26)  not null
        constraint fkapl4pyeiyspqac34t7vj0vq12
            references profile
);

alter table profilerestriction
    owner to munch;

create table affiliatebrand
(
    uid       varchar(26)  not null
        constraint affiliatebrand_pkey
            primary key,
    name      varchar(100) not null,
    image_uid varchar(26)  not null
        constraint fkbof3731q6lnk52lkhfy8jppey
            references image
);

alter table affiliatebrand
    owner to munch;

create table changegroup
(
    uid           varchar(26)  not null
        constraint changegroup_pkey
            primary key,
    completedat   timestamp,
    createdat     timestamp    not null,
    description   varchar(500),
    name          varchar(100) not null,
    createdby_uid varchar(26)  not null
        constraint fk24h1l2de66jx2q8w2rg2yawj
            references profile
);

alter table changegroup
    owner to munch;

create table placerevision
(
    uid             varchar(26) not null
        constraint placerevision_pkey
            primary key,
    description     varchar(250),
    hours           jsonb,
    location        jsonb,
    name            varchar(100),
    phone           varchar(100),
    price           jsonb,
    status          jsonb,
    synonyms        jsonb,
    tags            jsonb,
    website         varchar(1000),
    createdat       timestamp   not null,
    id              varchar(13) not null,
    createdby_uid   varchar(26) not null
        constraint fkbo4nir8s7isvfvj5xkyspic0t
            references profile,
    place_id        varchar(13) not null
        constraint fk63c2x1604ujiv60fwje9k2knd
            references place,
    changegroup_uid varchar(26)
        constraint fkcqwd4krbgpei17jln0ca4y1fs
            references changegroup
);

alter table placerevision
    owner to munch;

create table affiliate
(
    uid             varchar(26)   not null
        constraint affiliate_pkey
            primary key,
    createdat       timestamp     not null,
    placestruct     jsonb         not null,
    source          varchar(100)  not null,
    sourcekey       varchar(2048) not null
        constraint uk_e4vposnyx1qrm5e8vs7l4kvx6
            unique,
    status          varchar(100)  not null,
    type            varchar(100)  not null,
    updatedat       timestamp     not null,
    url             varchar(2048) not null,
    place_id        varchar(13)
        constraint fko3fjbb266e305a2iy1hmd7f6p
            references place,
    brand_uid       varchar(26)   not null
        constraint fk6pamn20ancm82axm3rckmlk83
            references affiliatebrand,
    editedby_uid    varchar(26)
        constraint fk3wiiuxcw26qyd2rj56d25e9xl
            references profile,
    changegroup_uid varchar(26)
        constraint fk9rjd9bti3r6svsjf95vrogqv5
            references changegroup
);

alter table affiliate
    owner to munch;

create table placeaffiliate
(
    createdat     timestamp     not null,
    type          varchar(100)  not null,
    updatedat     timestamp     not null,
    url           varchar(2048) not null,
    affiliate_uid varchar(26)   not null
        constraint placeaffiliate_pkey
            primary key
        constraint fkdlkjvi7ymoy4p4cxf6no97e0x
            references affiliate,
    place_id      varchar(13)   not null
        constraint fkmal8rg00eadx44bxos9xq4uh6
            references place,
    brand_uid     varchar(26)   not null
        constraint fk26j703b12nw6k0s8kqjvvvlud
            references affiliatebrand
);

alter table placeaffiliate
    owner to munch;

