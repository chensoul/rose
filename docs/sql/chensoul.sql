DROP DATABASE IF EXISTS `chensoul`;
create database `chensoul` default character set utf8mb4 collate utf8mb4_general_ci;

USE `chensoul`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `sys_app`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`         varchar(32)  DEFAULT NULL COMMENT '编码',
    `name`         varchar(32)  DEFAULT NULL COMMENT '名称',
    `remark`       varchar(512) DEFAULT NULL COMMENT '备注',
    `status`       smallint     DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`   smallint     DEFAULT 0 COMMENT '是否删除',
    `created_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `created_time` datetime     DEFAULT now() COMMENT '创建时间',
    `updated_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime     DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`)
) COMMENT='应用 ';

INSERT INTO `sys_app` (`id`, `code`, `name`, `status`)
VALUES (1, 'chensoul-cloud', '内部业务', 1);

CREATE TABLE `sys_client`
(
    `id`                      bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_id`               varchar(32)   DEFAULT NULL COMMENT '编码',
    `client_secret`           varchar(64)   DEFAULT NULL COMMENT '密钥',
    `resource_ids`            varchar(512)  DEFAULT NULL COMMENT '资源ServerID',
    `scope`                   varchar(32)   DEFAULT NULL COMMENT '作用域',
    `authorized_grant_types`  varchar(1024) DEFAULT NULL COMMENT '授权方式',
    `web_server_redirect_uri` varchar(1024) DEFAULT NULL COMMENT '回调地址',
    `authorities`             varchar(1024) DEFAULT NULL COMMENT '权限列表',
    `access_token_validity`   int(11) DEFAULT NULL COMMENT '请求令牌有效时间',
    `refresh_token_validity`  int(11) DEFAULT NULL COMMENT '刷新令牌有效时间',
    `additional_information`  text COMMENT '扩展信息',
    `autoapprove`             int(11) DEFAULT NULL COMMENT '是否自动放行',
    `status`                  smallint      DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`              smallint      DEFAULT 0 COMMENT '是否删除',
    `created_by`              varchar(32)   DEFAULT NULL COMMENT '创建人',
    `created_time`            datetime      DEFAULT now() COMMENT '创建时间',
    `updated_by`              varchar(32)   DEFAULT NULL COMMENT '更新人',
    `updated_time`            datetime      DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='客户端 ';

INSERT INTO `sys_client` (`id`, `client_id`, client_secret, `scope`, `authorized_grant_types`,
                          `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`,
                          `additional_information`, `autoapprove`)
VALUES (1, 'chensoul-cloud', 'chensoul-cloud', 'server', 'password,authorization_code,client_credentials,refresh_token',
        NULL, NULL, 600, 43200, NULL, 1);

CREATE TABLE `sys_log`
(
    `id`              bigint(64) NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name`            varchar(255) NOT NULL,
    `trace_id`        varchar(32)   DEFAULT NULL,
    `service_id`      varchar(32)   DEFAULT NULL,
    `server_host`     varchar(32)   DEFAULT NULL,
    `server_ip`       varchar(32)   DEFAULT NULL,
    `env`             varchar(16)   DEFAULT NULL,
    `remote_ip`       varchar(32)   DEFAULT NULL,
    `remote_location` varchar(256)  DEFAULT NULL,
    `user_agent`      varchar(1000) DEFAULT NULL,
    `request_uri`     varchar(255)  DEFAULT NULL,
    `request_args`    text,
    `request_method`  varchar(10)   DEFAULT NULL,
    `response_result` text,
    `cost_time`       bigint       not null,
    `success`         smallint      DEFAULT 1 COMMENT '是否成功',
    `fail_reason`     text,
    `tenant_id`       varchar(32)   DEFAULT NULL COMMENT '租户',
    `created_by`      varchar(32)   DEFAULT NULL COMMENT '创建人',
    `created_time`    datetime      DEFAULT now() COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='日志表';

