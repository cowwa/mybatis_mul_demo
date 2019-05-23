/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : localhost:3306
 Source Schema         : tenancy_sys_db

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : 65001

 Date: 23/05/2019 13:19:43
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tenant_param
-- ----------------------------
DROP TABLE IF EXISTS `tenant_param`;
CREATE TABLE `tenant_param`  (
  `id` bigint(20) NOT NULL,
  `tenant_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户名称',
  `tenant_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户码',
  `tenant_db_ip` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户数据库ip',
  `tenant_db_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户数据库名称',
  `tenant_db_account` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户数据库账号',
  `tenant_db_psw` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '租户数据库密码',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tenant_param
-- ----------------------------
INSERT INTO `tenant_param` VALUES (0, 'D租户', 'D', '127.0.0.1', 'd_db', 'root', '123456');
INSERT INTO `tenant_param` VALUES (1, 'A租户', 'A', '127.0.0.1', 'a_db', 'root', '123456');
INSERT INTO `tenant_param` VALUES (2, 'B租户', 'B', '127.0.0.1', 'b_db', 'root', '123456');
INSERT INTO `tenant_param` VALUES (3, 'C租户', 'C', '127.0.0.1', 'c_db', 'root', '123456');
INSERT INTO `tenant_param` VALUES (4, 'E租户', 'E', '127.0.0.1', 'e_db', 'root', '123456');

SET FOREIGN_KEY_CHECKS = 1;
