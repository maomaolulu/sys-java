-- 创建表
use anlian;

-- t_company	企业信息表
CREATE TABLE IF NOT EXISTS `t_company`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `code` VARCHAR(25) COMMENT '统一社会信用代码',
 `company` VARCHAR(200) NOT NULL COMMENT '企业名称',
 `address` VARCHAR(200) COMMENT '注册地址',
 `legalname` VARCHAR(50) COMMENT '法人代表',
 `scope` VARCHAR(1024) COMMENT '经营范围',
 `register_date` DATE COMMENT '成立日期',
 `province` VARCHAR(20) COMMENT '省份',
 `city` VARCHAR(50) COMMENT '城市',
 `area` VARCHAR(50) COMMENT '区县',
 `office_address` VARCHAR(200) COMMENT '办公地址',
 `contact` VARCHAR(50) COMMENT '联系人',
 `mobile` VARCHAR(50) COMMENT '联系电话(手机)',
 `telephone` VARCHAR(50) COMMENT '固定电话',
 `fax` VARCHAR(50) COMMENT '传真',
 `industry_category` VARCHAR(200) COMMENT '行业类别',
 `risk_level` TINYINT DEFAULT 0 COMMENT '职业病危害风险分类(0一般、1较重、2严重)',
 `population` INT DEFAULT 0 COMMENT '人员规模(人)',
 `products` VARCHAR(1024) COMMENT '产品名称',
 `yields` VARCHAR(1024) COMMENT '产量信息',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `userid` INT COMMENT '录入人ID',
 `username` VARCHAR(50) COMMENT '录入人姓名',
 `contract_status` TINYINT DEFAULT 0 COMMENT '合作状态(0潜在、1意向、2已合作)',
 `contract_first` DATE COMMENT '首次合作日期',
 `contract_last` DATE COMMENT '最近合作日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='企业信息';

-- t_contract	合同信息表
CREATE TABLE IF NOT EXISTS `t_contract`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `parentid` BIGINT DEFAULT 0 COMMENT '父级ID,一级指令为0，合同隶属：主合同、子合同',
 `identifier` VARCHAR(50) COMMENT '合同编号',
 `company_id` BIGINT NOT NULL COMMENT '受检企业信息表ID',
 `company` VARCHAR(200) NOT NULL COMMENT '受检企业名称',
 `entrust_company_id` BIGINT COMMENT '委托单位，企业信息表ID',
 `entrust_company` VARCHAR(200) COMMENT '委托单位名称',
 `province` VARCHAR(20) COMMENT '省份',
 `city` VARCHAR(50) COMMENT '城市',
 `area` VARCHAR(50) COMMENT '区县',
 `office_address` VARCHAR(200) COMMENT '受检详细地址',
 `contact` VARCHAR(50) COMMENT '联系人',
 `telephone` VARCHAR(50) COMMENT '联系电话',
 `project_name` VARCHAR(200) COMMENT '项目名称',
 `entrust_type` VARCHAR(20) COMMENT '委托类型',
 `type` VARCHAR(20) COMMENT '合同类型',
 `contract_template_id` BIGINT DEFAULT 0 COMMENT '合同模版ID',
 `status` INT DEFAULT 0 COMMENT '合同状态',
 `company_order` VARCHAR(20) COMMENT '项目隶属公司',
 `business_source` VARCHAR(20) COMMENT '杭州隶属(业务来源)',
 `salesmenid` INT COMMENT '业务员ID',
 `salesmen` VARCHAR(20) COMMENT '业务员',
 `total_money` DECIMAL(10,2) DEFAULT 0 COMMENT '合同金额(元)',
 `commission` DECIMAL(10,2) DEFAULT 0 COMMENT '佣金(元)',
 `evaluation_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '评审费(元)',
 `subcontract_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '分包费(元)',
 `service_charge` DECIMAL(10,2) DEFAULT 0 COMMENT '服务费用(元)',
 `other_expenses` DECIMAL(10,2) DEFAULT 0 COMMENT '其他支出(元)',
 `netvalue` DECIMAL(10,2) DEFAULT 0 COMMENT '合同净值(元)',
 `receipt_money` DECIMAL(10,2) DEFAULT 0 COMMENT '已收款金额(元)',
 `invoice_money` DECIMAL(10,2) DEFAULT 0 COMMENT '已开票金额(元)',
 `virtual_tax` DECIMAL(10,2) DEFAULT 0 COMMENT '虚拟税费(元)',
 `commission_date` DATE COMMENT '委托日期',
 `sign_date` DATE COMMENT '合同签订日期',
 `claim_end_date` DATE COMMENT '要求报告完成日期',
 `urgent` TINYINT DEFAULT 0 COMMENT '加急状态(0正常，1较急、2加急)',
 `old` TINYINT DEFAULT 0 COMMENT '新老业务(0新业务，1续签业务)',
 `careof` TINYINT DEFAULT 0 COMMENT '是否为转交业务(0否，1是)',
 `careof_type` TINYINT COMMENT '转交类型(0意向客户，1确定合作客户)',
 `careof_username` VARCHAR(50) COMMENT '转交人',
 `onsite_invest_date` DATE COMMENT '现场调查日期',
 `contract_review_date` DATE COMMENT '合同评审日期',
 `rpt_issuance_date` DATE COMMENT '报告签发日期',
 `rpt_sent_date` DATE COMMENT '报告发送日期',
 `rpt_binding_date` DATE COMMENT '报告装订日期',
 `receipt_date` DATE COMMENT '收款日期',
 `expressnumber` VARCHAR(50) COMMENT '快递单号',
 `key_clauses` VARCHAR(1024) COMMENT '关键条款',
 `plan_remarks` VARCHAR(200) COMMENT '排单备注',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `child_quantity` INT DEFAULT 0 COMMENT '子级数量',
 `userid` INT COMMENT '录入人ID',
 `username` VARCHAR(50) COMMENT '录入人姓名',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同信息';

