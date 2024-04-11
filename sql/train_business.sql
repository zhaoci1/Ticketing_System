/*
 Navicat Premium Data Transfer

 Source Server         : 远程train_business
 Source Server Type    : MySQL
 Source Server Version : 80034
 Source Host           : rm-7xv9s40yi6w07v136go.rwlb.rds.aliyuncs.com:3306
 Source Schema         : train_business

 Target Server Type    : MySQL
 Target Server Version : 80034
 File Encoding         : 65001

 Date: 11/04/2024 10:08:02
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for confirm_order
-- ----------------------------
DROP TABLE IF EXISTS `confirm_order`;
CREATE TABLE `confirm_order`  (
  `id` bigint NOT NULL COMMENT 'id',
  `member_id` bigint NOT NULL COMMENT '会员id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站',
  `daily_train_ticket_id` bigint NOT NULL COMMENT '余票ID',
  `tickets` json NOT NULL COMMENT '车票',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单状态|枚举[ConfirmOrderStatusEnum]',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `date_train_code_index`(`date` ASC, `train_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '确认订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of confirm_order
-- ----------------------------
INSERT INTO `confirm_order` VALUES (1777594245666967552, 1766068816934211584, '2024-04-18', 'D1', '上海', '广州', 1777572894705061888, '[{\"passengerId\": 1766069025873465344, \"seatTypeCode\": \"1\", \"passengerName\": \"test\", \"passengerType\": \"1\", \"passengerIdCard\": \"123321\"}]', 'S', '2024-04-09 15:07:48.927', '2024-04-09 15:07:49.435');

-- ----------------------------
-- Table structure for daily_train
-- ----------------------------
DROP TABLE IF EXISTS `daily_train`;
CREATE TABLE `daily_train`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次类型|枚举[TrainTypeEnum]',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站',
  `start_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站拼音',
  `start_time` time NOT NULL COMMENT '出发时间',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站',
  `end_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站拼音',
  `end_time` time NOT NULL COMMENT '到站时间',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_code_unique`(`date` ASC, `code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日车次' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train
-- ----------------------------
INSERT INTO `daily_train` VALUES (1777572867387559936, '2024-04-18', 'G1', 'G', '武汉', 'wǔhàn', '00:00:13', '深圳', 'shēnzhèn', '00:00:07', '2024-04-09 13:42:51.949', '2024-04-09 13:42:51.949');
INSERT INTO `daily_train` VALUES (1777572883632099328, '2024-04-18', 'D1', 'G', '上海', 'shànghǎi', '06:00:00', '广州', 'guǎngzhōu', '07:00:00', '2024-04-09 13:42:55.822', '2024-04-09 13:42:55.822');

-- ----------------------------
-- Table structure for daily_train_carriage
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_carriage`;
CREATE TABLE `daily_train_carriage`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '箱序',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `seat_count` int NOT NULL COMMENT '座位数',
  `row_count` int NOT NULL COMMENT '排数',
  `col_count` int NOT NULL COMMENT '列数',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_index_unique`(`date` ASC, `train_code` ASC, `index` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日车厢' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_carriage
-- ----------------------------
INSERT INTO `daily_train_carriage` VALUES (1777572869006561280, '2024-04-18', 'G1', 1, '2', 25, 5, 5, '2024-04-09 13:42:52.335', '2024-04-09 13:42:52.335');
INSERT INTO `daily_train_carriage` VALUES (1777572869203693568, '2024-04-18', 'G1', 2, '1', 20, 5, 4, '2024-04-09 13:42:52.382', '2024-04-09 13:42:52.382');
INSERT INTO `daily_train_carriage` VALUES (1777572884827475968, '2024-04-18', 'D1', 1, '1', 20, 5, 4, '2024-04-09 13:42:56.107', '2024-04-09 13:42:56.107');
INSERT INTO `daily_train_carriage` VALUES (1777572885016219648, '2024-04-18', 'D1', 2, '2', 25, 5, 5, '2024-04-09 13:42:56.152', '2024-04-09 13:42:56.152');

-- ----------------------------
-- Table structure for daily_train_seat
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_seat`;
CREATE TABLE `daily_train_seat`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `carriage_index` int NOT NULL COMMENT '箱序',
  `row` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '排号|01, 02',
  `col` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列号|枚举[SeatColEnum]',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `carriage_seat_index` int NOT NULL COMMENT '同车箱座序',
  `sell` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '售卖情况|将经过的车站用01拼接，0表示可卖，1表示已卖',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日座位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_seat
-- ----------------------------
INSERT INTO `daily_train_seat` VALUES (1777572869824450560, '2024-04-18', 'G1', 1, '01', 'A', '2', 1, '000', '2024-04-09 13:42:52.530', '2024-04-09 13:42:52.530');
INSERT INTO `daily_train_seat` VALUES (1777572870013194240, '2024-04-18', 'G1', 1, '01', 'B', '2', 2, '000', '2024-04-09 13:42:52.575', '2024-04-09 13:42:52.575');
INSERT INTO `daily_train_seat` VALUES (1777572870206132224, '2024-04-18', 'G1', 1, '01', 'C', '2', 3, '000', '2024-04-09 13:42:52.620', '2024-04-09 13:42:52.620');
INSERT INTO `daily_train_seat` VALUES (1777572870394875904, '2024-04-18', 'G1', 1, '01', 'D', '2', 4, '000', '2024-04-09 13:42:52.666', '2024-04-09 13:42:52.666');
INSERT INTO `daily_train_seat` VALUES (1777572870587813888, '2024-04-18', 'G1', 1, '01', 'F', '2', 5, '000', '2024-04-09 13:42:52.712', '2024-04-09 13:42:52.712');
INSERT INTO `daily_train_seat` VALUES (1777572870780751872, '2024-04-18', 'G1', 1, '02', 'A', '2', 6, '000', '2024-04-09 13:42:52.758', '2024-04-09 13:42:52.758');
INSERT INTO `daily_train_seat` VALUES (1777572870973689856, '2024-04-18', 'G1', 1, '02', 'B', '2', 7, '000', '2024-04-09 13:42:52.804', '2024-04-09 13:42:52.804');
INSERT INTO `daily_train_seat` VALUES (1777572871166627840, '2024-04-18', 'G1', 1, '02', 'C', '2', 8, '000', '2024-04-09 13:42:52.850', '2024-04-09 13:42:52.850');
INSERT INTO `daily_train_seat` VALUES (1777572871367954432, '2024-04-18', 'G1', 1, '02', 'D', '2', 9, '000', '2024-04-09 13:42:52.898', '2024-04-09 13:42:52.898');
INSERT INTO `daily_train_seat` VALUES (1777572871556698112, '2024-04-18', 'G1', 1, '02', 'F', '2', 10, '000', '2024-04-09 13:42:52.943', '2024-04-09 13:42:52.943');
INSERT INTO `daily_train_seat` VALUES (1777572871753830400, '2024-04-18', 'G1', 1, '03', 'A', '2', 11, '000', '2024-04-09 13:42:52.990', '2024-04-09 13:42:52.990');
INSERT INTO `daily_train_seat` VALUES (1777572871950962688, '2024-04-18', 'G1', 1, '03', 'B', '2', 12, '000', '2024-04-09 13:42:53.037', '2024-04-09 13:42:53.037');
INSERT INTO `daily_train_seat` VALUES (1777572872143900672, '2024-04-18', 'G1', 1, '03', 'C', '2', 13, '000', '2024-04-09 13:42:53.083', '2024-04-09 13:42:53.083');
INSERT INTO `daily_train_seat` VALUES (1777572872336838656, '2024-04-18', 'G1', 1, '03', 'D', '2', 14, '000', '2024-04-09 13:42:53.129', '2024-04-09 13:42:53.129');
INSERT INTO `daily_train_seat` VALUES (1777572872538165248, '2024-04-18', 'G1', 1, '03', 'F', '2', 15, '000', '2024-04-09 13:42:53.177', '2024-04-09 13:42:53.177');
INSERT INTO `daily_train_seat` VALUES (1777572872722714624, '2024-04-18', 'G1', 1, '04', 'A', '2', 16, '000', '2024-04-09 13:42:53.221', '2024-04-09 13:42:53.221');
INSERT INTO `daily_train_seat` VALUES (1777572872915652608, '2024-04-18', 'G1', 1, '04', 'B', '2', 17, '000', '2024-04-09 13:42:53.267', '2024-04-09 13:42:53.267');
INSERT INTO `daily_train_seat` VALUES (1777572873108590592, '2024-04-18', 'G1', 1, '04', 'C', '2', 18, '000', '2024-04-09 13:42:53.313', '2024-04-09 13:42:53.313');
INSERT INTO `daily_train_seat` VALUES (1777572873297334272, '2024-04-18', 'G1', 1, '04', 'D', '2', 19, '000', '2024-04-09 13:42:53.358', '2024-04-09 13:42:53.358');
INSERT INTO `daily_train_seat` VALUES (1777572873502855168, '2024-04-18', 'G1', 1, '04', 'F', '2', 20, '000', '2024-04-09 13:42:53.406', '2024-04-09 13:42:53.406');
INSERT INTO `daily_train_seat` VALUES (1777572873691598848, '2024-04-18', 'G1', 1, '05', 'A', '2', 21, '000', '2024-04-09 13:42:53.452', '2024-04-09 13:42:53.452');
INSERT INTO `daily_train_seat` VALUES (1777572873892925440, '2024-04-18', 'G1', 1, '05', 'B', '2', 22, '000', '2024-04-09 13:42:53.499', '2024-04-09 13:42:53.499');
INSERT INTO `daily_train_seat` VALUES (1777572874085863424, '2024-04-18', 'G1', 1, '05', 'C', '2', 23, '000', '2024-04-09 13:42:53.546', '2024-04-09 13:42:53.546');
INSERT INTO `daily_train_seat` VALUES (1777572874278801408, '2024-04-18', 'G1', 1, '05', 'D', '2', 24, '000', '2024-04-09 13:42:53.592', '2024-04-09 13:42:53.592');
INSERT INTO `daily_train_seat` VALUES (1777572874475933696, '2024-04-18', 'G1', 1, '05', 'F', '2', 25, '000', '2024-04-09 13:42:53.639', '2024-04-09 13:42:53.639');
INSERT INTO `daily_train_seat` VALUES (1777572874668871680, '2024-04-18', 'G1', 2, '01', 'A', '1', 1, '000', '2024-04-09 13:42:53.685', '2024-04-09 13:42:53.685');
INSERT INTO `daily_train_seat` VALUES (1777572874866003968, '2024-04-18', 'G1', 2, '01', 'C', '1', 2, '000', '2024-04-09 13:42:53.732', '2024-04-09 13:42:53.732');
INSERT INTO `daily_train_seat` VALUES (1777572875058941952, '2024-04-18', 'G1', 2, '01', 'D', '1', 3, '000', '2024-04-09 13:42:53.778', '2024-04-09 13:42:53.778');
INSERT INTO `daily_train_seat` VALUES (1777572875260268544, '2024-04-18', 'G1', 2, '01', 'F', '1', 4, '000', '2024-04-09 13:42:53.826', '2024-04-09 13:42:53.826');
INSERT INTO `daily_train_seat` VALUES (1777572875453206528, '2024-04-18', 'G1', 2, '02', 'A', '1', 5, '000', '2024-04-09 13:42:53.872', '2024-04-09 13:42:53.872');
INSERT INTO `daily_train_seat` VALUES (1777572875646144512, '2024-04-18', 'G1', 2, '02', 'C', '1', 6, '000', '2024-04-09 13:42:53.918', '2024-04-09 13:42:53.918');
INSERT INTO `daily_train_seat` VALUES (1777572875847471104, '2024-04-18', 'G1', 2, '02', 'D', '1', 7, '000', '2024-04-09 13:42:53.966', '2024-04-09 13:42:53.966');
INSERT INTO `daily_train_seat` VALUES (1777572876040409088, '2024-04-18', 'G1', 2, '02', 'F', '1', 8, '000', '2024-04-09 13:42:54.012', '2024-04-09 13:42:54.012');
INSERT INTO `daily_train_seat` VALUES (1777572876233347072, '2024-04-18', 'G1', 2, '03', 'A', '1', 9, '000', '2024-04-09 13:42:54.057', '2024-04-09 13:42:54.057');
INSERT INTO `daily_train_seat` VALUES (1777572876426285056, '2024-04-18', 'G1', 2, '03', 'C', '1', 10, '000', '2024-04-09 13:42:54.104', '2024-04-09 13:42:54.104');
INSERT INTO `daily_train_seat` VALUES (1777572876623417344, '2024-04-18', 'G1', 2, '03', 'D', '1', 11, '000', '2024-04-09 13:42:54.151', '2024-04-09 13:42:54.151');
INSERT INTO `daily_train_seat` VALUES (1777572876816355328, '2024-04-18', 'G1', 2, '03', 'F', '1', 12, '000', '2024-04-09 13:42:54.197', '2024-04-09 13:42:54.197');
INSERT INTO `daily_train_seat` VALUES (1777572877013487616, '2024-04-18', 'G1', 2, '04', 'A', '1', 13, '000', '2024-04-09 13:42:54.244', '2024-04-09 13:42:54.244');
INSERT INTO `daily_train_seat` VALUES (1777572877206425600, '2024-04-18', 'G1', 2, '04', 'C', '1', 14, '000', '2024-04-09 13:42:54.290', '2024-04-09 13:42:54.290');
INSERT INTO `daily_train_seat` VALUES (1777572877399363584, '2024-04-18', 'G1', 2, '04', 'D', '1', 15, '000', '2024-04-09 13:42:54.336', '2024-04-09 13:42:54.336');
INSERT INTO `daily_train_seat` VALUES (1777572877583912960, '2024-04-18', 'G1', 2, '04', 'F', '1', 16, '000', '2024-04-09 13:42:54.380', '2024-04-09 13:42:54.380');
INSERT INTO `daily_train_seat` VALUES (1777572877772656640, '2024-04-18', 'G1', 2, '05', 'A', '1', 17, '000', '2024-04-09 13:42:54.425', '2024-04-09 13:42:54.425');
INSERT INTO `daily_train_seat` VALUES (1777572877965594624, '2024-04-18', 'G1', 2, '05', 'C', '1', 18, '000', '2024-04-09 13:42:54.471', '2024-04-09 13:42:54.471');
INSERT INTO `daily_train_seat` VALUES (1777572878158532608, '2024-04-18', 'G1', 2, '05', 'D', '1', 19, '000', '2024-04-09 13:42:54.517', '2024-04-09 13:42:54.517');
INSERT INTO `daily_train_seat` VALUES (1777572878355664896, '2024-04-18', 'G1', 2, '05', 'F', '1', 20, '000', '2024-04-09 13:42:54.564', '2024-04-09 13:42:54.564');
INSERT INTO `daily_train_seat` VALUES (1777572885636976640, '2024-04-18', 'D1', 1, '01', 'A', '1', 1, '1', '2024-04-09 13:42:56.300', '2024-04-09 15:07:49.209');
INSERT INTO `daily_train_seat` VALUES (1777572885829914624, '2024-04-18', 'D1', 1, '01', 'C', '1', 2, '0', '2024-04-09 13:42:56.346', '2024-04-09 13:42:56.346');
INSERT INTO `daily_train_seat` VALUES (1777572886022852608, '2024-04-18', 'D1', 1, '01', 'D', '1', 3, '0', '2024-04-09 13:42:56.392', '2024-04-09 13:42:56.392');
INSERT INTO `daily_train_seat` VALUES (1777572886211596288, '2024-04-18', 'D1', 1, '01', 'F', '1', 4, '0', '2024-04-09 13:42:56.437', '2024-04-09 13:42:56.437');
INSERT INTO `daily_train_seat` VALUES (1777572886396145664, '2024-04-18', 'D1', 1, '02', 'A', '1', 5, '0', '2024-04-09 13:42:56.481', '2024-04-09 13:42:56.481');
INSERT INTO `daily_train_seat` VALUES (1777572886589083648, '2024-04-18', 'D1', 1, '02', 'C', '1', 6, '0', '2024-04-09 13:42:56.527', '2024-04-09 13:42:56.527');
INSERT INTO `daily_train_seat` VALUES (1777572886782021632, '2024-04-18', 'D1', 1, '02', 'D', '1', 7, '0', '2024-04-09 13:42:56.573', '2024-04-09 13:42:56.573');
INSERT INTO `daily_train_seat` VALUES (1777572886974959616, '2024-04-18', 'D1', 1, '02', 'F', '1', 8, '0', '2024-04-09 13:42:56.619', '2024-04-09 13:42:56.619');
INSERT INTO `daily_train_seat` VALUES (1777572887167897600, '2024-04-18', 'D1', 1, '03', 'A', '1', 9, '0', '2024-04-09 13:42:56.665', '2024-04-09 13:42:56.665');
INSERT INTO `daily_train_seat` VALUES (1777572887365029888, '2024-04-18', 'D1', 1, '03', 'C', '1', 10, '0', '2024-04-09 13:42:56.711', '2024-04-09 13:42:56.711');
INSERT INTO `daily_train_seat` VALUES (1777572887562162176, '2024-04-18', 'D1', 1, '03', 'D', '1', 11, '0', '2024-04-09 13:42:56.759', '2024-04-09 13:42:56.759');
INSERT INTO `daily_train_seat` VALUES (1777572887755100160, '2024-04-18', 'D1', 1, '03', 'F', '1', 12, '0', '2024-04-09 13:42:56.805', '2024-04-09 13:42:56.805');
INSERT INTO `daily_train_seat` VALUES (1777572887943843840, '2024-04-18', 'D1', 1, '04', 'A', '1', 13, '0', '2024-04-09 13:42:56.850', '2024-04-09 13:42:56.850');
INSERT INTO `daily_train_seat` VALUES (1777572888140976128, '2024-04-18', 'D1', 1, '04', 'C', '1', 14, '0', '2024-04-09 13:42:56.896', '2024-04-09 13:42:56.896');
INSERT INTO `daily_train_seat` VALUES (1777572888329719808, '2024-04-18', 'D1', 1, '04', 'D', '1', 15, '0', '2024-04-09 13:42:56.942', '2024-04-09 13:42:56.942');
INSERT INTO `daily_train_seat` VALUES (1777572888531046400, '2024-04-18', 'D1', 1, '04', 'F', '1', 16, '0', '2024-04-09 13:42:56.990', '2024-04-09 13:42:56.990');
INSERT INTO `daily_train_seat` VALUES (1777572888723984384, '2024-04-18', 'D1', 1, '05', 'A', '1', 17, '0', '2024-04-09 13:42:57.036', '2024-04-09 13:42:57.036');
INSERT INTO `daily_train_seat` VALUES (1777572888921116672, '2024-04-18', 'D1', 1, '05', 'C', '1', 18, '0', '2024-04-09 13:42:57.083', '2024-04-09 13:42:57.083');
INSERT INTO `daily_train_seat` VALUES (1777572889118248960, '2024-04-18', 'D1', 1, '05', 'D', '1', 19, '0', '2024-04-09 13:42:57.129', '2024-04-09 13:42:57.129');
INSERT INTO `daily_train_seat` VALUES (1777572889306992640, '2024-04-18', 'D1', 1, '05', 'F', '1', 20, '0', '2024-04-09 13:42:57.175', '2024-04-09 13:42:57.175');
INSERT INTO `daily_train_seat` VALUES (1777572889504124928, '2024-04-18', 'D1', 2, '01', 'A', '2', 1, '0', '2024-04-09 13:42:57.222', '2024-04-09 13:42:57.222');
INSERT INTO `daily_train_seat` VALUES (1777572889688674304, '2024-04-18', 'D1', 2, '01', 'B', '2', 2, '0', '2024-04-09 13:42:57.266', '2024-04-09 13:42:57.266');
INSERT INTO `daily_train_seat` VALUES (1777572889881612288, '2024-04-18', 'D1', 2, '01', 'C', '2', 3, '0', '2024-04-09 13:42:57.312', '2024-04-09 13:42:57.312');
INSERT INTO `daily_train_seat` VALUES (1777572890074550272, '2024-04-18', 'D1', 2, '01', 'D', '2', 4, '0', '2024-04-09 13:42:57.358', '2024-04-09 13:42:57.358');
INSERT INTO `daily_train_seat` VALUES (1777572890267488256, '2024-04-18', 'D1', 2, '01', 'F', '2', 5, '0', '2024-04-09 13:42:57.404', '2024-04-09 13:42:57.404');
INSERT INTO `daily_train_seat` VALUES (1777572890456231936, '2024-04-18', 'D1', 2, '02', 'A', '2', 6, '0', '2024-04-09 13:42:57.449', '2024-04-09 13:42:57.449');
INSERT INTO `daily_train_seat` VALUES (1777572890649169920, '2024-04-18', 'D1', 2, '02', 'B', '2', 7, '0', '2024-04-09 13:42:57.495', '2024-04-09 13:42:57.495');
INSERT INTO `daily_train_seat` VALUES (1777572890846302208, '2024-04-18', 'D1', 2, '02', 'C', '2', 8, '0', '2024-04-09 13:42:57.542', '2024-04-09 13:42:57.542');
INSERT INTO `daily_train_seat` VALUES (1777572891035045888, '2024-04-18', 'D1', 2, '02', 'D', '2', 9, '0', '2024-04-09 13:42:57.587', '2024-04-09 13:42:57.587');
INSERT INTO `daily_train_seat` VALUES (1777572891227983872, '2024-04-18', 'D1', 2, '02', 'F', '2', 10, '0', '2024-04-09 13:42:57.632', '2024-04-09 13:42:57.632');
INSERT INTO `daily_train_seat` VALUES (1777572891425116160, '2024-04-18', 'D1', 2, '03', 'A', '2', 11, '0', '2024-04-09 13:42:57.680', '2024-04-09 13:42:57.680');
INSERT INTO `daily_train_seat` VALUES (1777572891613859840, '2024-04-18', 'D1', 2, '03', 'B', '2', 12, '0', '2024-04-09 13:42:57.725', '2024-04-09 13:42:57.725');
INSERT INTO `daily_train_seat` VALUES (1777572891806797824, '2024-04-18', 'D1', 2, '03', 'C', '2', 13, '0', '2024-04-09 13:42:57.771', '2024-04-09 13:42:57.771');
INSERT INTO `daily_train_seat` VALUES (1777572892003930112, '2024-04-18', 'D1', 2, '03', 'D', '2', 14, '0', '2024-04-09 13:42:57.817', '2024-04-09 13:42:57.817');
INSERT INTO `daily_train_seat` VALUES (1777572892196868096, '2024-04-18', 'D1', 2, '03', 'F', '2', 15, '0', '2024-04-09 13:42:57.864', '2024-04-09 13:42:57.864');
INSERT INTO `daily_train_seat` VALUES (1777572892398194688, '2024-04-18', 'D1', 2, '04', 'A', '2', 16, '0', '2024-04-09 13:42:57.912', '2024-04-09 13:42:57.912');
INSERT INTO `daily_train_seat` VALUES (1777572892595326976, '2024-04-18', 'D1', 2, '04', 'B', '2', 17, '0', '2024-04-09 13:42:57.958', '2024-04-09 13:42:57.958');
INSERT INTO `daily_train_seat` VALUES (1777572892788264960, '2024-04-18', 'D1', 2, '04', 'C', '2', 18, '0', '2024-04-09 13:42:58.005', '2024-04-09 13:42:58.005');
INSERT INTO `daily_train_seat` VALUES (1777572892981202944, '2024-04-18', 'D1', 2, '04', 'D', '2', 19, '0', '2024-04-09 13:42:58.051', '2024-04-09 13:42:58.051');
INSERT INTO `daily_train_seat` VALUES (1777572893174140928, '2024-04-18', 'D1', 2, '04', 'F', '2', 20, '0', '2024-04-09 13:42:58.097', '2024-04-09 13:42:58.097');
INSERT INTO `daily_train_seat` VALUES (1777572893371273216, '2024-04-18', 'D1', 2, '05', 'A', '2', 21, '0', '2024-04-09 13:42:58.144', '2024-04-09 13:42:58.144');
INSERT INTO `daily_train_seat` VALUES (1777572893555822592, '2024-04-18', 'D1', 2, '05', 'B', '2', 22, '0', '2024-04-09 13:42:58.188', '2024-04-09 13:42:58.188');
INSERT INTO `daily_train_seat` VALUES (1777572893744566272, '2024-04-18', 'D1', 2, '05', 'C', '2', 23, '0', '2024-04-09 13:42:58.233', '2024-04-09 13:42:58.233');
INSERT INTO `daily_train_seat` VALUES (1777572893937504256, '2024-04-18', 'D1', 2, '05', 'D', '2', 24, '0', '2024-04-09 13:42:58.278', '2024-04-09 13:42:58.278');
INSERT INTO `daily_train_seat` VALUES (1777572894130442240, '2024-04-18', 'D1', 2, '05', 'F', '2', 25, '0', '2024-04-09 13:42:58.325', '2024-04-09 13:42:58.325');

-- ----------------------------
-- Table structure for daily_train_station
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_station`;
CREATE TABLE `daily_train_station`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '站序',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名',
  `name_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音',
  `in_time` time NULL DEFAULT NULL COMMENT '进站时间',
  `out_time` time NULL DEFAULT NULL COMMENT '出站时间',
  `stop_time` time NULL DEFAULT NULL COMMENT '停站时长',
  `km` decimal(8, 2) NOT NULL COMMENT '里程（公里）|从上一站到本站的距离',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_index_unique`(`date` ASC, `train_code` ASC, `index` ASC) USING BTREE,
  UNIQUE INDEX `date_train_code_name_unique`(`date` ASC, `train_code` ASC, `name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '每日车站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_station
-- ----------------------------
INSERT INTO `daily_train_station` VALUES (1777572867903459328, '2024-04-18', 'G1', 0, '武汉', 'wǔhàn', '05:06:07', '07:00:06', '01:53:59', 123.00, '2024-04-09 13:42:52.072', '2024-04-09 13:42:52.072');
INSERT INTO `daily_train_station` VALUES (1777572868096397312, '2024-04-18', 'G1', 1, '上海', 'shànghǎi', '00:00:07', '00:00:04', '23:59:57', 12.00, '2024-04-09 13:42:52.118', '2024-04-09 13:42:52.118');
INSERT INTO `daily_train_station` VALUES (1777572868297723904, '2024-04-18', 'G1', 2, '广州', 'guǎngzhōu', '00:01:00', '01:00:00', '00:59:00', 12.00, '2024-04-09 13:42:52.166', '2024-04-09 13:42:52.166');
INSERT INTO `daily_train_station` VALUES (1777572868494856192, '2024-04-18', 'G1', 3, '深圳', 'shēnzhèn', '00:01:00', '00:03:00', '00:02:00', 123.00, '2024-04-09 13:42:52.213', '2024-04-09 13:42:52.213');
INSERT INTO `daily_train_station` VALUES (1777572884139610112, '2024-04-18', 'D1', 0, '上海', 'shànghǎi', '01:00:00', '04:00:00', '03:00:00', 17.00, '2024-04-09 13:42:55.943', '2024-04-09 13:42:55.943');
INSERT INTO `daily_train_station` VALUES (1777572884332548096, '2024-04-18', 'D1', 1, '广州', 'guǎngzhōu', '05:00:00', '11:00:00', '06:00:00', 11.00, '2024-04-09 13:42:55.989', '2024-04-09 13:42:55.989');

-- ----------------------------
-- Table structure for daily_train_ticket
-- ----------------------------
DROP TABLE IF EXISTS `daily_train_ticket`;
CREATE TABLE `daily_train_ticket`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站',
  `start_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '出发站拼音',
  `start_time` time NOT NULL COMMENT '出发时间',
  `start_index` int NOT NULL COMMENT '出发站序|本站是整个车次的第几站',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站',
  `end_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '到达站拼音',
  `end_time` time NOT NULL COMMENT '到站时间',
  `end_index` int NOT NULL COMMENT '到站站序|本站是整个车次的第几站',
  `ydz` int NOT NULL COMMENT '一等座余票',
  `ydz_price` decimal(8, 2) NOT NULL COMMENT '一等座票价',
  `edz` int NOT NULL COMMENT '二等座余票',
  `edz_price` decimal(8, 2) NOT NULL COMMENT '二等座票价',
  `rw` int NOT NULL COMMENT '软卧余票',
  `rw_price` decimal(8, 2) NOT NULL COMMENT '软卧票价',
  `yw` int NOT NULL COMMENT '硬卧余票',
  `yw_price` decimal(8, 2) NOT NULL COMMENT '硬卧票价',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_start_end_unique`(`date` ASC, `train_code` ASC, `start` ASC, `end` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '余票信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of daily_train_ticket
-- ----------------------------
INSERT INTO `daily_train_ticket` VALUES (1777572878984810496, '2024-04-18', 'G1', '武汉', 'wǔhàn', '07:00:06', 0, '上海', 'shànghǎi', '00:00:07', 1, 20, 5.76, 25, 4.32, -1, 8.64, -1, 7.20, '2024-04-09 13:42:54.714', '2024-04-09 13:42:54.714');
INSERT INTO `daily_train_ticket` VALUES (1777572879630733312, '2024-04-18', 'G1', '武汉', 'wǔhàn', '07:00:06', 0, '广州', 'guǎngzhōu', '00:01:00', 2, 20, 11.52, 25, 8.64, -1, 17.28, -1, 14.40, '2024-04-09 13:42:54.714', '2024-04-09 13:42:54.714');
INSERT INTO `daily_train_ticket` VALUES (1777572880238907392, '2024-04-18', 'G1', '武汉', 'wǔhàn', '07:00:06', 0, '深圳', 'shēnzhèn', '00:01:00', 3, 20, 70.56, 25, 52.92, -1, 105.84, -1, 88.20, '2024-04-09 13:42:54.714', '2024-04-09 13:42:54.714');
INSERT INTO `daily_train_ticket` VALUES (1777572880847081472, '2024-04-18', 'G1', '上海', 'shànghǎi', '00:00:04', 1, '广州', 'guǎngzhōu', '00:01:00', 2, 20, 5.76, 25, 4.32, -1, 8.64, -1, 7.20, '2024-04-09 13:42:54.714', '2024-04-09 13:42:54.714');
INSERT INTO `daily_train_ticket` VALUES (1777572881442672640, '2024-04-18', 'G1', '上海', 'shànghǎi', '00:00:04', 1, '深圳', 'shēnzhèn', '00:01:00', 3, 20, 64.80, 25, 48.60, -1, 97.20, -1, 81.00, '2024-04-09 13:42:54.714', '2024-04-09 13:42:54.714');
INSERT INTO `daily_train_ticket` VALUES (1777572882038263808, '2024-04-18', 'G1', '广州', 'guǎngzhōu', '01:00:00', 2, '深圳', 'shēnzhèn', '00:01:00', 3, 20, 59.04, 25, 44.28, -1, 88.56, -1, 73.80, '2024-04-09 13:42:54.714', '2024-04-09 13:42:54.714');
INSERT INTO `daily_train_ticket` VALUES (1777572894705061888, '2024-04-18', 'D1', '上海', 'shànghǎi', '04:00:00', 0, '广州', 'guǎngzhōu', '05:00:00', 1, 19, 5.28, 25, 3.96, -1, 7.92, -1, 6.60, '2024-04-09 13:42:58.462', '2024-04-09 13:42:58.462');

-- ----------------------------
-- Table structure for sk_token
-- ----------------------------
DROP TABLE IF EXISTS `sk_token`;
CREATE TABLE `sk_token`  (
  `id` bigint NOT NULL COMMENT 'id',
  `date` date NOT NULL COMMENT '日期',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `count` int NOT NULL COMMENT '令牌余量',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `date_train_code_unique`(`date` ASC, `train_code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '秒杀令牌' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sk_token
-- ----------------------------
INSERT INTO `sk_token` VALUES (1777572883040702464, '2024-04-18', 'G1', 180, '2024-04-09 13:42:55.681', '2024-04-09 13:42:55.681');
INSERT INTO `sk_token` VALUES (1777572895678140416, '2024-04-18', 'D1', 90, '2024-04-09 13:42:58.694', '2024-04-09 13:42:58.694');

-- ----------------------------
-- Table structure for station
-- ----------------------------
DROP TABLE IF EXISTS `station`;
CREATE TABLE `station`  (
  `id` bigint NOT NULL COMMENT 'id',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名',
  `name_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音',
  `name_py` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音首字母',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of station
-- ----------------------------
INSERT INTO `station` VALUES (1769253458579427328, '广州', 'guǎngzhōu', 'gz', '2024-03-17 14:44:30.228', '2024-03-17 14:44:30.228');
INSERT INTO `station` VALUES (1769253477214720000, '上海', 'shànghǎi', 'sh', '2024-03-17 14:44:34.711', '2024-03-17 14:44:34.711');
INSERT INTO `station` VALUES (1769253510794317824, '深圳', 'shēnzhèn', 'sz', '2024-03-17 14:44:42.718', '2024-03-17 14:44:42.718');
INSERT INTO `station` VALUES (1769253523855380480, '武汉', 'wǔhàn', 'wh', '2024-03-17 14:44:45.831', '2024-03-17 14:44:45.831');

-- ----------------------------
-- Table structure for train
-- ----------------------------
DROP TABLE IF EXISTS `train`;
CREATE TABLE `train`  (
  `id` bigint NOT NULL COMMENT 'id',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次类型|枚举[TrainTypeEnum]',
  `start` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站',
  `start_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '始发站拼音',
  `start_time` time NOT NULL COMMENT '出发时间',
  `end` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站',
  `end_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '终点站拼音',
  `end_time` time NOT NULL COMMENT '到站时间',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `code_unique`(`code` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '车次' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train
-- ----------------------------
INSERT INTO `train` VALUES (1769253639064522752, 'G1', 'G', '武汉', 'wǔhàn', '00:00:13', '深圳', 'shēnzhèn', '00:00:07', '2024-03-17 14:45:13.298', '2024-03-17 14:45:13.298');
INSERT INTO `train` VALUES (1769714357844971520, 'D1', 'G', '上海', 'shànghǎi', '06:00:00', '广州', 'guǎngzhōu', '07:00:00', '2024-03-18 21:15:57.221', '2024-03-18 21:15:57.221');

-- ----------------------------
-- Table structure for train_carriage
-- ----------------------------
DROP TABLE IF EXISTS `train_carriage`;
CREATE TABLE `train_carriage`  (
  `id` bigint NOT NULL COMMENT 'id',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '厢号',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `seat_count` int NOT NULL COMMENT '座位数',
  `row_count` int NOT NULL COMMENT '排数',
  `col_count` int NOT NULL COMMENT '列数',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `train_code_index_unique`(`train_code` ASC, `index` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '火车车厢' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train_carriage
-- ----------------------------
INSERT INTO `train_carriage` VALUES (1769254410149564416, 'G1', 1, '2', 25, 5, 5, '2024-03-17 14:48:17.000', '2024-04-09 13:42:21.608');
INSERT INTO `train_carriage` VALUES (1769254576571158528, 'G1', 2, '1', 20, 5, 4, '2024-03-17 14:48:56.000', '2024-04-09 13:42:24.149');
INSERT INTO `train_carriage` VALUES (1770410625281953792, 'D1', 1, '1', 20, 5, 4, '2024-03-20 19:22:40.000', '2024-04-09 13:41:32.153');
INSERT INTO `train_carriage` VALUES (1772169152577015808, 'D1', 2, '2', 25, 5, 5, '2024-03-25 15:50:25.000', '2024-04-09 13:41:36.423');

-- ----------------------------
-- Table structure for train_seat
-- ----------------------------
DROP TABLE IF EXISTS `train_seat`;
CREATE TABLE `train_seat`  (
  `id` bigint NOT NULL COMMENT 'id',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `carriage_index` int NOT NULL COMMENT '厢序',
  `row` char(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '排号|01, 02',
  `col` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '列号|枚举[SeatColEnum]',
  `seat_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '座位类型|枚举[SeatTypeEnum]',
  `carriage_seat_index` int NOT NULL COMMENT '同车厢座序',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '座位' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train_seat
-- ----------------------------
INSERT INTO `train_seat` VALUES (1777572782700367872, 'G1', 1, '01', 'A', '2', 1, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572782893305856, 'G1', 1, '01', 'B', '2', 2, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572783077855232, 'G1', 1, '01', 'C', '2', 3, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572783270793216, 'G1', 1, '01', 'D', '2', 4, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572783476314112, 'G1', 1, '01', 'F', '2', 5, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572783669252096, 'G1', 1, '02', 'A', '2', 6, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572783862190080, 'G1', 1, '02', 'B', '2', 7, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572784046739456, 'G1', 1, '02', 'C', '2', 8, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572784243871744, 'G1', 1, '02', 'D', '2', 9, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572784436809728, 'G1', 1, '02', 'F', '2', 10, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572784625553408, 'G1', 1, '03', 'A', '2', 11, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572784818491392, 'G1', 1, '03', 'B', '2', 12, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572785007235072, 'G1', 1, '03', 'C', '2', 13, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572785195978752, 'G1', 1, '03', 'D', '2', 14, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572785388916736, 'G1', 1, '03', 'F', '2', 15, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572785577660416, 'G1', 1, '04', 'A', '2', 16, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572785770598400, 'G1', 1, '04', 'B', '2', 17, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572785955147776, 'G1', 1, '04', 'C', '2', 18, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572786148085760, 'G1', 1, '04', 'D', '2', 19, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572786345218048, 'G1', 1, '04', 'F', '2', 20, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572786538156032, 'G1', 1, '05', 'A', '2', 21, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572786735288320, 'G1', 1, '05', 'B', '2', 22, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572786924032000, 'G1', 1, '05', 'C', '2', 23, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572787108581376, 'G1', 1, '05', 'D', '2', 24, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572787301519360, 'G1', 1, '05', 'F', '2', 25, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572787494457344, 'G1', 2, '01', 'A', '1', 1, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572787679006720, 'G1', 2, '01', 'C', '1', 2, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572787871944704, 'G1', 2, '01', 'D', '1', 3, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572788060688384, 'G1', 2, '01', 'F', '1', 4, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572788257820672, 'G1', 2, '02', 'A', '1', 5, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572788450758656, 'G1', 2, '02', 'C', '1', 6, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572788639502336, 'G1', 2, '02', 'D', '1', 7, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572788836634624, 'G1', 2, '02', 'F', '1', 8, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572789025378304, 'G1', 2, '03', 'A', '1', 9, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572789209927680, 'G1', 2, '03', 'C', '1', 10, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572789407059968, 'G1', 2, '03', 'D', '1', 11, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572789591609344, 'G1', 2, '03', 'F', '1', 12, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572789784547328, 'G1', 2, '04', 'A', '1', 13, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572789973291008, 'G1', 2, '04', 'C', '1', 14, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572790170423296, 'G1', 2, '04', 'D', '1', 15, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572790359166976, 'G1', 2, '04', 'F', '1', 16, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572790547910656, 'G1', 2, '05', 'A', '1', 17, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572790740848640, 'G1', 2, '05', 'C', '1', 18, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572790937980928, 'G1', 2, '05', 'D', '1', 19, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572791130918912, 'G1', 2, '05', 'F', '1', 20, '2024-04-09 13:42:31.667', '2024-04-09 13:42:31.667');
INSERT INTO `train_seat` VALUES (1777572801050447872, 'D1', 1, '01', 'A', '1', 1, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572801230802944, 'D1', 1, '01', 'C', '1', 2, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572801427935232, 'D1', 1, '01', 'D', '1', 3, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572801620873216, 'D1', 1, '01', 'F', '1', 4, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572801813811200, 'D1', 1, '02', 'A', '1', 5, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572802006749184, 'D1', 1, '02', 'C', '1', 6, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572802199687168, 'D1', 1, '02', 'D', '1', 7, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572802392625152, 'D1', 1, '02', 'F', '1', 8, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572802585563136, 'D1', 1, '03', 'A', '1', 9, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572802770112512, 'D1', 1, '03', 'C', '1', 10, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572802958856192, 'D1', 1, '03', 'D', '1', 11, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572803147599872, 'D1', 1, '03', 'F', '1', 12, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572803336343552, 'D1', 1, '04', 'A', '1', 13, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572803529281536, 'D1', 1, '04', 'C', '1', 14, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572803722219520, 'D1', 1, '04', 'D', '1', 15, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572803915157504, 'D1', 1, '04', 'F', '1', 16, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572804108095488, 'D1', 1, '05', 'A', '1', 17, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572804301033472, 'D1', 1, '05', 'C', '1', 18, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572804489777152, 'D1', 1, '05', 'D', '1', 19, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572804682715136, 'D1', 1, '05', 'F', '1', 20, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572804871458816, 'D1', 2, '01', 'A', '2', 1, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572805068591104, 'D1', 2, '01', 'B', '2', 2, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572805261529088, 'D1', 2, '01', 'C', '2', 3, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572805446078464, 'D1', 2, '01', 'D', '2', 4, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572805630627840, 'D1', 2, '01', 'F', '2', 5, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572805819371520, 'D1', 2, '02', 'A', '2', 6, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572806003920896, 'D1', 2, '02', 'B', '2', 7, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572806192664576, 'D1', 2, '02', 'C', '2', 8, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572806385602560, 'D1', 2, '02', 'D', '2', 9, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572806578540544, 'D1', 2, '02', 'F', '2', 10, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572806775672832, 'D1', 2, '03', 'A', '2', 11, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572806964416512, 'D1', 2, '03', 'B', '2', 12, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572807161548800, 'D1', 2, '03', 'C', '2', 13, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572807350292480, 'D1', 2, '03', 'D', '2', 14, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572807543230464, 'D1', 2, '03', 'F', '2', 15, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572807736168448, 'D1', 2, '04', 'A', '2', 16, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572807920717824, 'D1', 2, '04', 'B', '2', 17, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572808117850112, 'D1', 2, '04', 'C', '2', 18, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572808319176704, 'D1', 2, '04', 'D', '2', 19, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572808503726080, 'D1', 2, '04', 'F', '2', 20, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572808696664064, 'D1', 2, '05', 'A', '2', 21, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572808889602048, 'D1', 2, '05', 'B', '2', 22, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572809082540032, 'D1', 2, '05', 'C', '2', 23, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572809275478016, 'D1', 2, '05', 'D', '2', 24, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');
INSERT INTO `train_seat` VALUES (1777572809476804608, 'D1', 2, '05', 'F', '2', 25, '2024-04-09 13:42:36.038', '2024-04-09 13:42:36.038');

-- ----------------------------
-- Table structure for train_station
-- ----------------------------
DROP TABLE IF EXISTS `train_station`;
CREATE TABLE `train_station`  (
  `id` bigint NOT NULL COMMENT 'id',
  `train_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '车次编号',
  `index` int NOT NULL COMMENT '站序',
  `name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名',
  `name_pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '站名拼音',
  `in_time` time NULL DEFAULT NULL COMMENT '进站时间',
  `out_time` time NULL DEFAULT NULL COMMENT '出站时间',
  `stop_time` time NULL DEFAULT NULL COMMENT '停站时长',
  `km` decimal(8, 2) NOT NULL COMMENT '里程（公里）|从上一站到本站的距离',
  `create_time` datetime(3) NULL DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime(3) NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `train_code_index_unique`(`train_code` ASC, `index` ASC) USING BTREE,
  UNIQUE INDEX `train_code_name_unique`(`train_code` ASC, `name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '火车车站' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of train_station
-- ----------------------------
INSERT INTO `train_station` VALUES (1769253988588457984, 'G1', 0, '武汉', 'wǔhàn', '05:06:07', '07:00:06', '01:53:59', 123.00, '2024-03-17 14:46:36.000', '2024-03-27 17:03:37.882');
INSERT INTO `train_station` VALUES (1769254184986742784, 'G1', 1, '上海', 'shànghǎi', '00:00:07', '00:00:04', '23:59:57', 12.00, '2024-03-17 14:47:23.000', '2024-03-27 17:03:41.220');
INSERT INTO `train_station` VALUES (1770055244517085184, 'D1', 0, '上海', 'shànghǎi', '01:00:00', '04:00:00', '03:00:00', 17.00, '2024-03-19 19:50:30.000', '2024-04-02 23:06:14.372');
INSERT INTO `train_station` VALUES (1770055366562942976, 'D1', 1, '广州', 'guǎngzhōu', '05:00:00', '11:00:00', '06:00:00', 11.00, '2024-03-19 19:51:00.000', '2024-04-02 23:06:17.246');
INSERT INTO `train_station` VALUES (1770340465208594432, 'G1', 3, '深圳', 'shēnzhèn', '00:01:00', '00:03:00', '00:02:00', 123.00, '2024-03-20 14:43:52.000', '2024-03-27 17:04:00.490');
INSERT INTO `train_station` VALUES (1772912525214617600, 'G1', 2, '广州', 'guǎngzhōu', '00:01:00', '01:00:00', '00:59:00', 12.00, '2024-03-27 17:04:19.712', '2024-03-27 17:04:19.712');

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
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of undo_log
-- ----------------------------
INSERT INTO `undo_log` VALUES (28, 9513189954978075, '192.168.1.77:8091:9513189954978072', 'serializer=jackson&compressorType=NONE', 0x7B7D, 1, '2024-04-01 18:55:22', '2024-04-01 18:55:22', NULL);

SET FOREIGN_KEY_CHECKS = 1;
