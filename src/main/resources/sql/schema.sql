-- ===========================================
-- DI挪车小程序数据库建表脚本
-- Database: di_move_car
-- ===========================================

CREATE DATABASE IF NOT EXISTS di_move_car DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE di_move_car;

-- ===========================================
-- 1. 用户表
-- ===========================================
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `openid`          VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '微信openid',
    `unionid`         VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '微信unionid',
    `nickname`        VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '昵称',
    `avatar_url`      VARCHAR(512) NOT NULL DEFAULT '' COMMENT '头像URL',
    `phone`           VARCHAR(20)  NOT NULL DEFAULT '' COMMENT '手机号',
    `real_name`       VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '真实姓名',
    `id_card`         VARCHAR(32)  NOT NULL DEFAULT '' COMMENT '身份证号',
    `is_verified`     TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否实名认证：0-否，1-是',
    `status`          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `notify_count`    INT          NOT NULL DEFAULT 0 COMMENT '通知次数',
    `success_rate`    DECIMAL(5,2) NOT NULL DEFAULT 100.00 COMMENT '成功率(%)',
    `rating`          DECIMAL(2,1) NOT NULL DEFAULT 5.0 COMMENT '评分',
    `last_login_time` DATETIME     NULL COMMENT '最后登录时间',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_openid` (`openid`),
    UNIQUE KEY `uk_phone` (`phone`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ===========================================
-- 2. 车辆表
-- ===========================================
DROP TABLE IF EXISTS `user_vehicle`;
CREATE TABLE `user_vehicle` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '车辆ID',
    `user_id`         BIGINT       NOT NULL COMMENT '用户ID',
    `plate_number`    VARCHAR(16)  NOT NULL COMMENT '车牌号',
    `vehicle_type`    TINYINT      NOT NULL DEFAULT 1 COMMENT '车辆类型：1-小型汽车蓝牌，2-新能源，3-大型汽车',
    `brand`           VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '品牌',
    `model`           VARCHAR(64)  NOT NULL DEFAULT '' COMMENT '型号',
    `color`           VARCHAR(16)  NOT NULL DEFAULT '' COMMENT '颜色',
    `is_default`      TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否默认车辆：0-否，1-是',
    `qr_code_url`     VARCHAR(512) NOT NULL DEFAULT '' COMMENT '二维码URL',
    `status`          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_plate_number` (`plate_number`, `deleted`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='车辆表';

-- ===========================================
-- 3. 挪车通知记录表
-- ===========================================
DROP TABLE IF EXISTS `move_notification`;
CREATE TABLE `move_notification` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '通知ID',
    `initiator_id`    BIGINT       NOT NULL COMMENT '发起人用户ID',
    `target_user_id`  BIGINT       NOT NULL COMMENT '被通知车主用户ID',
    `target_vehicle_id` BIGINT     NOT NULL COMMENT '被通知车辆ID',
    `plate_number`    VARCHAR(16)  NOT NULL COMMENT '车牌号',
    `notify_type`     TINYINT      NOT NULL DEFAULT 1 COMMENT '通知类型：1-我发起的，2-我被通知的',
    `status`          TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：1-待处理，2-处理中，3-已完成，4-已取消',
    `location`        VARCHAR(255) NOT NULL DEFAULT '' COMMENT '位置描述',
    `latitude`        DECIMAL(10,7) NULL COMMENT '纬度',
    `longitude`       DECIMAL(10,7) NULL COMMENT '经度',
    `parking_info`    VARCHAR(128) NOT NULL DEFAULT '' COMMENT '停车位信息',
    `remark`          VARCHAR(512) NOT NULL DEFAULT '' COMMENT '备注',
    `image_urls`      VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '图片URL，多个逗号分隔',
    `notify_channel`  TINYINT      NOT NULL DEFAULT 0 COMMENT '通知渠道：bit0-短信，bit1-微信推送，bit2-小程序消息',
    `response_time`   INT          NOT NULL DEFAULT 0 COMMENT '响应时间（分钟）',
    `complete_time`   DATETIME     NULL COMMENT '完成时间',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`         TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
    PRIMARY KEY (`id`),
    KEY `idx_initiator_id` (`initiator_id`),
    KEY `idx_target_user_id` (`target_user_id`),
    KEY `idx_target_vehicle_id` (`target_vehicle_id`),
    KEY `idx_plate_number` (`plate_number`),
    KEY `idx_status` (`status`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挪车通知记录表';

-- ===========================================
-- 4. 通知进度表
-- ===========================================
DROP TABLE IF EXISTS `notification_progress`;
CREATE TABLE `notification_progress` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '进度ID',
    `notification_id` BIGINT       NOT NULL COMMENT '通知ID',
    `step`            TINYINT      NOT NULL COMMENT '步骤：1-发起通知，2-通知已送达，3-车主已读，4-车主响应，5-挪车完成',
    `title`           VARCHAR(64)  NOT NULL COMMENT '步骤标题',
    `description`     VARCHAR(255) NOT NULL DEFAULT '' COMMENT '步骤描述',
    `status`          TINYINT      NOT NULL DEFAULT 0 COMMENT '状态：0-未开始，1-进行中，2-已完成',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `complete_time`   DATETIME     NULL COMMENT '完成时间',
    PRIMARY KEY (`id`),
    KEY `idx_notification_id` (`notification_id`),
    KEY `idx_step` (`step`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='通知进度表';

-- ===========================================
-- 5. 用户设置表
-- ===========================================
DROP TABLE IF EXISTS `user_setting`;
CREATE TABLE `user_setting` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '设置ID',
    `user_id`         BIGINT       NOT NULL COMMENT '用户ID',
    `notify_enable`   TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '消息通知：1-开启，0-关闭',
    `sound_enable`    TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '声音提醒：1-开启，0-关闭',
    `vibrate_enable`  TINYINT(1)   NOT NULL DEFAULT 1 COMMENT '振动提醒：1-开启，0-关闭',
    `quiet_start`     VARCHAR(8)   NOT NULL DEFAULT '22:00' COMMENT '免打扰开始时间',
    `quiet_end`       VARCHAR(8)   NOT NULL DEFAULT '08:00' COMMENT '免打扰结束时间',
    `quiet_enable`    TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '免打扰：1-开启，0-关闭',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户设置表';

-- ===========================================
-- 6. 短信验证码表
-- ===========================================
DROP TABLE IF EXISTS `sms_code`;
CREATE TABLE `sms_code` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `phone`           VARCHAR(20)  NOT NULL COMMENT '手机号',
    `code`            VARCHAR(10)  NOT NULL COMMENT '验证码',
    `type`            TINYINT      NOT NULL DEFAULT 1 COMMENT '类型：1-登录，2-注册，3-修改手机号',
    `expire_time`     DATETIME     NOT NULL COMMENT '过期时间',
    `used`            TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否已使用：0-否，1-是',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_phone` (`phone`),
    KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短信验证码表';

-- ===========================================
-- 初始化数据
-- ===========================================

-- 初始化测试用户
INSERT INTO `sys_user` (`id`, `openid`, `nickname`, `phone`, `real_name`, `is_verified`, `notify_count`, `success_rate`, `rating`)
VALUES
(1, 'test_openid_001', '张先生', '13800008888', '张三', 1, 28, 96.00, 4.9),
(2, 'test_openid_002', '李女士', '13900006666', '李四', 1, 15, 92.00, 4.7);

-- 初始化测试车辆
INSERT INTO `user_vehicle` (`user_id`, `plate_number`, `vehicle_type`, `is_default`, `status`)
VALUES
(1, '京A·88888', 1, 1, 1),
(1, '沪B·66666', 2, 0, 1),
(2, '京C·12345', 1, 1, 1);

-- 初始化测试通知
INSERT INTO `move_notification`
    (`initiator_id`, `target_user_id`, `target_vehicle_id`, `plate_number`, `notify_type`, `status`, `location`, `parking_info`)
VALUES
(2, 1, 1, '京A·88888', 2, 1, '朝阳区望京SOHO B2层', 'B2-156'),
(1, 2, 3, '京C·12345', 1, 1, '朝阳区望京SOHO B2层', 'B2-156'),
(2, 1, 1, '京A·88888', 2, 3, '海淀区中关村软件园', '地下B1-088');

-- 初始化进度
INSERT INTO `notification_progress` (`notification_id`, `step`, `title`, `description`, `status`, `complete_time`)
VALUES
(1, 1, '发起通知', '用户发起挪车通知', 2, '2024-06-27 14:32:00'),
(1, 2, '通知已送达', '短信+推送已发送', 2, '2024-06-27 14:33:00'),
(1, 3, '等待车主响应', '车主正在处理', 1, NULL),
(2, 1, '发起通知', '用户发起挪车通知', 2, '2024-06-27 12:10:00'),
(2, 2, '通知已送达', '短信+推送已发送', 2, '2024-06-27 12:11:00'),
(2, 3, '等待车主响应', '进行中', 1, NULL);

-- 初始化用户设置
INSERT INTO `user_setting` (`user_id`, `notify_enable`, `sound_enable`, `vibrate_enable`, `quiet_enable`)
VALUES (1, 1, 1, 1, 0), (2, 1, 1, 1, 0);