-- t_contract_template	合同模板共同信息表
CREATE TABLE IF NOT EXISTS `t_contract_template`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `name` VARCHAR(50) COMMENT '模板名称',
 `type` VARCHAR(20) COMMENT '合同类型',
 `file` VARCHAR(100) COMMENT '合同模板路径',
 `status` TINYINT DEFAULT 0 COMMENT '状态(0草稿，1启用、2停用)',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同模板共同信息';

-- t_contract_custom_template	合同模板自定义字段表
CREATE TABLE IF NOT EXISTS `t_contract_custom_template`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_template_id` BIGINT DEFAULT 0 NOT NULL COMMENT '合同模版ID',
 `name_en` VARCHAR(50) NOT NULL COMMENT '字段名称(英文)',
 `name_zh` VARCHAR(50) NOT NULL COMMENT '字段描述(中文)',
 `type` TINYINT DEFAULT 0 COMMENT '状态(0字符，1数值、2单选、2复选)',
 `note` VARCHAR(1024) COMMENT '字段内容',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同模板自定义字段';

-- t_contract_custom_data	合同模板自定义字段数据表
CREATE TABLE IF NOT EXISTS `t_contract_custom_data`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT DEFAULT 0 NOT NULL COMMENT '合同ID',
 `template_custom_id` BIGINT NOT NULL COMMENT '合同模版自定义字段ID',
 `name_en` VARCHAR(50) COMMENT '字段名称(英文)',
 `name_zh` VARCHAR(50) COMMENT '字段描述(中文)',
 `value` VARCHAR(1024) DEFAULT 0 COMMENT '字段的值',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同模板自定义字段数据';

-- t_department	组织架构/部门信息
CREATE TABLE IF NOT EXISTS `t_department`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `parent_id` BIGINT DEFAULT 0 COMMENT '上一级部门ID',
 `department` VARCHAR(20) COMMENT '部门名称',
 `director_id` BIGINT DEFAULT 0 COMMENT '主管人ID',
 `remarks` VARCHAR(200) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='组织架构/部门信息';

-- t_plan	任务排单表
CREATE TABLE IF NOT EXISTS `t_plan`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `department_id` BIGINT COMMENT '所属部门ID',
 `charge_id` BIGINT COMMENT '负责人ID',
 `charge` VARCHAR(50) COMMENT '负责人',
 `status` INT COMMENT '状态',
 `plan_survey_time` DATE COMMENT '计划调查日期',
 `plan_start_time` DATETIME COMMENT '计划采样开始时间',
 `plan_end_time` DATETIME COMMENT '计划采样结束时间',
 `survey_time` DATE COMMENT '实际调查日期',
 `start_time` DATETIME COMMENT '实际采样开始时间',
 `end_time` DATETIME COMMENT '实际采样结束时间',
 `report_survey_time` DATE COMMENT '报告调查日期',
 `report_layout_time` DATE COMMENT '报告采样计划日期',
 `report_start_time` DATETIME COMMENT '报告采样开始时间',
 `report_end_time` DATETIME COMMENT '报告采样结束时间',
 `data_report_time` DATE COMMENT '数据报告出具日期',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id),
 UNIQUE KEY `UK_PLAN_CONTRACT_ID` (`contract_id`) COMMENT '合同ID唯一性'
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='任务排单';

-- t_plan_user	任务人员表
CREATE TABLE IF NOT EXISTS `t_plan_user`(
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `plan_id` BIGINT NOT NULL COMMENT '任务排单ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `type` BIGINT NOT NULL COMMENT '人员类型(1报告人、2现场调查人员、3采样人员)',
 `user_id` BIGINT NOT NULL COMMENT '用户ID',
 `username` VARCHAR(50) NOT NULL COMMENT '用户名',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='任务人员';


-- t_equipment	仪器设备信息记录表
CREATE TABLE IF NOT EXISTS `t_equipment`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `serial_number` VARCHAR(100) COMMENT '内部编号',
 `location` VARCHAR(200) COMMENT '库位',
 `status` TINYINT COMMENT '状态',
 `name` VARCHAR(200) COMMENT '名称',
 `model` VARCHAR(100) COMMENT '规格型号',
 `appearance_num` VARCHAR(200) COMMENT '出场编号',
 `vendor` VARCHAR(200) COMMENT '生产厂家',
 `measure_range` VARCHAR(200) COMMENT '测量范围',
 `accuracy` VARCHAR(200) COMMENT '准确度等级/不确定度',
 `calibrat_cycle` INT COMMENT '检定校准周期(year)',
 `calibrat_org` VARCHAR(200) COMMENT '检定校准部门',
 `calibrat_expire` DATE COMMENT '档案最近检定到期日期',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `buy_date` DATE COMMENT '购买时间(年月日)',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='仪器设备信息记录';

-- t_equipment_reserve	用户预定仪器表
CREATE TABLE IF NOT EXISTS `t_equipment_reserve`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
`user_id` BIGINT NOT NULL COMMENT '用户ID',
`username` VARCHAR(50) NOT NULL COMMENT '用户名',
`start_date` DATE NOT NULL COMMENT '预计使用开始日期',
`end_date` DATE NOT NULL COMMENT '预计使用结束时间',
`ex_date` DATETIME COMMENT '出库时间',
`im_date` DATETIME COMMENT '归还入库时间',
`status` TINYINT DEFAULT 1 COMMENT '状态(1预定，2出库，3入库，4作废)',
`remarks` VARCHAR(1024) COMMENT '备注',
`createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户预定仪器';

