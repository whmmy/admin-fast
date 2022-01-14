/*
 Navicat Premium Data Transfer

 Source Server         : aliMIni
 Source Server Type    : MySQL
 Source Server Version : 50724

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 14/01/2022 15:38:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `USER_ID` bigint(16) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `PASSWORD` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `PHONE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `EMAIL` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `ROLE` tinyint(1) NOT NULL DEFAULT 1 COMMENT '用户角色（1：系统管理员；2：普通用户）',
  `TOKEN` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户token',
  `COMPANY_ID` bigint(16) NOT NULL DEFAULT 0,
  `REMARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `IS_DELETED` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（0：未删除；1：已删除）',
  `CREATE_ID` bigint(16) NOT NULL COMMENT '创建者ID',
  `UPDATE_ID` bigint(16) NOT NULL COMMENT '更新者ID',
  `CREATE_TIME` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '注册时间',
  `UPDATE_TIME` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`USER_ID`) USING BTREE,
  UNIQUE INDEX `UNIQUE_NAME`(`USER_NAME`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES (1, 'admin', 'f6fdffe48c908deb0f4c3bd36c032e72', NULL, 'admin', 1, '7354c689af554f53bd2d5f30607ac2e6', 0, NULL, 0, 1, 1, '2021-10-21 14:15:32', '2021-10-21 14:15:32');

-- ----------------------------
-- Table structure for tb_user_company
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_company`;
CREATE TABLE `tb_user_company`  (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `contact_phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `type` int(2) NULL DEFAULT NULL COMMENT '保留字段',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `isdelete` int(2) NOT NULL DEFAULT 0,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '鉴权token',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for tb_user_resource
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_resource`;
CREATE TABLE `tb_user_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源名称',
  `code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源CODE',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '资源分类ID',
  `type` int(2) NULL DEFAULT NULL,
  `sort` int(2) NULL DEFAULT NULL,
  `isdelete` int(2) NOT NULL DEFAULT 0,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_resource
-- ----------------------------
INSERT INTO `tb_user_resource` VALUES (1, NULL, '系统管理', 'systemManage', '系统管理', 0, 1, 0, 0, NULL);
INSERT INTO `tb_user_resource` VALUES (2, NULL, '用户管理', 'managerUser', '用户管理', 1, 2, 0, 0, NULL);
INSERT INTO `tb_user_resource` VALUES (3, NULL, '用户管理_更新权限', 'managerUserEdit', '用户管理_更新权限', 2, 3, 0, 0, NULL);
INSERT INTO `tb_user_resource` VALUES (4, NULL, '资源管理', 'managerResurce', '资源管理', 1, 2, 0, 0, NULL);
INSERT INTO `tb_user_resource` VALUES (5, NULL, '角色管理', 'managerRole', '角色管理', 1, 2, 0, 0, NULL);
INSERT INTO `tb_user_resource` VALUES (6, NULL, '公司管理', 'managerCompany', '公司管理', 1, 2, 0, 0, NULL);

-- ----------------------------
-- Table structure for tb_user_role
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role`;
CREATE TABLE `tb_user_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'role_code',
  `description` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述',
  `admin_count` int(11) NULL DEFAULT NULL COMMENT '后台用户数量',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `status` int(1) NULL DEFAULT 1 COMMENT '启用状态：0->禁用；1->启用',
  `sort` int(11) NULL DEFAULT 0,
  `company_id` bigint(16) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role
-- ----------------------------
INSERT INTO `tb_user_role` VALUES (1, 'admin', 'admin', 'admin', 1, '2022-01-12 10:06:34', 1, 0, 0);

-- ----------------------------
-- Table structure for tb_user_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role_relation`;
CREATE TABLE `tb_user_role_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NULL DEFAULT NULL,
  `role_id` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台用户和角色关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user_role_relation
-- ----------------------------
INSERT INTO `tb_user_role_relation` VALUES (1, 1, 1);

-- ----------------------------
-- Table structure for tb_user_role_resource_relation
-- ----------------------------
DROP TABLE IF EXISTS `tb_user_role_resource_relation`;
CREATE TABLE `tb_user_role_resource_relation`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) NULL DEFAULT NULL COMMENT '角色ID',
  `resource_id` bigint(20) NULL DEFAULT NULL COMMENT '资源ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '后台角色资源关系表' ROW_FORMAT = Dynamic;


SET FOREIGN_KEY_CHECKS = 1;
