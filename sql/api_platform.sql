-- 创建库
create database if not exists ai_platform;

-- 切换库
use ai_platform;

-- 用户表
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`
(
    `id`   int(0)                                                 NOT NULL AUTO_INCREMENT,
    `name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for chat_model
-- ----------------------------
DROP TABLE IF EXISTS `chat_model`;
CREATE TABLE `chat_model`
(
    `id`         bigint(0)                                               NOT NULL AUTO_INCREMENT,
    `name`       varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL DEFAULT NULL COMMENT '模型名称',
    `prompt`     varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '模型预设(提示语)',
    `img`        varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标路径',
    `categoryId` int(0)                                                  NULL DEFAULT NULL COMMENT '分类\r\n',
    `createTime` datetime(0)                                             NULL DEFAULT CURRENT_TIMESTAMP(0),
    `udpateTime` datetime(0)                                             NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `isOpen`     tinyint(0)                                              NULL DEFAULT 0 COMMENT '是否公开',
    `isDelete`   tinyint(0)                                              NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`
(
    `id`         bigint(0)                                               NOT NULL DEFAULT 0,
    `userId`     bigint(0)                                               NULL     DEFAULT NULL COMMENT '用户id',
    `partentId`  bigint(0)                                               NULL     DEFAULT NULL COMMENT '父级评论id',
    `content`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '评论内容',
    `upvotes`    int(0)                                                  NULL     DEFAULT NULL COMMENT '点赞数量',
    `createTime` datetime(0)                                             NULL     DEFAULT CURRENT_TIMESTAMP(0),
    `updateTime` datetime(0)                                             NULL     DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `isDelete`   tinyint(0)                                              NULL     DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for post
-- ----------------------------
DROP TABLE IF EXISTS `post`;
CREATE TABLE `post`
(
    `id`            bigint(0)                                                NOT NULL AUTO_INCREMENT COMMENT 'id',
    `age`           int(0)                                                   NULL     DEFAULT NULL COMMENT '年龄',
    `gender`        tinyint(0)                                               NOT NULL DEFAULT 0 COMMENT '性别（0-男, 1-女）',
    `education`     varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '学历',
    `place`         varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '地点',
    `job`           varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '职业',
    `contact`       varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '联系方式',
    `loveExp`       varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '感情经历',
    `content`       text CHARACTER SET utf8 COLLATE utf8_general_ci          NULL COMMENT '内容（个人介绍）',
    `photo`         varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '照片地址',
    `reviewStatus`  int(0)                                                   NOT NULL DEFAULT 0 COMMENT '状态（0-待审核, 1-通过, 2-拒绝）',
    `reviewMessage` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '审核信息',
    `viewNum`       int(0)                                                   NOT NULL DEFAULT 0 COMMENT '浏览数',
    `thumbNum`      int(0)                                                   NOT NULL DEFAULT 0 COMMENT '点赞数',
    `userId`        bigint(0)                                                NOT NULL COMMENT '创建用户 id',
    `createTime`    datetime(0)                                              NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `updateTime`    datetime(0)                                              NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `isDelete`      tinyint(0)                                               NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '帖子'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           bigint(0)                                                NOT NULL AUTO_INCREMENT COMMENT 'id',
    `userName`     varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '用户昵称',
    `userAccount`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '账号',
    `userAvatar`   varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '用户头像',
    `gender`       tinyint(0)                                               NULL     DEFAULT NULL COMMENT '性别',
    `userRole`     varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL DEFAULT 'user' COMMENT '用户角色：user / admin',
    `userPassword` varchar(512) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '密码',
    `createTime`   datetime(0)                                              NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `updateTime`   datetime(0)                                              NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    `isDelete`     tinyint(0)                                               NOT NULL DEFAULT 0 COMMENT '是否删除',
    `email`        varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '邮箱',
    `status`       tinyint(0)                                               NOT NULL DEFAULT 1 COMMENT '状态0-停用 1-启用   ',
    `accessKey`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'accessKey',
    `secretKey`    varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT 'secretKey',
    `callNum`      int(0)                                                   NOT NULL DEFAULT 50 COMMENT '接口调用次数 默认有50次',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uni_userAccount` (`userAccount`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci COMMENT = '用户'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_model
-- ----------------------------
DROP TABLE IF EXISTS `user_model`;
CREATE TABLE `user_model`
(
    `id`          bigint(0)                                               NOT NULL,
    `modelId`     int(0)                                                  NULL DEFAULT NULL COMMENT '模型id',
    `userId`      int(0)                                                  NULL DEFAULT NULL COMMENT '用户id',
    `chatData`    text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL COMMENT '用户聊天内容',
    `genResult`   text CHARACTER SET utf8 COLLATE utf8_general_ci         NULL COMMENT 'AI回复结果',
    `status`      varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '调用状态 wait/running/succeed/failed\r\n',
    `execMessage` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '执行信息',
    `createTime`  datetime(0)                                             NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updateTime`  datetime(0)                                             NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `isDelete`    tinyint(0)                                              NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for vip_user
-- ----------------------------
DROP TABLE IF EXISTS `vip_user`;
CREATE TABLE `vip_user`
(
    `id`         bigint(0)                                              NOT NULL COMMENT 'id',
    `userId`     bigint(0)                                              NULL DEFAULT NULL COMMENT '用户id',
    `vipType`    varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '会员类型   月度会员/年度会员',
    `expireTime` datetime(0)                                            NULL DEFAULT NULL COMMENT '到期时间',
    `createTime` datetime(0)                                            NULL DEFAULT CURRENT_TIMESTAMP(0),
    `updateTime` datetime(0)                                            NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0),
    `isDelete`   tinyint(0)                                             NULL DEFAULT 0,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
-- 帖子表
create table if not exists post
(
    id            bigint auto_increment comment 'id' primary key,
    age           int comment '年龄',
    gender        tinyint  default 0                 not null comment '性别（0-男, 1-女）',
    education     varchar(512)                       null comment '学历',
    place         varchar(512)                       null comment '地点',
    job           varchar(512)                       null comment '职业',
    contact       varchar(512)                       null comment '联系方式',
    loveExp       varchar(512)                       null comment '感情经历',
    content       text                               null comment '内容（个人介绍）',
    photo         varchar(1024)                      null comment '照片地址',
    reviewStatus  int      default 0                 not null comment '状态（0-待审核, 1-通过, 2-拒绝）',
    reviewMessage varchar(512)                       null comment '审核信息',
    viewNum       int                                not null default 0 comment '浏览数',
    thumbNum      int                                not null default 0 comment '点赞数',
    userId        bigint                             not null comment '创建用户 id',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete      tinyint  default 0                 not null comment '是否删除'
) comment '帖子';