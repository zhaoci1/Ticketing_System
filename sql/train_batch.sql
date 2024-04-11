/*
 Navicat Premium Data Transfer

 Source Server         : 远程train_batch
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : rm-7xv9s40yi6w07v136go.rwlb.rds.aliyuncs.com:3306
 Source Schema         : train_batch

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 11/04/2024 10:08:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `id` bigint NOT NULL COMMENT 'id',
  `mobile` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `mobile_unique`(`mobile` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of member
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器组',
  `BLOB_DATA` blob NULL COMMENT '数据',
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '日程名称',
  `CALENDAR` blob NOT NULL COMMENT '日程数据',
  PRIMARY KEY (`SCHED_NAME`, `CALENDAR_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器组',
  `CRON_EXPRESSION` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'cron表达式',
  `TIME_ZONE_ID` varchar(80) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '时区',
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('schedulerFactoryBean', 'com.jiawa.train.batch.job.DailyTrainJob', 'DEFAULT', '0/5 * * * * ?', 'Asia/Shanghai');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `ENTRY_ID` varchar(95) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'entryId',
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器组',
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '实例名称',
  `FIRED_TIME` bigint NOT NULL COMMENT '执行时间',
  `SCHED_TIME` bigint NOT NULL COMMENT '定时任务时间',
  `PRIORITY` int NOT NULL COMMENT '等级',
  `STATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '状态',
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'job名称',
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'job组',
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否异步',
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '是否请求覆盖',
  PRIMARY KEY (`SCHED_NAME`, `ENTRY_ID`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'job名称',
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'job组',
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `JOB_CLASS_NAME` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'job类名',
  `IS_DURABLE` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '是否持久化',
  `IS_NONCONCURRENT` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '是否非同步',
  `IS_UPDATE_DATA` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '是否更新数据',
  `REQUESTS_RECOVERY` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '请求是否覆盖',
  `JOB_DATA` blob NULL COMMENT 'job数据',
  PRIMARY KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('schedulerFactoryBean', 'com.jiawa.train.batch.job.DailyTrainJob', 'DEFAULT', NULL, 'com.jiawa.train.batch.job.DailyTrainJob', '0', '1', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `LOCK_NAME` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'lock名称',
  PRIMARY KEY (`SCHED_NAME`, `LOCK_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('schedulerFactoryBean', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器组',
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_GROUP`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `INSTANCE_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '实例名称',
  `LAST_CHECKIN_TIME` bigint NOT NULL COMMENT '最近检入时间',
  `CHECKIN_INTERVAL` bigint NOT NULL COMMENT '检入间隔',
  PRIMARY KEY (`SCHED_NAME`, `INSTANCE_NAME`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器组',
  `REPEAT_COUNT` bigint NOT NULL COMMENT '重复执行次数',
  `REPEAT_INTERVAL` bigint NOT NULL COMMENT '重复执行间隔',
  `TIMES_TRIGGERED` bigint NOT NULL COMMENT '已经触发次数',
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器组',
  `STR_PROP_1` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '开始配置1',
  `STR_PROP_2` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '开始配置2',
  `STR_PROP_3` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '开始配置3',
  `INT_PROP_1` int NULL DEFAULT NULL COMMENT 'int配置1',
  `INT_PROP_2` int NULL DEFAULT NULL COMMENT 'int配置2',
  `LONG_PROP_1` bigint NULL DEFAULT NULL COMMENT 'long配置1',
  `LONG_PROP_2` bigint NULL DEFAULT NULL COMMENT 'long配置2',
  `DEC_PROP_1` decimal(13, 4) NULL DEFAULT NULL COMMENT '配置描述1',
  `DEC_PROP_2` decimal(13, 4) NULL DEFAULT NULL COMMENT '配置描述2',
  `BOOL_PROP_1` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'bool配置1',
  `BOOL_PROP_2` varchar(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT 'bool配置2',
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers`  (
  `SCHED_NAME` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '定时任务名称',
  `TRIGGER_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器名称',
  `TRIGGER_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发器组',
  `JOB_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'job名称',
  `JOB_GROUP` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'job组',
  `DESCRIPTION` varchar(250) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '描述',
  `NEXT_FIRE_TIME` bigint NULL DEFAULT NULL COMMENT '下一次触发时间',
  `PREV_FIRE_TIME` bigint NULL DEFAULT NULL COMMENT '前一次触发时间',
  `PRIORITY` int NULL DEFAULT NULL COMMENT '等级',
  `TRIGGER_STATE` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发状态',
  `TRIGGER_TYPE` varchar(8) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '触发类型',
  `START_TIME` bigint NOT NULL COMMENT '开始时间',
  `END_TIME` bigint NULL DEFAULT NULL COMMENT '结束时间',
  `CALENDAR_NAME` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '日程名称',
  `MISFIRE_INSTR` smallint NULL DEFAULT NULL COMMENT '未触发实例',
  `JOB_DATA` blob NULL COMMENT 'job数据',
  PRIMARY KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) USING BTREE,
  INDEX `SCHED_NAME`(`SCHED_NAME` ASC, `JOB_NAME` ASC, `JOB_GROUP` ASC) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('schedulerFactoryBean', 'com.jiawa.train.batch.job.DailyTrainJob', 'DEFAULT', 'com.jiawa.train.batch.job.DailyTrainJob', 'DEFAULT', '生成每日车次', 1712066925000, 1712066921084, 5, 'PAUSED', 'CRON', 1710832983000, 0, NULL, 0, '');

SET FOREIGN_KEY_CHECKS = 1;
