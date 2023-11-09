/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`anlian` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

use anlian;


-- 系统管理基础信息表
DROP TABLE IF EXISTS sys_menu;
DROP TABLE IF EXISTS sys_dept;
DROP TABLE IF EXISTS sys_user;
DROP TABLE IF EXISTS sys_user_token;
DROP TABLE IF EXISTS sys_captcha;
DROP TABLE IF EXISTS sys_role;
DROP TABLE IF EXISTS sys_user_role;
DROP TABLE IF EXISTS sys_role_menu;
DROP TABLE IF EXISTS sys_role_dept;
DROP TABLE IF EXISTS sys_config;
DROP TABLE IF EXISTS sys_dict;
DROP TABLE IF EXISTS sys_log;
DROP TABLE IF EXISTS sys_task;

-- 菜单
CREATE TABLE `sys_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) COMMENT '菜单名称',
  `url` varchar(200) COMMENT '菜单URL',
  `perms` varchar(500) COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) COMMENT '菜单图标',
  `order_num` int COMMENT '排序',
  PRIMARY KEY (`menu_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='菜单管理';

-- 部门
CREATE TABLE `sys_dept` (
  `dept_id` bigint NOT NULL AUTO_INCREMENT,
  `parent_id` bigint COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) COMMENT '部门名称',
  `order_num` int COMMENT '排序',
  `del_flag` tinyint DEFAULT 0 COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='部门管理';

-- 系统用户
CREATE TABLE `sys_user` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) COMMENT '密码',
  `salt` varchar(20) COMMENT '盐',
  `email` varchar(100) COMMENT '邮箱',
  `mobile` varchar(100) COMMENT '手机号',
  `ip` VARCHAR(20)  COMMENT '绑定IP',
  `defeats` INT DEFAULT 0 COMMENT '失败次数',
  `status` tinyint COMMENT '状态  0：禁用   1：正常',
  `dept_id` bigint(20) COMMENT '部门ID',
  `create_user_id` bigint(20) COMMENT '创建者ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX (`username`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户';