-- t_equipment_reserve_rel	用户预定仪器对应关系表
CREATE TABLE IF NOT EXISTS `t_equipment_reserve_rel`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
`reserve_id` BIGINT NOT NULL COMMENT '仪器预定ID',
`equipment_id` BIGINT NOT NULL COMMENT '仪器ID',
`reserve_date` DATE NOT NULL COMMENT '预定日期（多日期分天存储）',
PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用户预定仪器对应关系';

-- t_staff	员工信息
CREATE TABLE IF NOT EXISTS `t_staff`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `department_id` BIGINT DEFAULT 0 COMMENT '上一级部门ID',
 `staff` VARCHAR(50) COMMENT '员工姓名',
 `sex` TINYINT DEFAULT 1 COMMENT '性别(1男、2女)',
 `telephone` VARCHAR(50) COMMENT '联系电话',
 `post` VARCHAR(50) COMMENT '职务(与角色相关)',
 `authority` VARCHAR(100) COMMENT '权限范围',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='员工信息';

-- t_company_survey	用人单位概况调查表
CREATE TABLE IF NOT EXISTS `t_company_survey`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `company` VARCHAR(100) COMMENT '单位名称',
 `identifier` VARCHAR(100) COMMENT '合同编号',
 `project_name` VARCHAR(100) COMMENT '项目名称',
 `office_address` VARCHAR(100) COMMENT '单位地址',
 `contact` VARCHAR(100) COMMENT '联系人',
 `telephone` VARCHAR(50) COMMENT '联系方式',
 `industry_category` VARCHAR(200) COMMENT '所属行业',
 `risk_level` TINYINT DEFAULT 0 COMMENT '职业病危害风险分类(0一般、1较重、2严重)',
 `detection_type` VARCHAR(200) COMMENT '检测类型(评价/定期/其它)',
 `labor_quota` INT COMMENT '劳动定员(人数)',
 `health_sector` TINYINT DEFAULT 0 COMMENT '有无卫生管理部门(0:无, 1:有)',
 `department` VARCHAR(100) COMMENT '部门名称',
 `population` INT COMMENT '人数',
 `inspection_org` VARCHAR(100) COMMENT '职业健康检查机构名称',
 `last_test_time` DATE COMMENT '最近一次职业健康检查时间',
 `product` VARCHAR(1024) COMMENT '产品',
 `yield` VARCHAR(1024) COMMENT '产量',
 `accompany` VARCHAR(50) COMMENT '调查陪同人',
 `survey_date` DATE COMMENT '调查日期(年月日)',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id),
 UNIQUE KEY `UK_COMPANY_SURVEY_CONTRACT_ID` (`contract_id`) COMMENT '合同ID唯一性'
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用人单位概况调查';

-- t_craft_process	工艺流程调查表
CREATE TABLE IF NOT EXISTS `t_craft_process`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `name` VARCHAR(200) COMMENT '工艺流程名称',
 `path` VARCHAR(1024) COMMENT '工艺流程图路径',
 `description` VARCHAR(1024) COMMENT '工艺流程文本描述', 
 `department` VARCHAR(50) COMMENT '部门名称',
 `people_num` TINYINT COMMENT '人数',
 `inspection_org` VARCHAR(100) COMMENT '职业健康检查机构名称',
 `last_test_time` DATE COMMENT '最近一次职业健康检查时间',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工艺流程调查';

