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
  `job_num` VARCHAR(100) COMMENT '工号',
  `salt` varchar(20) COMMENT '盐',
  `email` varchar(100) COMMENT '邮箱',
  `mobile` varchar(100) COMMENT '手机号',
  `ip` VARCHAR(20)  COMMENT '绑定IP',
  `defeats` INT DEFAULT 0 COMMENT '失败次数',
  `status` tinyint  DEFAULT 1 COMMENT '状态  0：禁用   1：正常',
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

-- 角色与部门对应关系，2020-12-10添加类型type 1代表部门，2代表项目类型
CREATE TABLE `sys_role_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` bigint COMMENT '角色ID',
  `dept_id` bigint COMMENT '部门ID',
  `type` tinyint DEFAULT 1 COMMENT '数据类型(1:部门,2:项目类型)',
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
INSERT INTO `sys_user` (`user_id`, `username`, `password`,`job_num`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (1, 'admin', '49f185de3a05937442d9c607901a7b7cee7ec44c3bb74a935a05c15e81175582','hz001', 'YzcmCZNvbXocrsz9dm8e', '18888666511@QQ.COM', '18888666511', '1', '1', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`,`job_num`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (2, 'manager', 'c903cfd1795bd696601eb261431048f71041791a3a3f20f5530d30639b52daf1','hz002', '00rKCFCP9rAcxdXjltwr', '18888666511@QQ.COM', '18888666511', '1', '2', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`,`job_num`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (3, 'safe', '54673bc71bf615b2316f3a985ef2879951d9780bbe70732504a3edf74f81e1df','hz003', '327njMiO6POltIozSK7R', 'SAFE@QQ.COM', '18812345678', '1', '2', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`,`job_num`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (4, 'auditor', 'aa625cfee2e68078d8c476c14c2aee2340019c5df601e53520ec63ed223eeedf','hz004', 'MpU26tc20e5so971dQwE', 'AUDITOR@QQ.COM', '18888866666', '1', '2', 2, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`,`job_num`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (5, 'jun', 'c903cfd1795bd696601eb261431048f71041791a3a3f20f5530d30639b52daf1','hz005', '00rKCFCP9rAcxdXjltwr', 'SAFE@QQ.COM', '18812345678', '1', '2', 1, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`,`job_num`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (6, 'yang', '15c6f85802bd1a3401bcb0d925e62f604d27509dbc180614e987ead4d88f0563','hz006', '00rKCFCP9rAcxdXjltwr', 'AUDITOR@QQ.COM', '18888866666', '1', '2', 2, NOW());
INSERT INTO `sys_user` (`user_id`, `username`, `password`,`job_num`, `salt`, `email`, `mobile`, `status`, `create_user_id`, `dept_id`, `create_time`) VALUES (7, 'guang', '34b50253208db134db2fea32d28cbbbee89340ac2862da9dcc4726f9ef1b11ce','hz007', '2NAhO1d9mtKXTtQrrEXk', 'AUDITOR@QQ.COM', '18888866666', '1', '2', 2, NOW());

-- 初始化角色数据 1管理员、2安全员、3审计员
INSERT INTO `sys_role` (`role_id`, `role_name`, `create_user_id`, `create_time`) VALUES (1, '管理员', '2', NOW());
INSERT INTO `sys_role` (`role_id`, `role_name`, `create_user_id`, `create_time`) VALUES (2, '安全员', '2', NOW());
INSERT INTO `sys_role` (`role_id`, `role_name`, `create_user_id`, `create_time`) VALUES (3, '审计员', '2', NOW());

TRUNCATE TABLE sys_menu;
TRUNCATE TABLE sys_role_menu;

INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'首页','dashboard',NULL,1,'iconstore',1);
  
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'客户管理','company',NULL,1,'iconCustomermanagement-fill',5);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:company:list,anlian:company:info,sys:user:list',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:company:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:company:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:company:delete',2,NULL,4);
  
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'拜访报价','quotation',NULL,1,'iconcontacts-fill',6);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:quotation:list,anlian:quotation:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:quotation:save,sys:user:list,anlian:company:list',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:quotation:update,sys:user:list,anlian:company:list',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:quotation:delete',2,NULL,4);
  
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'业务中心','',NULL,0,'iconset',7);
  SET @parentId_salesman = @@identity;

  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_salesman,'业务员管理','salesman','anlian:salesTarget:list,anlian:salesTarget:info',1,NULL,1);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:salesTarget:save',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:salesTarget:update',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:salesTarget:delete',2,NULL,3);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_salesman,'业绩统计','salesKpi','anlian:salesTarget:list,anlian:project:list,anlian:contract:list',1,NULL,2);
  SET @parentId = @@identity;
  
INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'合同管理','',NULL,0,'iconfeeds-fill',8);
  SET @parentId_contract = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_contract,'合同录入','contract',NULL,1,'config',1);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:contract:list,anlian:contract:info,sys:user:list,anlian:company:list,sys:dept:list',2,NULL,1);
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
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'项目管理','',NULL,0,'iconfeeds-fill',10);
  SET @parentId_project = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'项目录入','project',NULL,1,'config',1);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:project:list,anlian:project:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:project:save,anlian:contract:list,anlian:contract:info,sys:user:list,anlian:company:list',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:project:update,anlian:contract:list,anlian:contract:info,sys:user:list,anlian:company:list',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:project:delete',2,NULL,4);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'项目金额查看',NULL,'project:account:info',2,NULL,5);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'项目金额修改',NULL,'project:account:update',2,NULL,6);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'项目统计','projectStatistics','anlian:salesTarget:list,anlian:project:list',1,'config',2);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'项目统计详情','projectDetailStatis','anlian:salesTarget:list',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'项目状态统计','projectDetState','anlian:salesTarget:list',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'项目进行中的统计','projectStateNow','anlian:salesTarget:list',2,NULL,3);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'部门统计','deptStatistics','anlian:project:list,sys:dept:list,sys:user:list',1,NULL,3);
  SET @parentId = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'部门目标','setDeptTarget','anlian:divisionTarget:list,anlian:divisionTarget:info,sys:dept:list,sys:user:list',1,NULL,4);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'部门目标设置',NULL,'anlian:divisionTarget:save',2,NULL,1);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'项目修改','projectStatus',NULL,1,'config',5);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:project:list,anlian:project:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:project:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:project:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:project:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'项目查询','projectAll',NULL,1,'config',6);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:project:list,anlian:project:info,anlian:projectProcedures:list,dept:project:list',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'终止',NULL,'anlian:project:update',2,NULL,2);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'部门项目','deptProject','dept:project:list,anlian:project:update',1,NULL,7);
  SET @parentId = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_project,'项目评审','projectReview',NULL,1,NULL,8);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:project:list,anlian:projectReview:list,anlian:projectReview:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:projectReview:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:projectReview:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:projectReview:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'收付款记录','account','anlian:account:list,anlian:project:list',1,'iconall',13);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:account:save',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:account:update',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:account:delete',2,NULL,3);
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'任务管理','',NULL,0,'iconmanage-order-fill',15);
  SET @parentId_task = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_task,'任务下发','palnIssue',NULL,1,'config',1);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:paln:list,anlian:paln:info,sys:dept:list',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'下发',NULL,'anlian:paln:update',2,NULL,2);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_task,'任务排单','planScheduling',NULL,1,'config',2);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:plan:list,anlian:plan:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'排单',NULL,'anlian:plan:save,anlian:plan:update',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'补采',NULL,'anlian:plan:save,anlian:plan:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'重排',NULL,'anlian:plan:save,anlian:plan:update',2,NULL,4);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'确认排单',NULL,'anlian:plan:save,anlian:plan:update',2,NULL,5);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_task,'补采任务','planRepeat','anlian:plan:list,anlian:plan:info,sys:dept:list,sys:user:list',1,'config',3);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'排单',NULL,'anlian:plan:save,anlian:plan:update',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'重排',NULL,'anlian:plan:save,anlian:plan:update',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'确认排单',NULL,'anlian:plan:save,anlian:plan:update',2,NULL,3);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'现场调查','survey','anlian:plan:list,anlian:plan:info,sys:dept:list',1,'iconorder-fill',20);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'调查记录','surverDetail','sample:company:list,sample:company:info',1,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'sample:company:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'sample:company:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'sample:company:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'采样计划','planList','anlian:plan:list,anlian:plan:info,sys:dept:list',1,'iconform-fill',21);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'采样计划','gatherPlan','anlian:gatherplan:list,anlian:gatherplan:info',1,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'采样排单',NULL,'anlian:gatherplan:save,anlian:equipment:list,anlian:equipment:save,anlian:equipment:update,anlian:equipment:delete',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:gatherplan:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:gatherplan:delete',2,NULL,4);
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'采样记录','sampleList','anlian:plan:list,anlian:plan:info,sys:dept:list,',1,'icontemplate-fill',25);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'采样记录','sample','sample:gather:list,sample:gather:info,sample:weather:list',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'sample:gather:save,sample:weather:save',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'sample:gather:update,sample:weather:update',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'sample:gather:delete,sample:weather:delete',2,NULL,6);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'方案补充',NULL,'sample:company:list,sample:company:info,sample:company:save,sample:company:update,anlian:company:delete',2,NULL,6);
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'报告管理','',NULL,0,'iconmanage-order-fill',29);
  SET @parentId_report = @@identity;
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_report,'实验室报告','reportData','anlian:plan:list,anlian:plan:info,sys:dept:list',1,NULL,30);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'sample:gather:list,sample:gather:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'sample:gather:save,sample:weather:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'sample:gather:update,sample:weather:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'sample:gather:delete,sample:weather:delete',2,NULL,4);
    
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_report,'检测报告','reportAll','anlian:plan:list,anlian:plan:info,sys:dept:list',1,NULL,35);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:gismethod:list,anlian:gismethod:info,report:review:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'详情',NULL,'anlian:gismethod:info,anlian:gismethod:listByContractId',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'添加',NULL,'anlian:gismethod:save,report:review:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:gismethod:update,report:review:update',2,NULL,4);
  
  
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
  
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (0,'基础数据管理',NULL,NULL,0,'iconset',98);
  SET @parentId_artisan = @@identity;
  
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_artisan,'技术人员信息','artisan',NULL,1,NULL,1);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'查看',NULL,'anlian:plan:list,anlian:plan:info',2,NULL,1);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'新增',NULL,'anlian:artisan:save',2,NULL,2);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'修改',NULL,'anlian:artisan:update',2,NULL,3);
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId,'删除',NULL,'anlian:artisan:delete',2,NULL,4);
  


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
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '查看', NULL, 'sys:role:list,sys:role:info,sys:role:select', 2, NULL, 1);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '新增', NULL, 'sys:role:save', 2, NULL, 2);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '修改', NULL, 'sys:role:update', 2, NULL, 3);
		INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '删除', NULL, 'sys:role:delete', 2, NULL, 4);

  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId_menu, '菜单管理', 'menu', NULL, '1', 'menu', 10);
  SET @parentId = @@identity;
  INSERT INTO `sys_menu`(`parent_id`, `name`, `url`, `perms`, `type`, `icon`, `order_num`) VALUES (@parentId, '查看', NULL, 'sys:menu:list,sys:menu:info,sys:role:select', 2, NULL, 1);
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

TRUNCATE TABLE sys_dept;
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES (1, '0', '浙江安联检测', '0', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES (2, '1', '杭州～浙江安联检测', '1', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES (3, '1', '嘉兴～安联检测', '2', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES (4, '1', '宁波～安联检测', '3', '0');
INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `name`, `order_num`, `del_flag`) VALUES (5, '1', '宁波优维', '4', '0');
-- 杭州～浙江安联检测 ：管理部1；财务部4；综合部12；市场部9；实验室21；环境公卫事业部；职业卫生事业部41；辐射事业部2；
-- 杭州～浙江安联检测：综合部（主管张林英）：行政人事组3；质控组4；IT技术组4；
-- 杭州～浙江安联检测：市场部（主管张国康）：职业卫生8；环境检测0
-- 杭州～浙江安联检测：环境公卫事业部（主管李晓臣）：公共卫生组5；环境咨询组2；环境检测组4；
-- 杭州～浙江安联检测：职业卫生事业部（主管孙春花）：检评组23（检评一组、检评二组、检评三组、检评四组）；评价组15（评价采样组、评价一组、评价二组）；

-- 杭州～浙江安联检测
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '管理部', 21);
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '财务部', 22);
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '综合部', 23);
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '市场部', 24);
	SET @parentId = @@identity;
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '职业卫生');
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '环境检测');

INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '实验室', 25);	
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '环境公卫事业部', 26);
	SET @parentId = @@identity;
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '公共卫生组');
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '环境咨询组');
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '环境检测组');
	
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '职业卫生事业部', 27);
	SET @parentId_zyws = @@identity;
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId_zyws, '检评组');
		SET @parentId = @@identity;
		INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '检评一组');
		INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '检评二组');
		INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '检评三组');
		INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '检评四组');
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId_zyws, '评价组');
		SET @parentId = @@identity;
		INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '评价采样组');
		INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '评价一组');
		INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '评价二组');
	
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (2, '辐射事业部', 28);
-- 杭州～浙江安联检测

-- 嘉兴～安联检测（主管王勇）：综合部4；业务中心6；实验室4；环境公卫事业部3；职业卫生事业部10；
-- 嘉兴～安联检测：职业卫生事业部（主管张袁金）：检评四组4、检评五组3；评价一组1、评价二组1；
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (3, '综合部', 31);
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (3, '业务中心', 32);
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (3, '实验室', 33);	
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (3, '环境公卫事业部', 34);
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (3, '职业卫生事业部', 35);
	SET @parentId = @@identity;
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '检评四组');
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '检评五组');
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '评价一组');
	INSERT INTO `sys_dept` (`parent_id`, `name`) VALUES (@parentId, '评价二组');
-- 嘉兴～安联检测

-- 宁波～安联检测（主管蒋倪梦）：评价部13；业务部5；
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (4, '评价部', 41);
INSERT INTO `sys_dept` (`parent_id`, `name`, `order_num`) VALUES (4, '业务部', 42);
-- 宁波～安联检测

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
    
INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num,remark) VALUE 
('合同评审','projectReview','1','客户要求是否符合法律法规的要求','1',NULL),
('合同评审','projectReview','2','资质业务服务范围(是否满足)','2',NULL),
('合同评审','projectReview','3','人员专业能力(是否满足)','3',NULL),
('合同评审','projectReview','4','仪器设备及环境条件(是否满足)','4',NULL),
('合同评审','projectReview','5','检测方法及标准物质(是否满足)','5',NULL),
('合同评审','projectReview','6','检测项目是否需要分包','6','分包项目：_______ \n 分包比例_______%'),
('合同评审','projectReview','7','合同约定完成时限(是否满足)','7','_____个工作日'),
('合同评审','projectReview','8','后勤保障能否满足要求','8',NULL),
('合同评审','projectReview','9','项目收费是否合理','9',NULL);

INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('失败原因','failureCause','1','价格原因','1'),
('失败原因','failureCause','2','资质不符','2'),
('失败原因','failureCause','3','时效不足','3'),
('失败原因','failureCause','4','客户关系','4'),
('失败原因','failureCause','5','监管关系','5');

INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('支付方式','paymentType','1','支付方式一','1'),
('支付方式','paymentType','2','支付方式二','2'),
('支付方式','paymentType','3','支付方式三','3'),
('支付方式','paymentType','4','支付方式四','4'),
('支付方式','paymentType','5','支付方式五','5');

INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('联系人类型','contactType','1','商务联系人','1'),
('联系人类型','contactType','2','EHS联系人','2'),
('联系人类型','contactType','3','财务联系人','3'),
('联系人类型','contactType','4','业务联系人','4');

INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('项目类型','projectType','1','检评','1'),
('项目类型','projectType','2','预评','2'),
('项目类型','projectType','3','专篇','3'),
('项目类型','projectType','4','控评','4'),
('项目类型','projectType','5','现评','5'),
('项目类型','projectType','6','环境检测','6'),
('项目类型','projectType','7','环境验收','7'),
('项目类型','projectType','8','排污许可','8'),
('项目类型','projectType','9','咨询','9'),
('项目类型','projectType','10','公卫检测','10'),
('项目类型','projectType','11','辐射检测','11'),
('项目类型','projectType','12','辐射评价','12');


INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('隶属公司','membershipType','1','杭州安联','1'),
('隶属公司','membershipType','2','宁波安联','2'),
('隶属公司','membershipType','3','嘉兴安联','3');


INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('提成类型','commissionType','1','业务提成','1'),
('提成类型','commissionType','2','采样提成','2'),
('提成类型','commissionType','3','报告编制提成','3'),
('提成类型','commissionType','4','检测提成','4');

INSERT INTO sys_dict (NAME,TYPE,CODE,VALUE,order_num) VALUE 
('提成比列','commissionRatio','1','0.06','1'),
('提成比列','commissionRatio','2','0.02','2'),
('提成比列','commissionRatio','3','0.035','3'),
('提成比列','commissionRatio','4','0.03','4');


INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',1,'hasHealthOrg',1,'是否建立职业卫生管理机构',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',2,'hasManager',2,'是否设有专（兼）职管理人员',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',3,'hasThreeSimul',3,'是否建立职业卫生“三同时”制度并实施',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',4,'hasHealthPlan',4,'是否制定职业病防治计划和实施方案',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',5,'isManifestDuty',5,'如涉及协议单位/外包作业人员，是否明确外包单位/个人的职业病防治责任',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',6,'hasHazardSys',6,'是否建立职业病危害申报制度并实施',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',7,'hasSecuritySys',7,'是否建立企业职业安全规章制度',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',8,'hasWarn',8,'是否在使用有毒物品作业场所的醒目位置设置警示标识、告知卡和中文说明书',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',9,'hasHazardNoti',9,'是否进行了职业危害合同告知',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',10,'hasSupervisionSys',10,'是否建立作业场所职业病危害监测制度并实施',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',11,'hasHealthSys',11,'是否建立职业健康监护制度并实施',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',12,'hasTrain',12,'是否建立有毒有害作业工人的上岗培训和危害告知制度',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',13,'hasProtectEquipSys',13,'是否建立个人防护用品的使用管理制度',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',14,'hasHealthRecord',14,'是否建立职业健康监护档案和职业卫生管理档案',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',15,'hasEmergencyPlan',15,'是否制度应急救援预案',0);
INSERT into `sys_dict` (`name`,`type`,`code`,`value`,`order_num`,`remark`,`del_flag`) VALUES('企业评价信息补充说明','companySurveyAppariseNote',16,'isEmergencyExercise',16,'是否展开应急救援演练',0);


-- ----------------------------
-- Records of t_law
-- ----------------------------
INSERT INTO `t_law`(id,law_name,descriptions,defvalue) VALUES 
('1', '《中华人民共和国职业病防治法》（2017年修正）中华人民共和国主席令第81号', '2017-11-05', 1),
('2', '《中华人民共和国安全生产法》中华人民共和国主席令第13号', '2014-12-01', 1),
('3', '《中华人民共和国劳动合同法》中华人民共和国主席令第73号', '2013-07-01', 1),
('4', '《中华人民共和国尘肺病防治条例》中华人民共和国国务院令第105号', '1987-12-03', 1),
('5', '《工作场所职业卫生监督管理规定》国家安全生产监督管理总局47号', '2012-06-01', 1),
('6', '《职业病危害项目申报办法》国家安全生产监督管理总局令第48号', '2012-06-01', 1),
('7', '《用人单位职业健康监护监督管理办法》国家安全生产监督管理总局49号', '2012-06-01', 1),
('8', '《用人单位职业病危害因素定期检测管理规范》安监总厅安健〔2015〕16号', '2015-02-28', 1),
('9', '《职业病危害因素分类目录》国卫疾控发〔2015〕92号', '2015-11-17', 1),
('10', '《用人单位职业病危害告知与警示标识管理规范》安监总厅安健〔2014〕111号', '2014-11-13', 1),
('11', '《建设项目职业病危害风险分类管理目录》安监总安健〔2012〕73号', '2012-05-31', 1),
('12', '《职业病危害因素分类目录》国卫疾控发〔2015〕92号', '2015-11-17', 1);

-- ALTER TABLE t_account ADD INDEX INDEX_ACCOUNT_PROJECT_ID (`project_id`);  -- 创建收付款记录表项目ID的索引

-- 配置信息数据初始化 
  INSERT IGNORE INTO `sys_config`(`param_key`,`param_value`,`status`,`remark`) values 
    ('SystemLoginDefeats',5,1,'允许用户登录连续失败最大次数(默认值为5)'),
    ('SystemDiskThreshold',0.80,1,'磁盘空间低于阈值时告警(数值小于1,默认值0.80)');
    
  INSERT IGNORE INTO `sys_config`(`param_key`,`param_value`,`status`,`remark`) values 
    ('CharcoalTubeAlkane','|正己烷|正戊烷|正庚烷|辛烷|壬烷|',1,'可以在同一个活性炭管中采集的己烷类物质的配置(前后加|)'),
    ('CharcoalTubeBenzene','|苯|甲苯|二甲苯|乙苯|乙酸乙酯|乙酸丁酯|',1,'可以在同一个活性炭管中采集的苯类物质的配置(前后加|)');

  INSERT IGNORE INTO `sys_config`(`param_key`,`param_value`,`status`,`remark`) values 
    ('CharcoalTubeAlkane','|正己烷|正戊烷|正庚烷|辛烷|壬烷|',1,'可以在同一个活性炭管中采集的己烷类物质的配置(前后加|)'),
    ('CharcoalTubeBenzene','|苯|甲苯|二甲苯|乙苯|乙酸乙酯|乙酸丁酯|',1,'可以在同一个活性炭管中采集的苯类物质的配置(前后加|)');
    