-- 系统用户Token
CREATE TABLE `sys_user_token` (
  `user_id` bigint(20) NOT NULL,
  `token` varchar(100) NOT NULL COMMENT 'token',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `token` (`token`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户Token';

-- 系统验证码
CREATE TABLE `sys_captcha` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `code` varchar(6) NOT NULL COMMENT '验证码',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='系统验证码';

-- 角色
-- 2019-04-24 角色名称重复的限制
CREATE TABLE `sys_role` (
  `role_id` bigint NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) NOT NULL COMMENT '角色名称',
  `remark` varchar(100) COMMENT '备注',
  `dept_id` bigint(20) COMMENT '部门ID',
  `create_user_id` bigint(20) COMMENT '创建者ID',
  `create_time` datetime COMMENT '创建时间',
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='角色';

-- 用户与角色对应关系
CREATE TABLE `sys_user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint COMMENT '用户ID',
  `role_id` bigint COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户与角色对应关系';

-- 角色与菜单对应关系
CREATE TABLE `sys_role_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint COMMENT '角色ID',
  `menu_id` bigint COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单对应关系';

-- 角色与部门对应关系
CREATE TABLE `sys_role_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint COMMENT '角色ID',
  `dept_id` bigint COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='角色与部门对应关系';

-- 系统配置信息
CREATE TABLE `sys_config` (
	`id` bigint NOT NULL AUTO_INCREMENT,
	`param_key` varchar(50) COMMENT 'key',
	`param_value` varchar(2000) COMMENT 'value',
	`status` tinyint DEFAULT 1 COMMENT '状态   0：隐藏   1：显示',
	`remark` varchar(500) COMMENT '备注',
	PRIMARY KEY (`id`),
	UNIQUE INDEX (`param_key`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置信息表';

-- 数据字典
CREATE TABLE `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL COMMENT '字典名称',
  `type` varchar(100) NOT NULL COMMENT '字典类型',
  `code` varchar(100) NOT NULL COMMENT '字典码',
  `value` varchar(1000) NOT NULL COMMENT '字典值',
  `order_num` int DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) COMMENT '备注',
  `del_flag` tinyint DEFAULT 0 COMMENT '删除标记  -1：已删除  0：正常',
  PRIMARY KEY (`id`),
  UNIQUE KEY(`type`,`code`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='数据字典表';

-- 系统日志
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) COMMENT '用户名',
  `operation` varchar(50) COMMENT '用户操作',
  `method` varchar(200) COMMENT '请求方法',
  `params` text COMMENT '请求参数',
  `time` bigint NOT NULL COMMENT '执行时长(毫秒)',
  `ip` varchar(64) COMMENT 'IP地址',
  `create_date` datetime COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志';

-- 任务计划表
CREATE TABLE sys_task (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  cron_expression varchar(255) DEFAULT NULL COMMENT 'cron表达式',
  method_name varchar(255) DEFAULT NULL COMMENT '任务调用的方法名',
  is_concurrent varchar(255) DEFAULT NULL COMMENT '任务是否有状态',
  description varchar(255) DEFAULT NULL COMMENT '任务描述',
  update_by varchar(64) DEFAULT NULL COMMENT '更新者',
  bean_class varchar(255) DEFAULT NULL COMMENT '任务执行时调用哪个类的方法 包名+类名',
  create_date datetime DEFAULT NULL COMMENT '创建时间',
  job_status varchar(255) DEFAULT NULL COMMENT '任务状态',
  job_group varchar(255) DEFAULT NULL COMMENT '任务分组',
  update_date datetime DEFAULT NULL COMMENT '更新时间',
  create_by varchar(64) DEFAULT NULL COMMENT '创建者',
  spring_bean varchar(255) DEFAULT NULL COMMENT 'Spring bean',
  job_name varchar(255) DEFAULT NULL COMMENT '任务名',
  PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='任务计划';


-- 初始数据 用户名admin 密码mayong  2管理员manager/manager、3安全员safe/safe、4审计员auditor/auditor
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (1, 'admin', '49f185de3a05937442d9c607901a7b7cee7ec44c3bb74a935a05c15e81175582', 'YzcmCZNvbXocrsz9dm8e', '18888666511@QQ.COM', '18888666511', '1', '1', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (2, 'manager', 'c903cfd1795bd696601eb261431048f71041791a3a3f20f5530d30639b52daf1', '00rKCFCP9rAcxdXjltwr', '18888666511@QQ.COM', '18888666511', '1', '2', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (3, 'safe', '54673bc71bf615b2316f3a985ef2879951d9780bbe70732504a3edf74f81e1df', '327njMiO6POltIozSK7R', 'SAFE@QQ.COM', '18812345678', '1', '2', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (4, 'auditor', 'aa625cfee2e68078d8c476c14c2aee2340019c5df601e53520ec63ed223eeedf', 'MpU26tc20e5so971dQwE', 'AUDITOR@QQ.COM', '18888866666', '1', '2', 2, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (5, 'jun', 'c903cfd1795bd696601eb261431048f71041791a3a3f20f5530d30639b52daf1', '00rKCFCP9rAcxdXjltwr', 'SAFE@QQ.COM', '18812345678', '1', '2', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (6, 'yang', '15c6f85802bd1a3401bcb0d925e62f604d27509dbc180614e987ead4d88f0563', '00rKCFCP9rAcxdXjltwr', 'AUDITOR@QQ.COM', '18888866666', '1', '2', 2, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (7, 'guang', '34b50253208db134db2fea32d28cbbbee89340ac2862da9dcc4726f9ef1b11ce', '2NAhO1d9mtKXTtQrrEXk', 'AUDITOR@QQ.COM', '18888866666', '1', '2', 2, NOW());

-- 初始化角色数据 1管理员、2安全员、3审计员
INSERT INTO `sys_role` (`role_id`, `role_name`, `create_user_id`, `create_time`) VALUES (1, '管理员', '2', NOW());
INSERT INTO `sys_role` (`role_id`, `role_name`, `create_user_id`, `create_time`) VALUES (2, '安全员', '2', NOW());
INSERT INTO `sys_role` (`role_id`, `role_name`, `create_user_id`, `create_time`) VALUES (3, '审计员', '2', NOW());

TRUNCATE TABLE sys_menu;
TRUNCATE TABLE sys_role_menu;

INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'首页','dashboard',NULL,1,'iconstore',1);
  
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'客户管理','company',NULL,1,'iconCustomermanagement-fill',5);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:company:list,anlian:company:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:company:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:company:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:company:delete',2,NULL,4);
  
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'拜访报价','quotation',NULL,1,'iconcontacts-fill',6);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:quotation:list,anlian:quotation:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:quotation:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:quotation:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:quotation:delete',2,NULL,4);
  
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'合同管理','',NULL,0,'iconfeeds-fill',10);
  SET @parentId_contract = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'合同录入','contract',NULL,1,'config',1);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contract:list,anlian:contract:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:contract:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:contract:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:contract:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'模板工具','contractTool',NULL,1,'config',2);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contractTool:list,anlian:contractTool:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:contractTool:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:contractTool:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:contractTool:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'模板管理','contractTmpl',NULL,1,'config',3);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contractTmpl:list,anlian:contractTmpl:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:contractTmpl:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:contractTmpl:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:contractTmpl:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'合同生成','contractCreate',NULL,1,'config',4);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contractCreate:list,anlian:contractCreate:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:contractCreate:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:contractCreate:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:contractCreate:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'合同状态','contractStatus',NULL,1,'config',5);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contractStatus:list,anlian:contractStatus:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:contractStatus:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:contractStatus:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:contractStatus:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'合同查询','contractAll',NULL,1,'config',6);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contractAll:list,anlian:contractAll:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:contractAll:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:contractAll:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:contractAll:delete',2,NULL,4);
 
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'合同评审','contractReview',NULL,1,NULL,7);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contractReview:list,anlian:contractReview:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:contractReview:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:contractReview:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:contractReview:delete',2,NULL,4);
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'收付款记录','account','anlian:account:list',1,'iconall',13);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:account:save',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:account:update',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:account:delete',2,NULL,3);
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'任务管理','',NULL,0,'iconmanage-order-fill',15);
  SET @parentId_task = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_task,'任务下发','palnIssue',NULL,1,'config',1);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:palnIssue:list,anlian:palnIssue:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:palnIssue:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:palnIssue:update,anlian:contract:relayTask',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:palnIssue:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_task,'任务排单','planScheduling',NULL,1,'config',2);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:plan:list,anlian:plan:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:plan:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:plan:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:plan:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'现场调查','survey',NULL,1,'iconorder-fill',20);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'调查记录','surverDetail','sample:company:list,sample:company:info',1,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'sample:company:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'sample:company:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'sample:company:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'采样计划','planList',NULL,1,'iconform-fill',21);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'采样计划','gatherPlan','anlian:gatherplan:list,anlian:gatherplan:info',1,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:gatherplan:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:gatherplan:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:gatherplan:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'采样记录','sampleList',NULL,1,'icontemplate-fill',25);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'采样记录','sample','sample:gather:list,sample:gather:info,sample:weather:list',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'sample:gather:save,sample:weather:save',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'sample:gather:update,sample:weather:update',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'sample:gather:delete,sample:weather:delete',2,NULL,6);
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'报告管理','',NULL,0,'iconmanage-order-fill',29);
  SET @parentId_report = @@identity;
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_report,'实验室报告','reportData','',1,'',30);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'sample:gather:list,sample:gather:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'sample:gather:save,sample:weather:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'sample:gather:update,sample:weather:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'sample:gather:delete,sample:weather:delete',2,NULL,4);
    
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_report,'检测报告','reportAll',NULL,1,NULL,35);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:gismethod:list',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'详情',NULL,'anlian:gismethod:info,anlian:gismethod:listByContractId',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'添加',NULL,'anlian:gismethod:save',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:gismethod:update',2,NULL,4);
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_report,'报告审核','reportReview',NULL,1,NULL,40);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'report:review:list',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'详情',NULL,'report:review:info,report:review:getByContractId',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'添加',NULL,'report:review:save',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'report:review:update',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_report,'报告校核','reportCheck',NULL,1,NULL,45);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'report:proofread:list',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'详情',NULL,'report:proofread:info',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'添加',NULL,'report:proofread:save',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'report:proofread:update',2,NULL,4);

 

  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'项目归档','projecarchive',NULL,1,'iconintegral-fill',50);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:projecarchive:list,anlian:projecarchive:info',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:projecarchive:save',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:projecarchive:update',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:projecarchive:delete',2,NULL,6);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'仪器管理','',NULL,0,'iconvideo',55);
  SET @parentId_equipment = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_equipment,'仪器管理','equipment',NULL,1,'el-icon-video-camera',57);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:equipment:list,anlian:equipment:info',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:equipment:save',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:equipment:update',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:equipment:delete',2,NULL,6);
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_equipment,'仪器预定','reservation',NULL,1,'el-icon-video-camera',59);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:equipment:list,anlian:equipment:info',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:equipment:save',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:equipment:update',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:equipment:delete',2,NULL,6);
  


