create database ai_librarian;
use ai_librarian;

create table dataset
(
    id            bigint auto_increment
        primary key,
    name          varchar(20)                         not null comment 'unique',
    remark        varchar(255)                        null,
    relevant_size int       default 3                 not null comment 'number of related data queries',
    vector_type   varchar(255)                        not null,
    create_time   timestamp default CURRENT_TIMESTAMP not null,
    update_time   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);


create table document
(
    id          bigint auto_increment
        primary key,
    dataset_id  bigint                              not null,
    name        varchar(255)                        not null,
    tags        text                                not null,
    hash_code   varchar(255)                        not null,
    state       varchar(255)                        not null comment 'wait，processing，complete,error',
    file_key    text                                null comment 'Object Storage key',
    size        bigint    default 0                 not null,
    type        varchar(255)                        null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);


create table document_qdrant
(
    id          bigint auto_increment
        primary key,
    document_id bigint       not null,
    qdrant_id   text         not null,
    info        text         not null,
    state       varchar(255) not null comment 'processing，complete,error'
);

create table json_qdrant
(
    dataset_id bigint       not null,
    qdrant_id  varchar(255) not null,
    info       json         not null,
    primary key (dataset_id, qdrant_id)
);


create table scene
(
    id          bigint auto_increment
        primary key,
    name        varchar(30)                         not null,
    remark      varchar(255)                        null,
    template    varchar(255)                        not null default '${message}' comment '${message},${data}',
    dataset_id  bigint                              null,
    model_type  varchar(255)                        not null,
    params      json                                not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table chat_context
(
    id        varchar(255) not null,
    sort      int          not null,
    user      text         not null,
    assistant text         not null,
    primary key (id, sort)
);

create table external_model
(
    id                       bigint auto_increment
        primary key,
    name                     varchar(30)                         not null,
    remark                   varchar(255)                        null,
    chat_address             varchar(255)                        not null,
    input_max_token          int                                 not null default 3000,
    check_parameters_address varchar(255)                        null,
    create_time              timestamp default CURRENT_TIMESTAMP not null,
    update_time              timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

create table external_vector
(
    id          bigint auto_increment
        primary key,
    name        varchar(30)                         not null,
    size        int                                 not null comment "model size",
    remark      varchar(255)                        null,
    address     varchar(255)                        not null,
    create_time timestamp default CURRENT_TIMESTAMP not null,
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
);

