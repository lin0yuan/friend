create table follow
(
    subscriber_id bigint                              null comment '粉丝id',
    tutor_id      bigint                              null comment '被关注者',
    create_time   timestamp default CURRENT_TIMESTAMP null comment '添加关注或粉丝时间'
)
    comment '关注与粉丝';

create index follow_user_id_fk
    on follow (subscriber_id);

create index follow_user_id_fk_2
    on follow (tutor_id);

create table message
(
    id            bigint unsigned auto_increment comment '信息id',
    from_id       bigint                             null comment '发送者id',
    to_id         bigint                             null comment '接受者id',
    message_text  varchar(1024)                      null comment '消息内容',
    sent_datetime datetime default CURRENT_TIMESTAMP null comment '发送时间',
    file          varchar(255)                       null comment '发送到图片或者文件',
    message_type  int      default 0                 not null comment '消息类型,文本=0,视频/图片=1,文件=2,音频=3',
    constraint message_id_uindex
        unique (id)
)
    comment '信息数据表';

create index message_user_id_fk
    on message (from_id);

create index message_user_id_fk_2
    on message (to_id);

alter table message
    add primary key (id);

create table oauth_user
(
    source  varchar(20) not null comment '第三方授权官网',
    uuid    varchar(20) not null comment '授权方用户id',
    user_id bigint      null comment '对应用户表id'
)
    comment '第三方授权信息';

create table persistent_logins
(
    username  varchar(64) not null,
    series    varchar(64) not null
        primary key,
    token     varchar(64) not null,
    last_used timestamp   not null
);

create table user
(
    id            bigint auto_increment comment '主键'
        primary key,
    email_address varchar(32)                         not null comment '邮箱地址',
    surname       varchar(32)                         not null comment '用户名',
    user_role     tinyint   default 0                 null comment '用户角色：0=普通用户,1=管理员',
    avatar        varchar(255)                        not null comment '头像信息',
    user_status   tinyint   default 1                 not null comment '用户状态:0=在线,1=离线,2=禁用',
    create_time   timestamp default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间',
    password      varchar(125)                        not null,
    gender        tinyint   default 2                 null comment '性别:0=男,1=女,2=未填写',
    constraint user_email_address_uindex
        unique (email_address)
)
    comment '用户表';

create table user_info
(
    id               bigint unsigned                     not null comment '用户id',
    given_name       varchar(32)                         null comment '真实姓名',
    phone_number     varchar(32)                         null comment '手机号码',
    brief_self_intro varchar(625)                        null comment '自我介绍',
    faculty          varchar(255)                        null comment '天赋才能',
    good_at          varchar(255)                        null comment '擅长',
    hobby            varchar(255)                        null comment '兴趣爱好',
    self_intro       varchar(64)                         null comment '自我简介',
    create_time      timestamp default CURRENT_TIMESTAMP null comment '创建时间',
    update_time      timestamp default CURRENT_TIMESTAMP null comment '修改时间',
    age              int                                 null comment '年龄',
    constraint user_info_user_id_uindex
        unique (id)
);

alter table user_info
    add primary key (id);