CREATE TABLE `sys_resource`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `app_id`       bigint(20) DEFAULT NULL COMMENT '客户端ID',
    `parent_id`    bigint(20) DEFAULT NULL COMMENT '父级ID',
    `name`         varchar(32)   DEFAULT NULL COMMENT '名称',
    `remark`       varchar(512)  DEFAULT NULL COMMENT '备注',
    `icon`         varchar(64)   DEFAULT NULL COMMENT '图标',
    `url`          varchar(1024) DEFAULT NULL COMMENT 'URL',
    `open_mode`    int(11) DEFAULT NULL COMMENT '打开方式 0默认单页打开，1打开新页面，2iframe打开',
    `permission`   varchar(32)   DEFAULT NULL COMMENT '权限标识码',
    `type`         int(11) DEFAULT NULL COMMENT '类型 0：目录   1：菜单   2：uri',
    `sort`         int(11) DEFAULT NULL COMMENT '排序',
    `status`       smallint      DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`   smallint      DEFAULT 0 COMMENT '是否删除',
    `created_by`   varchar(32)   DEFAULT NULL COMMENT '创建人',
    `created_time` datetime      DEFAULT now() COMMENT '创建时间',
    `updated_by`   varchar(32)   DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime      DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='资源 ';

CREATE TABLE `sys_role`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`         varchar(32)  DEFAULT NULL COMMENT '角色名',
    `code`         varchar(32)  DEFAULT NULL COMMENT '角色编码',
    `remark`       varchar(512) DEFAULT NULL COMMENT '备注',
    `status`       smallint     DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`   smallint     DEFAULT 0 COMMENT '是否删除',
    `tenant_id`    varchar(32)  DEFAULT NULL COMMENT '租户',
    `created_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `created_time` datetime     DEFAULT now() COMMENT '创建时间',
    `updated_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime     DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
)COMMENT='角色 ';

INSERT INTO `sys_role` (`id`, `name`, `code`, `tenant_id`)
VALUES (1, '超级管理员', 'super_admin', '000000');

CREATE TABLE `sys_role_resource`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`      bigint(20) DEFAULT NULL COMMENT '角色ID',
    `resource_id`  bigint(20) DEFAULT NULL COMMENT '资源ID',
    `tenant_id`    varchar(32) DEFAULT NULL COMMENT '租户',
    `created_by`   varchar(32) DEFAULT NULL COMMENT '创建人',
    `created_time` datetime    DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='角色资源表';

