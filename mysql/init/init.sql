create database ai_librarian;
use ai_librarian;
create table dataset
(
    id            bigint auto_increment
        primary key,
    name          varchar(20)                         not null comment '名称，唯一',
    remark        varchar(255)                        null comment '备注',
    relevant_size int       default 3                 not null comment '查询相关数据条数',
    vector_type   varchar(255)                        not null comment '使用向量计算方式',
    create_time   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '数据集';


create table document
(
    id          bigint auto_increment
        primary key,
    dataset_id  bigint                              not null comment '数据集id',
    name        varchar(255)                        not null comment '名称',
    hash_code   varchar(255)                        not null comment '文件hash',
    state       varchar(255)                        not null comment '文件状态 wait，processing，complete,error',
    file_key    text                                null comment '文件key',
    size        bigint    default 0                 not null comment '文件大小',
    type        varchar(255)                        null comment '类型',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '文件数据';


create table document_qdrant
(
    id          bigint auto_increment
        primary key,
    document_id bigint       not null comment 'document_id',
    qdrant_id   varchar(255) not null comment 'qdrant_id',
    info        text         not null comment 'info',
    state       varchar(255) not null comment '处理状态 processing，complete,error'
) comment '文件数据和qdrant关联';

create table json_qdrant
(
    dataset_id bigint       not null comment '数据集id',
    qdrant_id  varchar(255) not null comment 'qdrant_id',
    info       json         not null comment 'info',
    primary key (dataset_id, qdrant_id)
) comment 'json数据和qdrant关联';


create table scene
(
    id          bigint auto_increment
        primary key,
    name        varchar(30)                         not null comment '名称',
    remark      varchar(255)                        null comment '简介',
    template    varchar(255)                        not null default '${message}' comment '消息输入模版',
    dataset_id  bigint                              null comment '数据集id',
    model_type  varchar(255)                        not null comment '使用模型类型',
    params      json                                not null comment '参数',
    create_time timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '情景';

create table chat_context
(
    id        varchar(255) not null,
    sort      int          not null comment '顺序',
    user      text         not null comment '用户输入',
    assistant text         not null comment 'ai输出',
    primary key (id, sort)
)
    comment '上下文';

create table external_model
(
    id                       bigint auto_increment
        primary key,
    name                     varchar(30)                         not null comment '名称',
    remark                   varchar(255)                        null comment '简介',
    chat_address             varchar(255)                        not null comment 'chat请求地址',
    input_max_token          int                                 not null default 3000 comment '输入最大token',
    check_parameters_address varchar(255)                        null comment '检查参数请求地址',
    create_time              timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time              timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
)
    comment '自定义模型表';