-- t_machine	生产设备调查表(现场记录中不展示但是最后报告中需要，数据从设备布局信息等表中获取)
CREATE TABLE IF NOT EXISTS `t_machine`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `department` VARCHAR(100) COMMENT '部门',
 `workshop` VARCHAR(100) COMMENT '车间',
 `name` VARCHAR(100) COMMENT '生产设备名称',
 `model` VARCHAR(100) COMMENT '型号',
 `num` INT COMMENT '数量',
 `status` VARCHAR(50) COMMENT '设备状态',
 `position` VARCHAR(50) COMMENT '具体位置',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='生产设备调查表(现场记录中不展示但是最后报告中需要)';

-- t_material	原辅材料、中间产品、产品情况调查表
CREATE TABLE IF NOT EXISTS `t_material`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `name` VARCHAR(100) COMMENT '物料(原料、辅料、中间品、产品)',
 `consumption` VARCHAR(50) COMMENT '年消耗量/用量',
 `max_storage` VARCHAR(50) COMMENT '最大储存量',
 `state` VARCHAR(50) COMMENT '状态(固态、液体、粉末等)',
 `specs` VARCHAR(50) COMMENT '规格指标/包装规格',
 `component` VARCHAR(200) COMMENT '主要成分',
 `where_use` VARCHAR(50) COMMENT '使用岗位(或场所)',
 `transport` VARCHAR(100) COMMENT '运输方式',
 `storage_mode` VARCHAR(100) COMMENT '存储方式',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='原辅材料、中间产品、产品情况调查';

-- t_equipment_measure	设备布局测点布置图调查表
CREATE TABLE IF NOT EXISTS `t_equipment_measure`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `name` VARCHAR(100) COMMENT '布局图名称',
 `path` VARCHAR(1024) COMMENT '布局图路径',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='设备布局测点布置图调查';

-- t_equipment_layout	设备布局信息表
CREATE TABLE IF NOT EXISTS `t_equipment_layout`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `code` VARCHAR(50) COMMENT '序号',
 `img_id` BIGINT NOT NULL COMMENT '图片id',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `device` VARCHAR(100) COMMENT '设备名称',
 `model` VARCHAR(100) COMMENT '型号',
 `total_num` INT COMMENT '总数量',
 `run_num` INT COMMENT '运行数量',
 `status` VARCHAR(100) COMMENT '设备状态',
 `position` VARCHAR(100) COMMENT '具体位置',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='设备布局信息';

-- t_measure_layout	检测点布置信息表
CREATE TABLE IF NOT EXISTS `t_measure_layout`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
`code` VARCHAR(50) COMMENT '编号',
 `img_id` BIGINT COMMENT '图片id',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `labor_id` BIGINT NOT NULL COMMENT '关联的作业情况信息ID',
 `hazard_id` BIGINT NOT NULL COMMENT '关联的检测地点ID',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '检测地点',
 `replenish_type` TINYINT DEFAULT 0 COMMENT '补充类型(0否，1是补充数据)',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='检测点布置信息';

-- t_labor_operation	劳动者作业情况调查表
CREATE TABLE IF NOT EXISTS `t_labor_operation`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `total_num` INT COMMENT '总数(岗位总人数)',
 `contact_num` INT COMMENT '接触危害人数(默认等于总人数)',
 `run_num` INT COMMENT '人/班(每班岗多少人)',
 `job_content` VARCHAR(1024) COMMENT '工作内容、过程和工作方式、作业地点',
 `hazard_source` VARCHAR(1024) COMMENT '危害因素主要来源',
 `working_time` INT NOT NULL COMMENT '每天工作时长(H/D)',
 `working_days` INT NOT NULL COMMENT '每周工作天数(D/W)',
 `shift_system` VARCHAR(50) COMMENT '工作班制',
 `shielding` VARCHAR(100) COMMENT '个人防护用品',
 `replenish_type` TINYINT DEFAULT 0 COMMENT '补充类型(0否，1是补充数据)',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='劳动者作业情况调查';

-- t_labor_hazard 劳动者作业接害情况
CREATE TABLE IF NOT EXISTS `t_labor_hazard` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `labor_id` BIGINT NOT NULL COMMENT '关联的作业情况信息ID',
  `job_name` VARCHAR(100) COMMENT '工种名称',
  `contact_time` FLOAT NOT NULL COMMENT '接触时间(h/d)',
  `touch_time` VARCHAR(50) COMMENT '接触时间描述(h/d或h/w)',
  `point_num` INT NOT NULL COMMENT '检测点数量',
  `safety_devices` VARCHAR(100) COMMENT '防护措施',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='劳动者作业接害情况';

