SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for uaa_auth_url_config
-- ----------------------------
DROP TABLE IF EXISTS `uaa_auth_url_config`;
CREATE TABLE `uaa_auth_url_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用标志',
  `auth_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `url_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT 'url类型, 0 直接放行, 1 登录后可访问',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除 0 否, 1 是',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_app_url`(`app_name`, `url_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of uaa_auth_url_config
-- ----------------------------
INSERT INTO `uaa_auth_url_config` VALUES (1, 'book', '/api/login', 0, '2023-01-31 14:40:45', '2023-01-31 14:40:45', 0);

-- ----------------------------
-- Table structure for uaa_user_data_source
-- ----------------------------
DROP TABLE IF EXISTS `uaa_user_data_source`;
CREATE TABLE `uaa_user_data_source`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '应用标志',
  `query_all_path_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '调用地址, 用于获取应用所有接口',
  `ds` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '数据源名称',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'JDBC 用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'JDBC 密码',
  `driver_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'com.mysql.cj.jdbc.Driver' COMMENT 'JDBC driver',
  `url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'JDBC url 地址',
  `query_user_auth_sql` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '查询用户所有权限的sql',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_app`(`app_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of uaa_user_data_source
-- ----------------------------
INSERT INTO `uaa_user_data_source` VALUES (1, 'book', '', 'book', 'root', 'root', 'com.mysql.cj.jdbc.Driver', 'jdbc:mysql://127.0.0.1:3306/uaa?useUnicode=true&useSSL=false&allowMultiQueries=true&serverTimezone=GMT%2B8&useAffectedRows=true', 'SELECT DISTINCT permission_rule FROM tb_permission WHERE find_in_set(id, (SELECT r.rules FROM tb_role r, tb_user_role ur WHERE r.id = ur.role_id AND ur.user_id = 2 ))', '2023-01-31 14:40:45', '2023-01-31 14:40:45');

SET FOREIGN_KEY_CHECKS = 1;
