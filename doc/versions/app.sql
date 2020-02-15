/*
 Navicat Premium Data Transfer

 Source Server         : weima
 Source Server Type    : MySQL
 Source Server Version : 50718
 Source Host           : cdb-f0308s90.bj.tencentcdb.com:10121
 Source Schema         : bill_star

 Target Server Type    : MySQL
 Target Server Version : 50718
 File Encoding         : 65001

 Date: 09/09/2019 19:51:44
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ding_dept
-- ----------------------------
DROP TABLE IF EXISTS `ding_dept`;
CREATE TABLE `ding_dept`  (
  `id` int(11) NOT NULL DEFAULT 0,
  `parent_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_school_zone` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否校区：0=否；1=是；仅用于一级部门',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '如是校区，则需维护校区电话',
  `create_by` int(11) NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_by` int(11) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ding_dept_user
-- ----------------------------
DROP TABLE IF EXISTS `ding_dept_user`;
CREATE TABLE `ding_dept_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `corp_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `dept_id` int(11) NULL DEFAULT NULL,
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  `create_by` int(11) NULL DEFAULT NULL,
  `update_by` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for ding_user
-- ----------------------------
DROP TABLE IF EXISTS `ding_user`;
CREATE TABLE `ding_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_school_id` int(11) NULL DEFAULT NULL COMMENT '校区ID',
  `user_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mobile` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `position` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `is_admin` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否企业管理员，超管',
  `is_teacher` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否教师',
  `is_leader` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否部门领导',
  `create_by` int(11) NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL,
  `update_by` int(11) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_configs
-- ----------------------------
DROP TABLE IF EXISTS `sys_configs`;
CREATE TABLE `sys_configs`  (
  `id` bigint(8) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_by` int(100) NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` int(100) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `create_date` timestamp(0) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  `create_by` int(11) NULL DEFAULT NULL,
  `update_by` int(11) NULL DEFAULT NULL,
  `type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型，或者说属于哪个模块',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `type_name_unique`(`type`, `name`) USING BTREE COMMENT '类型+名字唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '对应实体名',
  `model_id` int(11) NULL DEFAULT NULL COMMENT '对应实体ID',
  `type` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源类型',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源链接',
  `create_by` int(255) NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `update_date` timestamp(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_bill
-- ----------------------------
DROP TABLE IF EXISTS `t_bill`;
CREATE TABLE `t_bill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '单据编号，生成，注意唯一性',
  `dept_school_id` int(11) NOT NULL COMMENT '校区ID',
  `student_id` int(11) NOT NULL COMMENT '学生ID',
  `amount` decimal(12, 4) NOT NULL COMMENT '金额',
  `type` tinyint(1) NOT NULL COMMENT '1=新生；2=续费；3=补费；4=退费；',
  `pay_type_id` int(11) NOT NULL COMMENT '支付方式，取自字典表',
  `bill_time` datetime(0) NOT NULL COMMENT '开票时间，默认取当前时间，也支持从前端传，方便历史数据导入',
  `bill_creator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '开票人，默认取当前用户姓名，也支持从前端传，方便历史数据导入',
  `is_transferred` tinyint(255) NULL DEFAULT NULL COMMENT '是否接送。0=否，1=是',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注，显示在单据上的',
  `create_by` int(11) NOT NULL,
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `update_by` int(11) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `unique_no`(`bill_no`) USING BTREE COMMENT '单据编号唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '票据信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_bill_course
-- ----------------------------
DROP TABLE IF EXISTS `t_bill_course`;
CREATE TABLE `t_bill_course`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bill_id` int(11) NULL DEFAULT NULL COMMENT '票据ID',
  `course_id` int(11) NULL DEFAULT NULL COMMENT '课程ID',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '到期时间',
  `create_date` timestamp(0) NULL DEFAULT NULL,
  `create_by` int(11) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  `update_by` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '票据和课程明细' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_course
-- ----------------------------
DROP TABLE IF EXISTS `t_course`;
CREATE TABLE `t_course`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_school_id` int(11) NULL DEFAULT NULL COMMENT '校区ID',
  `teacher_id` int(11) NULL DEFAULT NULL COMMENT '关联教师ID',
  `dict_course_id` int(11) NULL DEFAULT NULL COMMENT '字典表-班级名字（启蒙）',
  `course_index` tinyint(255) NULL DEFAULT NULL COMMENT '班别（1,2,3,4,5,6...15）',
  `regular_time` varchar(218) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上课时间（每周三10点到12点）',
  `classroom_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '教室号',
  `create_by` int(11) NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL,
  `update_by` int(11) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `course_unique`(`dept_school_id`, `teacher_id`, `dict_course_id`, `course_index`) USING BTREE COMMENT '校区+班级+班别+教师'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '课程信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dept_school_id` int(11) NOT NULL COMMENT '所属校区',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '姓名',
  `mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '手机号',
  `gender` tinyint(1) NOT NULL COMMENT '1=男，2=女',
  `school_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '学校；注意，该学校为学生就读学校',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '班级',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '住址',
  `create_by` int(11) NOT NULL,
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `update_by` int(11) NULL DEFAULT NULL,
  `update_date` timestamp(0) NULL DEFAULT NULL,
  `is_suspended` tinyint(255) NOT NULL COMMENT '0=未休学，1=已休学',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '学生信息' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