-- t_labor_hazard_item 劳动者作业接害检测项目信息
CREATE TABLE IF NOT EXISTS `t_labor_hazard_item` (
  `id` BIGINT(10) NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `hazard_id` BIGINT NOT NULL COMMENT '关联的测试点ID',
  `test_id` BIGINT NOT NULL COMMENT '检测物质ID',
  `test_item` VARCHAR(100) NOT NULL COMMENT '检测物质名称',
  `test_alias` VARCHAR(100) COMMENT '检测物质别名',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='劳动者作业接害检测项目信息';

-- t_measures_supplies	职业病防护措施及个人防护用品调查表
CREATE TABLE IF NOT EXISTS `t_measures_supplies`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位(工种)',
 `measures_ventilate` VARCHAR(200) COMMENT '防护措施及通风装置',
 `model_param` VARCHAR(200) COMMENT '防护措施型号、设置参数',
 `num` INT COMMENT '防护措施设置数量',
 `supplies` VARCHAR(100) COMMENT '个人防护用品名称、型号',
 `replace_cycle` VARCHAR(50) COMMENT '个人防护用品更换周期',
 `use_condition` VARCHAR(200) COMMENT '个人防护用品佩戴情况',
 `hazard_factors` VARCHAR(200) COMMENT '防护危害因素',
 `estimate` VARCHAR(200) COMMENT '有效性评价',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='职业病防护措施及个人防护用品调查';

-- t_rescue_warning	应急救援和警示标识等设置情况调查表
CREATE TABLE IF NOT EXISTS `t_rescue_warning`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '工种(岗位)',
 `rescue_facilities` VARCHAR(200) COMMENT '应急救援设施名称、参数',
 `num` INT COMMENT '应急救援设施数量',
 `warning_signs` VARCHAR(200) COMMENT '警示标识',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='应急救援和警示标识等设置情况调查';

-- t_auxiliary_living	建筑卫生学辅助用室及生活用室设置情况调查表
CREATE TABLE IF NOT EXISTS `t_auxiliary_living`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `place` VARCHAR(200) COMMENT '具体地点',
 `toilet_squat_man` INT COMMENT '卫生间男蹲位数量(个)',
 `toilet_urinal` INT COMMENT '卫生间小便池数量(个)',
 `toilet_squat_woman` INT COMMENT '卫生间女蹲位数量(个)',
 `toilet_tap` INT COMMENT '盥洗室水龙头数量(个)',
 `shower_man` INT COMMENT '男淋浴器数量(个)',
 `bath_man` INT COMMENT '男浴池数量(个)',
 `shower_woman` INT COMMENT '女淋浴器数量(个)',
 `bath_woman` INT COMMENT '女浴池数量(个)',
 `lounge_name_num` VARCHAR(100) COMMENT '休闲室名称数量',
 `lounge_drinking` VARCHAR(100) COMMENT '休息室清洁饮水方式',
 `locker_man` INT COMMENT '更衣室更衣柜男数量',
 `locker_woman` INT COMMENT '更衣室更衣柜女数量',
 `locker_mode` VARCHAR(100) COMMENT '更衣室存衣方式',
 `canteen` VARCHAR(200) COMMENT '食堂设置情况',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='建筑卫生学辅助用室及生活用室设置情况调查';

-- t_realistic_work	工作场所劳动者工作日写实调查记录表
CREATE TABLE IF NOT EXISTS `t_realistic_work`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `workshop` VARCHAR(100) COMMENT '车间/工作场所',
 `post` VARCHAR(100) COMMENT '岗位',
 `total_num` INT COMMENT '岗位总人数',
 `max_shift_num` INT COMMENT '最大班人数',
 `shift_system` VARCHAR(100) COMMENT '工作制度',
 `realistic_num` INT COMMENT '写实人数',
 `name` VARCHAR(100) COMMENT '姓名',
 `working_years` INT COMMENT '工龄',
 `job_description` VARCHAR(1024) COMMENT '工作场所及工作内容描述',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工作场所劳动者工作日写实调查记录';

-- t_realistic_record	工作日写实信息记录表
CREATE TABLE IF NOT EXISTS `t_realistic_record`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `realistic_id` BIGINT NOT NULL COMMENT '写实记录ID',
 `working_time` VARCHAR(200) COMMENT '工作时间',
 `place` VARCHAR(100) COMMENT '工作地点',
 `job_content` VARCHAR(200) COMMENT '工作内容',
 `working_hours` VARCHAR(100) COMMENT '耗费工时',
 `hazard_factors` VARCHAR(200) COMMENT '接触职业病危害因素',
 `remakes` VARCHAR(200) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工作日写实信息记录';

-- t_gather_plan	现场采样和检测计划信息表
CREATE TABLE IF NOT EXISTS `t_gather_plan`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `hazard_id` BIGINT  COMMENT '关联的检测地点ID',
 `place` VARCHAR(200) COMMENT '检测地点',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `sample_item` VARCHAR(100) COMMENT '检测项目',
 `sample_type` VARCHAR(100) COMMENT '项目类型(空气、粉尘等字典)',
 `sample_basis` VARCHAR(100) COMMENT '采样及检测依据',
 `equipment` VARCHAR(100) COMMENT '采样设备',
 `mode` TINYINT COMMENT '采样方式(0:无,1定点,2个人)',
 `start_time` DATETIME COMMENT '采样开始时间',
 `end_time` DATETIME COMMENT '采样结束时间',
 `flow` VARCHAR(50) COMMENT '采样流量(L/min)',
 `collector` VARCHAR(50) COMMENT '收集器',
 `sample_num` INT COMMENT '样品数量',
 `sample_blank` TINYINT DEFAULT 0 COMMENT '空白样(0否、1是)',
 `sample_code` VARCHAR(100) COMMENT '样品编号',
 `storage` VARCHAR(100) COMMENT '样品保存',
 `substance_id` BIGINT NOT NULL COMMENT '检测物质数据表ID',
 `gist_method_id` BIGINT NOT NULL COMMENT '检测法规依据数据表ID',
 `replenish_type` TINYINT DEFAULT 0 COMMENT '补充类型(0否，1是补充数据)',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='现场采样和检测计划信息';


-- t_gather_point	采样和检测布点图
CREATE TABLE IF NOT EXISTS `t_gather_point`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `workshop` VARCHAR(200) COMMENT '车间名称',
 `layout` VARCHAR(100) COMMENT '布局图',
 `point_code` INT COMMENT '采集点编号',
 `point_name` VARCHAR(100) COMMENT '检测地点',
 `project` VARCHAR(200) COMMENT '检测项目',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='采样和检测布点图';

-- t_substance	检测物质数据表
CREATE TABLE IF NOT EXISTS `t_substance`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `name` VARCHAR(200) COMMENT '名称',
 `name_en` VARCHAR(200) COMMENT '英文名',
 `cas_no` VARCHAR(200) COMMENT '化学文摘号CAS No.',
 `mac` FLOAT COMMENT '最高容许浓度(mg/ m³)',
 `pc_twa` FLOAT COMMENT '时间加权平均容许浓度(mg/ m³)',
 `pc_stel` FLOAT COMMENT '短时间接触容许浓度(mg/ m³)',
 `max_limit` FLOAT COMMENT '最大超限倍数',
 `remaks` VARCHAR(200) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='检测物质数据';

-- t_gist_method	检测法规依据数据表
CREATE TABLE IF NOT EXISTS `t_gist_method`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `substance_id` BIGINT NOT NULL COMMENT '检测物质ID',
 `type` TINYINT COMMENT '采样方式(0:无,1定点,2个人)',
 `sample_tablename` VARCHAR(100) COMMENT '此采样数据记录在哪张表中',
 `certification` VARCHAR(200) COMMENT '计量认证名称',
 `basis` VARCHAR(200) COMMENT '采样及检测依据',
 `basis_old` VARCHAR(200) COMMENT 'basis的原依据标准',
 `basis_name` VARCHAR(500) COMMENT '检测依据的名称',		
 `equipment` VARCHAR(200) COMMENT '采样设备',
 `flow` VARCHAR(50) COMMENT '采样流量',
 `test_time` VARCHAR(50) COMMENT '采样时间(描述)',
 `collector` VARCHAR(200) COMMENT '收集器',
 `preserve_traffic` VARCHAR(200) COMMENT '保存/运输方式',
 `preserve_require` VARCHAR(200) COMMENT '样品保存要求',
 `shelf_life` INT COMMENT '样品保存期限(天)',
 `blank_sample` VARCHAR(200) COMMENT '空白样要求',
 `balnk_num` INT COMMENT '空白样数量',
 `method` VARCHAR(200) COMMENT '检测方法',
 `authentication` VARCHAR(100) COMMENT '是否通过计量认证(空,A,D,B(18.7))',
 `min_detection` DOUBLE COMMENT '最低检出值',
 `sample_volume` VARCHAR(100) COMMENT '采样体积',
 `method_num` INT COMMENT '多种采样方法',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='检测法规依据数据';

-- t_law	法律依据数据
CREATE TABLE IF NOT EXISTS `t_law` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
  `law_name` varchar(500)  DEFAULT NULL COMMENT '法律名称',
  `descriptions` VARCHAR(200) DEFAULT NULL COMMENT '法律依规实施说明',
  `defvalue` TINYINT DEFAULT 0 COMMENT '默认选择(0:无,1选中)',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='法律依据数据';

-- t_law_contract	法律法规依据对应合同关系
CREATE TABLE IF NOT EXISTS `t_law_contract`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `law_id` BIGINT NOT NULL COMMENT '法律法规依据表ID',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='法律法规依据对应合同关系';

-- t_project_archive	项目归档文件目录表
CREATE TABLE IF NOT EXISTS `t_project_archive`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT COMMENT '合同ID',
 `name` VARCHAR(200) COMMENT '案卷名称',
 `archive_num` VARCHAR(200) COMMENT '档案编号',
 `order_num` INT COMMENT '序号',
 `charge` VARCHAR(200) COMMENT '责任者',
 `code` VARCHAR(200) COMMENT '文号',
 `title` VARCHAR(200) COMMENT '标题名称',
 `report_date` VARCHAR(200) COMMENT '日期',
 `page` VARCHAR(200) COMMENT '页码',
 `note` VARCHAR(200) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目归档文件目录';

-- t_linkman	企业联系人信息
CREATE TABLE IF NOT EXISTS `t_linkman`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `companyid` BIGINT COMMENT '企业信息表ID',
 `name` VARCHAR(20) COMMENT '联系人',
 `telephone` VARCHAR(20) COMMENT '联系电话',
 `remarks` VARCHAR(200) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='企业联系人信息';

-- -------- 报告技术审核记录 ----------
-- t_report_review	报告技术审核记录表
CREATE TABLE IF NOT EXISTS `t_report_review`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `charger_id` BIGINT COMMENT '负责人ID',
 `charger` VARCHAR(100) COMMENT '负责人',
 `writer_id` BIGINT COMMENT '编写人ID',
 `writer` VARCHAR(100) COMMENT '编写人',
 `other_content` VARCHAR(1024) COMMENT '其它审核内容',
 `problem` VARCHAR(1024) COMMENT '问题描述',
 `reviewer_id` BIGINT COMMENT '审核人ID',
 `reviewer` VARCHAR(100) COMMENT '审核人',
 `review_date` DATE COMMENT '审核日期',
 `modification` VARCHAR(1024) COMMENT '修改情况',
 `reviser_id` BIGINT COMMENT '修改人ID',
 `reviser` VARCHAR(100) COMMENT '修改人',
 `revise_date` DATE COMMENT '修改日期',
 `opinion` VARCHAR(1024) COMMENT '审核意见',
 `director_id` BIGINT COMMENT '技术负责人ID',
 `director` VARCHAR(100) COMMENT '技术负责人',
 `over_time` DATE COMMENT '审核结束日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='报告技术审核记录表';

-- t_report_proofread	报告技术校核记录表
CREATE TABLE IF NOT EXISTS `t_report_proofread`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `charger_id` BIGINT COMMENT '负责人ID',
 `charger` VARCHAR(100) COMMENT '负责人',
 `writer_id` BIGINT COMMENT '编写人ID',
 `writer` VARCHAR(100) COMMENT '编写人',
 `other_content` VARCHAR(1024) COMMENT '其它审核内容',
 `problem` VARCHAR(1024) COMMENT '问题描述',
 `checker_id` BIGINT COMMENT '校核人ID',
 `checker` VARCHAR(100) COMMENT '校核人',
 `check_date` DATE COMMENT '校核时间',
 `modification` VARCHAR(1024) COMMENT '修改情况',
 `reviser_id` BIGINT COMMENT '修改人ID',
 `reviser` VARCHAR(100) COMMENT '修改人',
 `revise_date` DATE COMMENT '修改人签字日期',
 `opinion` VARCHAR(1024) COMMENT '审核意见',
 `director_id` BIGINT COMMENT '质控负责人ID',
 `director` VARCHAR(100) COMMENT '质控负责人',
 `over_time` DATE COMMENT '质控签字日期',
 `suggestion` VARCHAR(1024) COMMENT '签发意见',
 `issuer_id` BIGINT COMMENT '签发人ID',
 `issuer` VARCHAR(100) COMMENT '签发人',
 `issuer_time` DATE COMMENT '签发时间',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='报告技术校核记录表';

-- t_report_review_dict	报告技术审核项对应关系表
CREATE TABLE IF NOT EXISTS `t_report_review_dict`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `sys_dict_code` VARCHAR(50) NOT NULL COMMENT '技术审核项对应字典code',
 `value` TINYINT  COMMENT '选中的值(0否、1是)',
 `type` VARCHAR(50) NOT NULL COMMENT '审核reportReview；校核reportProofread',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='报告技术审核项对应关系';

-- t_report_improve 报告信息完善记录表
CREATE TABLE IF NOT EXISTS `t_report_improve` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `contract_id` bigint DEFAULT NULL COMMENT '合同ID（唯一索引）',
  `source` varchar(200) DEFAULT NULL COMMENT '业务来源',
  `test_type` varchar(200) DEFAULT NULL COMMENT '检测类别',
  `test_range` varchar(1024) DEFAULT NULL COMMENT '检测范围',
  `post_setting` varchar(1024) DEFAULT NULL COMMENT '劳动定员及工种（岗位）设置情况',
  `production` varchar(1024) DEFAULT NULL COMMENT '检查时的生产情况',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `contract_id` (`contract_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告信息完善记录';

-- -------- 报告技术审核记录 ----------

-- t_account 收付款记录表
CREATE TABLE IF NOT EXISTS `t_account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `contract_id` int NOT NULL COMMENT '合同ID(唯一索引)',
  `type` tinyint DEFAULT NULL COMMENT '款项类别',
  `happen_time` date DEFAULT NULL COMMENT '收/付时间',
  `amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '收/付款金额(元)',
  `settle_style` tinyint DEFAULT NULL COMMENT '结算方式',
  `invoice_amount` decimal(10,2) NOT NULL DEFAULT 0.00 COMMENT '开票金额(元)',
  `virtual_tax` DECIMAL(10,2) DEFAULT 0 COMMENT '虚拟税费(元)',
  `invoice_number` varchar(100)  COMMENT '发票号码',
  `remarks` varchar(500)  COMMENT '备注',
  `userid` bigint DEFAULT NULL COMMENT '录入人ID',
  `username` varchar(50)  COMMENT '录入人姓名',
  `editor_id` bigint DEFAULT NULL COMMENT '修改人ID',
  `editor_name` varchar(50)  COMMENT '修改人姓名',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收付款记录';

-- t_contract_review 合同评审表
CREATE TABLE IF NOT EXISTS `t_contract_review`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
  `project_name` VARCHAR(200) COMMENT '项目名称',
 `entrust` VARCHAR(200) COMMENT '委托单位名称',
 `contact` VARCHAR(50) COMMENT '联系人',
 `mobile` VARCHAR(50) COMMENT '联系电话(手机)',
 `entrust_type` VARCHAR(50) COMMENT '项目类型',
 `test_item` VARCHAR(1024) COMMENT '主要检测项目',
 `remarks` VARCHAR(500) COMMENT '其他要求',
  `review_conclusion` INT DEFAULT 1 COMMENT '评审结论（0合同不可行,1合同可行）',
  `is_review` INT DEFAULT 0 COMMENT '是否通过评审（0未通过，1通过）',
  `charge_opinion` VARCHAR(200) COMMENT '评审负责人意见及签名',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同评审表';

-- t_contract_review_opinion  评审意见表
CREATE TABLE IF NOT EXISTS `t_contract_review_opinion`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_review_id` BIGINT NOT NULL COMMENT '合同合同评审表ID',
  `duty` VARCHAR(200) COMMENT '部门/职务',
 `review_comment` INT COMMENT '评审意见（0不同意,1同意）',
 `signatory` VARCHAR(200) COMMENT '签字人',
 `signatory_date` DATE COMMENT '签字时间',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评审意见表';

-- t_contract_review_dict  合同评审项对应关系表
CREATE TABLE IF NOT EXISTS `t_contract_review_dict`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_review_id` BIGINT NOT NULL COMMENT '合同合同评审表ID',
 `sys_dict_code` VARCHAR(50) NOT NULL COMMENT '合同评审项对应字典表Code',
 `name` VARCHAR(200) COMMENT '评审项目名称',
 `value` TINYINT DEFAULT 1 COMMENT '选中的值(0否、1是)',
 `remarks` VARCHAR(200) COMMENT '备注',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同评审项对应关系表';


-- t_quotation	报价记录表
CREATE TABLE IF NOT EXISTS `t_quotation`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `company_id` BIGINT NOT NULL COMMENT '企业ID',
 `company` VARCHAR(200) NOT NULL COMMENT '企业名称',
 `type` VARCHAR(200) NOT NULL COMMENT '报价项目类型',
 `project` VARCHAR(200) COMMENT '项目名称',
 `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
 `amount_upper` VARCHAR(200) NOT NULL COMMENT '金额大写',
 `userid` BIGINT NOT NULL COMMENT '报价人ID',
 `contact` VARCHAR(50) NOT NULL COMMENT '联系人',
 `mobile` VARCHAR(50) NOT NULL COMMENT '联系电话(手机)',
 `quotation_date` DATE NOT NULL COMMENT '报价日期',
 `payment` VARCHAR(1024) NOT NULL COMMENT '开票金额(元)',
 `cooperation` INT NOT NULL DEFAULT 0 COMMENT '是否合作(0:未结束  1:合作成功  2:合作失败)',
 `reason` VARCHAR(1024) COMMENT '合作失败的原因或其它备注',
 `enclosure` VARCHAR(200) COMMENT '附件路径(word附件)',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='报价记录';


-- t_quotation_item	报价项目信息表
CREATE TABLE IF NOT EXISTS `t_quotation_item`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `quotation_id` BIGINT NOT NULL COMMENT '报价记录ID',
 `num` VARCHAR(20) COMMENT '序号',
 `parent_num` VARCHAR(20) COMMENT '主序号',
 `name` VARCHAR(200) COMMENT '报价项目名称',
 `price` DECIMAL(10,2) COMMENT '价格',
 `unit` VARCHAR(20) COMMENT '价格单位(元/万元)',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='报价项目信息表';


-- -------- 样品采集记录信息 ----------