-- 菜单初始化
-- 2019-05-20 菜单新增ICON矢量图标的美化
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0, '系统管理', NULL, NULL, 0, 'iconset', 100);
SET @parentId_menu = @@identity;


  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '部门管理', 'dept', NULL, 1, 'admin', 5);
  SET @parentId = @@identity;
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '查看', NULL, 'sys:dept:list,sys:dept:info', 2, NULL, 1);
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '新增', NULL, 'sys:dept:save', 2, NULL, 2);
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '修改', NULL, 'sys:dept:update', 2, NULL, 3);
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '删除', NULL, 'sys:dept:delete', 2, NULL, 4);

	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '用户管理', 'user', NULL, 1, 'admin', 1);
	SET @parentId = @@identity;
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '查看', NULL, 'sys:user:list,sys:user:info', 2, NULL, 1);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '新增', NULL, 'sys:user:save', 2, NULL, 2);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '修改', NULL, 'sys:user:update', 2, NULL, 3);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '删除', NULL, 'sys:user:delete', 2, NULL, 4);

	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '角色管理', 'role', NULL, 1, 'role', 10);
	SET @parentId = @@identity;
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '查看', NULL, 'sys:role:list,sys:role:info', 2, NULL, 1);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '新增', NULL, 'sys:role:save', 2, NULL, 2);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '修改', NULL, 'sys:role:update', 2, NULL, 3);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '删除', NULL, 'sys:role:delete', 2, NULL, 4);

  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '菜单管理', 'menu', NULL, '1', 'menu', 10);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '查看', NULL, 'sys:menu:list,sys:menu:info', 2, NULL, 1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '新增', NULL, 'sys:menu:save', 2, NULL, 2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '修改', NULL, 'sys:menu:update', 2, NULL, 3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '删除', NULL, 'sys:menu:delete', 2, NULL, 4);

  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '字典管理', 'user', NULL, 1, 'config', 5);
  SET @parentId = @@identity;
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '查看', NULL, 'sys:dict:list,sys:dict:info', 2, NULL, 0);
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '新增', NULL, 'sys:dict:save', 2, NULL, 0);
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '修改', NULL, 'sys:dict:update', 2, NULL, 0);
    INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '删除', NULL, 'sys:dict:delete', 2, NULL, 0);

	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '参数管理', 'config', 'sys:config:list,sys:config:info,sys:config:save,sys:config:update,sys:config:delete', 1, 'config', 6);
	
	INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '系统日志', 'log', 'sys:log:list', 1, 'log', 10);
	
  

  -- 初始化管理员角色的权限，即赋予全部权限给管理员角色  TRUNCATE TABLE sys_role_menu;
  INSERT INTO sys_role_menu(role_id,menu_id) SELECT 1,menu_id FROM sys_menu;
  INSERT INTO sys_role_menu(role_id,menu_id) values(1,-666666);   -- 当子菜单并非全选时，主菜单在菜单列表也需要呈现的处理
  
  -- 初始化用户的角色的权限，用户关联的角色
  INSERT INTO sys_user_role(user_id,role_id) VALUES(2,1),(3,2),(4,3),(5,1),(6,1),(7,1);


INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('1', '0', '安联集团', '0', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('2', '1', '杭州分公司', '1', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('3', '1', '宁波分公司', '2', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('4', '3', '技术部', '0', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES ('5', '3', '销售部', '1', '0');

-- INSERT INTO `sys_dict`(`id`, `name`, `type`, `code`, `value`, `order_num`, `remark`, `del_flag`) VALUES (1, '性别', 'sex', '0', '女', 0, NULL, 0);
-- INSERT INTO `sys_dict`(`id`, `name`, `type`, `code`, `value`, `order_num`, `remark`, `del_flag`) VALUES (2, '性别', 'sex', '1', '男', 1, NULL, 0);
-- INSERT INTO `sys_dict`(`id`, `name`, `type`, `code`, `value`, `order_num`, `remark`, `del_flag`) VALUES (3, '性别', 'sex', '2', '未知', 3, NULL, 0);

INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('技术审核记录','reportReview','1','模板是否统一','1'),
('技术审核记录','reportReview','2','信息是否完整','2'),
('技术审核记录','reportReview','3','检测项目与协议或检测计划所示内容是否一致、项目齐全性','3'),
('技术审核记录','reportReview','4','报告与现场调查资料的一致性','4'),
('技术审核记录','reportReview','5','报告与原始记录的一致性','5'),
('技术审核记录','reportReview','6','质量控制数据是否符合要求','6'),
('技术审核记录','reportReview','7','检测方法的适用性和有效性','7'),
('技术审核记录','reportReview','8','所用仪器的适用性和有效性','8'),
('技术审核记录','reportReview','9','数据统计方法的适用性','9'),
('技术审核记录','reportReview','10','报告内容的完整性和数据准确性','10'),
('技术审核记录','reportReview','11','检测数据是否符合逻辑关系','11'),
('技术审核记录','reportReview','12','检测结论的正确性及建议的可行性','12');


INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('校核记录','reportProofread','1','模板是否统一','1'),
('校核记录','reportProofread','2','信息是否完整','2'),
('校核记录','reportProofread','3','检测项目与协议或任务单所示内容是否一致、项目齐全性','3'),
('校核记录','reportProofread','4','技术审核修改意见是否落实','4');

-- 采样样品数量数据的初始化，其他采样物质样品数量默认为1
-- delete from sys_dict where type='sampleRecordTypeToNum';
INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('样品数量·噪声定点测量','sampleRecordTypeToNum','noiseFixed','3','1'),
('样品数量·脉冲噪声强度测试','sampleRecordTypeToNum','noisePulse','3','2'),
('样品数量·噪声倍频程测量','sampleRecordTypeToNum','noiseOctave','3','3'),
('样品数量·高温(热源稳定)测试','sampleRecordTypeToNum','temperatureStable','3','4'),
('样品数量·空气中有害物质定点','sampleRecordTypeToNum','airFixed','3','5'),
('样品数量·空气中有害物质个体','sampleRecordTypeToNum','airIndividual','3','6'),
('样品数量·空气中粉尘分散度定点','sampleRecordTypeToNum','airDust','3','7'),
('样品数量·一氧化碳定点测试','sampleRecordTypeToNum','co','3','8'),
('样品数量·二氧化碳定点测试','sampleRecordTypeToNum','co2','3','9');
    

-- 配置信息数据初始化 
  INSERT IGNORE INTO `sys_config`(`param_key`,`param_value`,`status`,`remark`) values 
    ('SystemLoginDefeats',5,1,'允许用户登录连续失败最大次数(默认值为5)'),
    ('SystemDiskThreshold',0.80,1,'磁盘空间低于阈值时告警(数值小于1,默认值0.80)');

	