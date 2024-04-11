/*
 Navicat Premium Data Transfer

 Source Server         : 远程train_member
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : rm-7xv9s40yi6w07v136go.rwlb.rds.aliyuncs.com:3306
 Source Schema         : train_member

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 11/04/2024 10:08:18
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
INSERT INTO `member` VALUES (1770788012494557184, '10000000000');
INSERT INTO `member` VALUES (1773547796276842496, '10203050601');
INSERT INTO `member` VALUES (1766068816934211584, '12345678909');
INSERT INTO `member` VALUES (1770786344575045632, '19999999999');

-- ----------------------------
-- Table structure for passenger
-- ----------------------------
DROP TABLE IF EXISTS `passenger`;
CREATE TABLE `passenger`  (
  `id` bigint NOT NULL COMMENT 'id',
  `member_id` bigint NOT NULL COMMENT '会员id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '姓名',
  `id_card` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '身份证',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '旅客类型|枚举[PassengerTypeEnum]',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_id_index`(`member_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '乘车人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of passenger
-- ----------------------------
INSERT INTO `passenger` VALUES (1766069025873465344, 1766068816934211584, 'test', '123321', '1', '2024-03-08 19:50:42.401', '2024-03-08 19:50:42.401');
INSERT INTO `passenger` VALUES (1766301718666153984, 1766068816934211584, 'test', '123321', '1', '2024-03-09 11:15:20.639', '2024-03-09 11:15:20.639');
INSERT INTO `passenger` VALUES (1766707928997433344, 1, '34', '3434531243115', '1', '2024-03-10 14:09:28.768', '2024-03-10 14:09:28.768');
INSERT INTO `passenger` VALUES (1767105676838047744, 1766068816934211584, 'tom', '34534342424', '1', '2024-03-11 16:29:59.232', '2024-03-11 16:29:59.232');
INSERT INTO `passenger` VALUES (1767159510507589632, 1770786344575045632, 'cat22xx', '12321312312', '2', '2024-03-11 20:03:54.000', '2024-03-14 15:29:48.347');
INSERT INTO `passenger` VALUES (1770789521751937024, 1770788012494557184, 'cs', '1234214134', '1', '2024-03-21 20:28:16.319', '2024-03-21 20:28:16.319');
INSERT INTO `passenger` VALUES (1772152126353575936, 1766068816934211584, 'cs', '23423423', '1', '2024-03-25 14:42:46.569', '2024-03-25 14:42:46.569');
INSERT INTO `passenger` VALUES (1772152147635474432, 1766068816934211584, 'cs2', 'sdfdsfsdf', '2', '2024-03-25 14:42:51.648', '2024-03-25 14:42:51.648');
INSERT INTO `passenger` VALUES (1773547940770615296, 1773547796276842496, 'zzzzz', 'dfdfd', '2', '2024-03-29 11:09:14.664', '2024-03-29 11:09:14.664');

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket`  (
  `id` bigint NOT NULL COMMENT 'id',
  `member_id` bigint NOT NULL COMMENT '会员id',
  `passenger_id` bigint NOT NULL COMMENT '乘客id',
  `passenger_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '乘客姓名',
  `train_date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `carriage_index` int NOT NULL COMMENT '箱序',
  `seat_row` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '排号|01, 02',
  `seat_col` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列号|枚举[SeatColEnum]',
  `start_station` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站',
  `start_time` time NOT NULL COMMENT '出发时间',
  `end_station` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站',
  `end_time` time NOT NULL COMMENT '到站时间',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `member_id_index`(`member_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车票' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES (1777594247449546752, 1766068816934211584, 1766069025873465344, 'test', '2024-04-18', 'D1', 1, '01', 'A', '上海', '04:00:00', '广州', '05:00:00', '1', '2024-04-09 15:07:49.353', '2024-04-09 15:07:49.353');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `branch_id` bigint NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int NOT NULL,
  `log_created` datetime NOT NULL,
  `log_modified` datetime NOT NULL,
  `ext` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
