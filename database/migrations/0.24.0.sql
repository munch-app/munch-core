create table workergroup
(
    uid         varchar(26)  not null
        constraint workergroup_pkey
            primary key,
    createdat   timestamp    not null,
    description varchar(500) not null,
    name        varchar(100) not null,
    updatedat   timestamp    not null
);

alter table workergroup
    owner to munch;


create table workertask
(
    uid         varchar(26)  not null
        constraint workertask_pkey
            primary key,
    completedat timestamp,
    details     jsonb,
    startedat   timestamp    not null,
    status      varchar(100) not null,
    updatedat   timestamp    not null,
    group_uid   varchar(26)  not null
        constraint fk6v9516m8fha96inx2f7x60cuh
            references workergroup
);

alter table workertask
    owner to munch;


