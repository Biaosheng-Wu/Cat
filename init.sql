-- =============================================
-- 校园流浪猫图鉴 - MySQL 建库建表脚本
-- 使用方式：
--   1. 用 root 登录 MySQL：mysql -u root -p
--   2. 执行：source 这个文件路径
--   3. 或者复制粘贴到 Navicat / DBeaver / Workbench 中执行
-- =============================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS cat_care
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE cat_care;

-- =============================================
-- 猫咪档案表
-- =============================================
CREATE TABLE IF NOT EXISTS t_cat (
    id                  BIGINT          AUTO_INCREMENT  PRIMARY KEY     COMMENT '猫咪ID',
    name                VARCHAR(100)    DEFAULT NULL                    COMMENT '昵称',
    avatar_url          VARCHAR(500)    DEFAULT NULL                    COMMENT '头像URL',
    breed               VARCHAR(100)    DEFAULT NULL                    COMMENT '品种',
    gender              VARCHAR(20)     DEFAULT NULL                    COMMENT '性别: MALE/FEMALE/UNKNOWN',
    age_estimate        VARCHAR(50)     DEFAULT NULL                    COMMENT '预估年龄',
    color               VARCHAR(50)     DEFAULT NULL                    COMMENT '毛色',
    appearance_features VARCHAR(500)    DEFAULT NULL                    COMMENT '外貌特征',
    tnr_status          INT             DEFAULT 0                       COMMENT 'TNR状态: 0-未绝育 1-已绝育 2-已剪耳标',
    frequent_locations  VARCHAR(500)    DEFAULT NULL                    COMMENT '常出没地点',
    default_latitude    DECIMAL(10,7)   DEFAULT NULL                    COMMENT '默认纬度',
    default_longitude   DECIMAL(10,7)   DEFAULT NULL                    COMMENT '默认经度',
    health_status       VARCHAR(50)     DEFAULT NULL                    COMMENT '健康状态: HEALTHY/SICK/INJURED',
    creator_id          BIGINT          DEFAULT NULL                    COMMENT '创建者用户ID',
    create_time         DATETIME        DEFAULT NULL                    COMMENT '创建时间',
    update_time         DATETIME        DEFAULT NULL                    COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='猫咪档案表';

-- =============================================
-- 健康异常上报表
-- =============================================
CREATE TABLE IF NOT EXISTS t_health_report (
    id              BIGINT          AUTO_INCREMENT  PRIMARY KEY     COMMENT '上报ID',
    cat_id          BIGINT          DEFAULT NULL                    COMMENT '猫咪ID',
    reporter_id     BIGINT          DEFAULT NULL                    COMMENT '上报者用户ID',
    report_type     VARCHAR(50)     DEFAULT NULL                    COMMENT '类型: SICK/INJURED/THIN/OTHER',
    description     VARCHAR(1000)   DEFAULT NULL                    COMMENT '异常描述',
    image_urls      VARCHAR(2000)   DEFAULT NULL                    COMMENT '图片URL列表(逗号分隔)',
    latitude        DECIMAL(10,7)   DEFAULT NULL                    COMMENT '发现地点纬度',
    longitude       DECIMAL(10,7)   DEFAULT NULL                    COMMENT '发现地点经度',
    status          INT             DEFAULT 0                       COMMENT '处理状态: 0-待处理 1-处理中 2-已完成',
    handler_id      BIGINT          DEFAULT NULL                    COMMENT '处理人ID',
    handle_remark   VARCHAR(1000)   DEFAULT NULL                    COMMENT '处理备注',
    report_time     DATETIME        DEFAULT NULL                    COMMENT '上报时间',
    handle_time     DATETIME        DEFAULT NULL                    COMMENT '处理时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健康异常上报表';

-- =============================================
-- 投喂打卡记录表
-- =============================================
CREATE TABLE IF NOT EXISTS t_feed_record (
    id              BIGINT          AUTO_INCREMENT  PRIMARY KEY     COMMENT '打卡ID',
    cat_id          BIGINT          DEFAULT NULL                    COMMENT '猫咪ID',
    user_id         BIGINT          DEFAULT NULL                    COMMENT '投喂用户ID',
    remark          VARCHAR(500)    DEFAULT NULL                    COMMENT '投喂备注(投喂了什么食物)',
    latitude        DECIMAL(10,7)   DEFAULT NULL                    COMMENT '投喂纬度',
    longitude       DECIMAL(10,7)   DEFAULT NULL                    COMMENT '投喂经度',
    location_name   VARCHAR(500)    DEFAULT NULL                    COMMENT '投喂地点名称',
    feed_time       DATETIME        DEFAULT NULL                    COMMENT '投喂时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='投喂打卡记录表';

-- =============================================
-- RBAC 权限体系：用户表
-- =============================================
CREATE TABLE IF NOT EXISTS t_user (
    id              BIGINT          AUTO_INCREMENT  PRIMARY KEY     COMMENT '用户ID',
    username        VARCHAR(50)     NOT NULL UNIQUE                 COMMENT '用户名',
    password        VARCHAR(200)    NOT NULL                        COMMENT '密码(BCrypt加密)',
    nickname        VARCHAR(50)     DEFAULT NULL                    COMMENT '昵称',
    phone           VARCHAR(20)     DEFAULT NULL                    COMMENT '手机号',
    avatar_url      VARCHAR(500)    DEFAULT NULL                    COMMENT '头像URL',
    enabled         TINYINT(1)      DEFAULT 1                       COMMENT '是否启用: 0-禁用 1-启用',
    create_time     DATETIME        DEFAULT NULL                    COMMENT '注册时间',
    update_time     DATETIME        DEFAULT NULL                    COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- RBAC 权限体系：角色表
-- =============================================
CREATE TABLE IF NOT EXISTS t_role (
    id              BIGINT          AUTO_INCREMENT  PRIMARY KEY     COMMENT '角色ID',
    role_code       VARCHAR(50)     NOT NULL UNIQUE                 COMMENT '角色编码: ROLE_ADMIN / ROLE_VOLUNTEER',
    role_name       VARCHAR(50)     NOT NULL                        COMMENT '角色名称: 管理员 / 志愿者',
    description     VARCHAR(200)    DEFAULT NULL                    COMMENT '角色描述'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- =============================================
-- RBAC 权限体系：用户-角色关联表
-- =============================================
CREATE TABLE IF NOT EXISTS t_user_role (
    id              BIGINT          AUTO_INCREMENT  PRIMARY KEY     COMMENT '关联ID',
    user_id         BIGINT          NOT NULL                        COMMENT '用户ID',
    role_id         BIGINT          NOT NULL                        COMMENT '角色ID',
    UNIQUE KEY uk_user_role (user_id, role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 初始化角色数据
-- =============================================
INSERT INTO t_role (role_code, role_name, description) VALUES
('ROLE_ADMIN',     '管理员', '拥有全部权限，包括删除和管理'),
('ROLE_VOLUNTEER', '志愿者', '普通用户，可投喂、上报、查看猫咪')
ON DUPLICATE KEY UPDATE role_name = VALUES(role_name);

-- =============================================
-- 初始化测试账号（由 Spring DataInitializer 创建）
-- 账号: admin / 密码: 123456  角色: 管理员
-- 账号: volun / 密码: 123456  角色: 志愿者
-- =============================================