CREATE TABLE `sys_tenant`
(
    `id`            varchar(32) NOT NULL COMMENT '主键',
    `name`          varchar(64)  DEFAULT NULL COMMENT '名称',
    `domain`        varchar(512) DEFAULT NULL COMMENT '域名',
    `contact_name`  varchar(512) DEFAULT NULL COMMENT '联系人姓名',
    `contact_phone` varchar(32)  DEFAULT NULL COMMENT '联系人手机号',
    `address`       varchar(257) DEFAULT NULL COMMENT '联系人地址',
    `expire_time`   datetime     DEFAULT NULL COMMENT '过期时间',
    `status`        smallint     DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`    smallint     DEFAULT 0 COMMENT '是否删除',
    `created_by`    varchar(32)  DEFAULT NULL COMMENT '创建人',
    `created_time`  datetime     DEFAULT now() COMMENT '创建时间',
    `updated_by`    varchar(32)  DEFAULT NULL COMMENT '更新人',
    `updated_time`  datetime     DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户表';

CREATE TABLE `sys_tenant_app`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `app_id`       bigint(20) DEFAULT NULL COMMENT '应用ID',
    `tenant_id`    varchar(32) DEFAULT NULL COMMENT '租户ID',
    `created_by`   varchar(32) DEFAULT NULL COMMENT '创建人',
    `created_time` datetime    DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) COMMENT='租户应用表';


CREATE TABLE `sys_user`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `real_name`       varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '真实姓名',
    `phone`           varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '手机号',
    `last_login_ip`   varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '上次登录IP',
    `last_login_time` datetime                       DEFAULT NULL COMMENT '上次登录时间',
    `status`          smallint                       DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`      smallint                       DEFAULT 0 COMMENT '是否删除',
    `created_by`      varchar(32)                    DEFAULT NULL COMMENT '创建人',
    `created_time`    datetime                       DEFAULT now() COMMENT '创建时间',
    `updated_by`      varchar(32)                    DEFAULT NULL COMMENT '更新人',
    `updated_time`    datetime                       DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='用户 ';

INSERT INTO `sys_user` (`id`, `real_name`, `phone`)
VALUES (1, 'admin', '1380000000');

CREATE TABLE `sys_dept`
(
    `id`           bigint NOT NULL COMMENT '主键',
    `parent_id`    bigint        DEFAULT '0' COMMENT '父主键',
    `trace_id`     varchar(2000) DEFAULT NULL COMMENT '祖级列表',
    `name`         varchar(45)   DEFAULT NULL COMMENT '部门名',
    `full_name`    varchar(45)   DEFAULT NULL COMMENT '部门全称',
    `sort`         int           DEFAULT NULL COMMENT '排序',
    `remark`       varchar(255)  DEFAULT NULL COMMENT '备注',
    `tenant_id`    varchar(32)   DEFAULT NULL COMMENT '租户ID',
    `status`       smallint      DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`   smallint      DEFAULT 0 COMMENT '是否删除',
    `created_by`   varchar(32)   DEFAULT NULL COMMENT '创建人',
    `created_time` datetime      DEFAULT now() COMMENT '创建时间',
    `updated_by`   varchar(32)   DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime      DEFAULT now() COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE
) COMMENT='部门表';

CREATE TABLE `sys_post`
(
    `id`           bigint NOT NULL COMMENT '主键',
    `code`         varchar(12)  DEFAULT NULL COMMENT '岗位编号',
    `name`         varchar(64)  DEFAULT NULL COMMENT '岗位名称',
    `type`         smallint     DEFAULT NULL COMMENT '岗位类型',
    `sort`         int          DEFAULT NULL COMMENT '岗位排序',
    `remark`       varchar(255) DEFAULT NULL COMMENT '岗位描述',
    `tenant_id`    varchar(32)  DEFAULT NULL COMMENT '租户ID',
    `status`       smallint     DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`   smallint     DEFAULT 0 COMMENT '是否删除',
    `created_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `created_time` datetime     DEFAULT now() COMMENT '创建时间',
    `updated_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime     DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='岗位表';

CREATE TABLE `sys_announce`
(
    `id`           bigint NOT NULL COMMENT '主键',
    `title`        varchar(255) DEFAULT NULL COMMENT '标题',
    `content`      varchar(255) DEFAULT NULL COMMENT '内容',
    `type`         smallint     DEFAULT NULL COMMENT '类型',
    `release_time` datetime     DEFAULT NULL COMMENT '发布时间',
    `tenant_id`    varchar(32)  DEFAULT NULL COMMENT '租户ID',
    `status`       smallint     DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`   smallint     DEFAULT 0 COMMENT '是否删除',
    `created_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `created_time` datetime     DEFAULT now() COMMENT '创建时间',
    `updated_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime     DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='公告表';

CREATE TABLE `sys_tenant_user_role`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      bigint(20) DEFAULT NULL COMMENT '用户ID',
    `role_id`      bigint(20) DEFAULT NULL COMMENT '角色ID',
    `tenant_id`    varchar(32) DEFAULT NULL COMMENT '租户',
    `created_by`   varchar(32) DEFAULT NULL COMMENT '创建人',
    `created_time` datetime    DEFAULT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='租户用户角色表';

CREATE TABLE `sys_tenant_user`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      bigint(20) DEFAULT NULL COMMENT '用户ID',
    `nickname`     varchar(32)  DEFAULT NULL COMMENT ' 昵称',
    `email`        varchar(128) DEFAULT NULL COMMENT ' 邮箱',
    `avatar`       varchar(512) DEFAULT NULL COMMENT ' 头像',
    `dept_id`      bigint(20) DEFAULT NULL COMMENT '部门ID',
    `post_id`      bigint(20) DEFAULT NULL COMMENT '岗位ID',
    `tenant_id`    varchar(32)  DEFAULT NULL COMMENT '租户',
    `status`       smallint     DEFAULT 1 COMMENT '是否可用 0不可用，1可用',
    `is_deleted`   smallint     DEFAULT 0 COMMENT '是否删除',
    `created_by`   varchar(32)  DEFAULT NULL COMMENT '创建人',
    `created_time` datetime     DEFAULT now() COMMENT '创建时间',
    `updated_by`   varchar(32)  DEFAULT NULL COMMENT '更新人',
    `updated_time` datetime     DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
) COMMENT='租户用户表';
