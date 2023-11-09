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
 `status` INT DEFAULT 0 COMMENT '合同状态(0录入，1签订)',
 `contract_status` INT DEFAULT 0 COMMENT '合同签订状态(0未回，1已回)',
 `deal_status` INT DEFAULT 0 COMMENT '协议签订状态(0未回，1已回)',
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
 `netvalue` DECIMAL(10,2) DEFAULT 0 COMMENT '项目净值(元)',
 `commission_date` DATE COMMENT '委托日期',
 `sign_date` DATE COMMENT '合同签订日期',
 `urgent` TINYINT DEFAULT 0 COMMENT '加急状态(0正常，1较急、2加急)',
 `old` TINYINT DEFAULT 0 COMMENT '新老业务(0新业务，1续签业务)',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `userid` INT COMMENT '录入人ID',
 `username` VARCHAR(50) COMMENT '录入人姓名',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id),
 UNIQUE KEY `UK_CONTRACT_IDENTIFIER` (`identifier`) COMMENT '合同编号唯一性'
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='合同信息';


-- t_project	项目信息表
CREATE TABLE IF NOT EXISTS `t_project`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `parentid` BIGINT DEFAULT 0 COMMENT '父级ID,一级指令为0，项目隶属：主项目、子项目',
 `identifier` VARCHAR(50) COMMENT '项目编号',
 `contract_id` BIGINT  COMMENT '合同ID',
  `dept_id` BIGINT COMMENT '所属部门ID',
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
 `type` VARCHAR(20) COMMENT '项目类型',
 `status` INT DEFAULT 0 COMMENT '项目状态',
 `company_order` VARCHAR(20) COMMENT '项目隶属公司',
 `business_source` VARCHAR(20) COMMENT '杭州隶属(业务来源)',
 `salesmenid` INT COMMENT '业务员ID',
 `salesmen` VARCHAR(20) COMMENT '业务员',
 `total_money` DECIMAL(10,2) DEFAULT 0 COMMENT '项目金额(元)',
 `commission` DECIMAL(10,2) DEFAULT 0 COMMENT '佣金(元)',
 `evaluation_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '评审费(元)',
 `subproject_fee` DECIMAL(10,2) DEFAULT 0 COMMENT '分包费(元)',
 `service_charge` DECIMAL(10,2) DEFAULT 0 COMMENT '服务费用(元)',
 `other_expenses` DECIMAL(10,2) DEFAULT 0 COMMENT '其他支出(元)',
 `netvalue` DECIMAL(10,2) DEFAULT 0 COMMENT '项目净值(元)',
 `receipt_money` DECIMAL(10,2) DEFAULT 0 COMMENT '已收款金额(元)',
 `invoice_money` DECIMAL(10,2) DEFAULT 0 COMMENT '已开票金额(元)',
 `virtual_tax` DECIMAL(10,2) DEFAULT 0 COMMENT '虚拟税费(元)',
 `commission_date` DATE COMMENT '委托日期',
 `sign_date` DATE COMMENT '项目签订日期',
 `claim_end_date` DATE COMMENT '要求报告完成日期',
 `urgent` TINYINT DEFAULT 0 COMMENT '加急状态(0正常，1较急、2加急)',
 `old` TINYINT DEFAULT 0 COMMENT '新老业务(0新业务，1续签业务)',
 `careof` TINYINT DEFAULT 0 COMMENT '是否为转交业务(0否，1是)',
 `careof_type` TINYINT COMMENT '转交类型(0意向客户，1确定合作客户)',
 `careof_username` VARCHAR(50) COMMENT '转交人',
 `onsite_invest_date` DATE COMMENT '现场调查日期',
 `project_review_date` DATE COMMENT '项目评审日期',
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
 PRIMARY KEY (id),
 UNIQUE KEY `UK_PROJECT_IDENTIFIER` (`identifier`) COMMENT '项目编号唯一性'
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目信息';

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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `department_id` BIGINT COMMENT '所属部门ID',
 `charge_id` BIGINT COMMENT '负责人ID',
 `charge` VARCHAR(50) COMMENT '负责人',
 `charge_job_num` VARCHAR(100) COMMENT '负责人工号',
 `is_supplement` INT DEFAULT 0 COMMENT '是否补采(0否,1是)',
 `status` INT COMMENT '状态',
 `project_type` VARCHAR(20) COMMENT '项目类型',
 `plan_survey_time` DATE COMMENT '计划调查日期',
 `plan_start_time` DATETIME COMMENT '计划采样开始时间',
 `plan_end_time` DATETIME COMMENT '计划采样结束时间',
 `task_release_time` DATETIME COMMENT '任务下发时间',
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
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='任务排单';

-- t_plan_user	任务人员表
CREATE TABLE IF NOT EXISTS `t_plan_user`(
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `plan_id` BIGINT NOT NULL COMMENT '任务排单ID',
 `artisan_id` BIGINT NOT NULL COMMENT '技术人员记录ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `type` BIGINT NOT NULL COMMENT '人员类型(0:实际采样组员  1报告签字人员   2报告审核人员)',
 `job_type` VARCHAR(50) COMMENT '任务工作类型(调查，采样)',
 `userid` BIGINT NOT NULL COMMENT '人员用户ID',
 `username` VARCHAR(50) NOT NULL COMMENT '人员用户名',
 `job_num` VARCHAR(100) COMMENT '人员工号',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
  `hazard_num` INT COMMENT '职业病危害接触人数',
 `inspection_org` VARCHAR(100) COMMENT '职业健康检查机构名称',
 `last_test_time` DATE COMMENT '最近一次职业健康检查时间',
 `product` VARCHAR(1024) COMMENT '产品',
 `yield` VARCHAR(1024) COMMENT '产量',
 `accompany` VARCHAR(50) COMMENT '调查陪同人',
 `survey_date` DATE COMMENT '调查日期(年月日)',
 `test_nature` VARCHAR(255) COMMENT '检测性质',
 `test_place` VARCHAR(1024) COMMENT '检测与评价场所',
 `test_date` VARCHAR(255) COMMENT '采样时间',
 `test_items` VARCHAR(1024) COMMENT '主要检测项目',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id),
 UNIQUE KEY `UK_COMPANY_SURVEY_PROJECT_ID` (`project_id`) COMMENT '项目ID唯一性'
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='用人单位概况调查';

-- t_craft_process	工艺流程调查表
CREATE TABLE IF NOT EXISTS `t_craft_process`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `corresponding_product` VARCHAR(255) COMMENT '对应产品/对应工艺',
 `name` VARCHAR(100) COMMENT '物料(原料、辅料、中间品、产品)',
 `consumption` VARCHAR(50) COMMENT '年消耗量/用量',
 `max_storage` VARCHAR(50) COMMENT '最大储存量',
 `state` VARCHAR(50) COMMENT '状态(固态、液体、粉末等)',
 `specs` VARCHAR(50) COMMENT '规格指标/包装规格',
 `component` VARCHAR(200) COMMENT '主要成分',
 `where_use` VARCHAR(50) COMMENT '使用岗位(或场所)',
 `transport` VARCHAR(100) COMMENT '运输方式',
 `storage_mode` VARCHAR(100) COMMENT '存储方式',
 `storage_facilities` VARCHAR(100) COMMENT '储存场所',
 `storage_cycle` VARCHAR(100) COMMENT '储存周期',
 `external_transportation` VARCHAR(100) COMMENT '厂外运输方式',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='原辅材料、中间产品、产品情况调查';

-- t_equipment_measure	设备布局测点布置图调查表
CREATE TABLE IF NOT EXISTS `t_equipment_measure`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
`code` INT COMMENT '编号',
 `img_id` BIGINT COMMENT '图片id',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `labor_id` BIGINT NOT NULL COMMENT '关联的作业情况信息ID',
 `hazard_id` BIGINT NOT NULL COMMENT '关联的检测地点ID',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '检测地点',
 `replenish_type` TINYINT DEFAULT 0 COMMENT '补充类型(0否，1是补充数据)',
 `status` INT DEFAULT 0 COMMENT '生成采样计划状态(0未生成，1已生成)', 
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='检测点布置信息';

-- t_labor_operation	劳动者作业情况调查表
CREATE TABLE IF NOT EXISTS `t_labor_operation`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `total_num` INT DEFAULT 0 COMMENT '总数(岗位总人数)',
 `contact_num` INT DEFAULT 0 COMMENT '接触危害人数(默认等于总人数)',
 `run_num` INT DEFAULT 0 COMMENT '人/班(每班岗多少人)',
 `job_content` VARCHAR(1024) COMMENT '工作内容、过程和工作方式、作业地点',
 `hazard_source` VARCHAR(1024) COMMENT '危害因素主要来源',
 `working_time` INT NOT NULL COMMENT '每天工作时长(H/D)',
 `working_days` INT NOT NULL COMMENT '每周工作天数(D/W)',
 `shift_system` VARCHAR(50) COMMENT '工作班制',
 `shielding` VARCHAR(100) COMMENT '个人防护用品',
  `ht_discern` INT DEFAULT 0 COMMENT '高温识别不检测（0否，1是）',
 `replenish_type` TINYINT DEFAULT 0 COMMENT '补充类型(0否，1是补充数据)',
 `status` INT DEFAULT 0 COMMENT '生成采样计划状态(0未生成，1已生成)',
 `state` INT DEFAULT 0 COMMENT '生成工作写实(0未生成，1已生成)',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `hazard_id` BIGINT  COMMENT '关联的检测地点ID',
 `place` VARCHAR(200) COMMENT '检测地点',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `measure_layout_code` INT COMMENT '检测点布置信息编号',
 `sample_item` VARCHAR(100) COMMENT '检测项目',
 `sample_type` VARCHAR(100) COMMENT '项目类型(空气、粉尘等字典)',
 `sample_basis` VARCHAR(100) COMMENT '采样及检测依据',
 `equipment` VARCHAR(100) COMMENT '采样设备',
 `mode` TINYINT COMMENT '采样方式(0:无,1定点,2个人)',
 `start_time` DATETIME COMMENT '采样开始时间',
 `end_time` DATETIME COMMENT '采样结束时间',
 `flow` DECIMAL(10,2) COMMENT '采样流量(L/min)',
 `test_time` VARCHAR(20) COMMENT '采样时间(min)',
 `run_num` INT COMMENT '人/班(每班岗多少人)',
 `collector` VARCHAR(50) COMMENT '收集器',
 `sample_num` INT COMMENT '样品数量',
 `sample_blank` TINYINT DEFAULT 0 COMMENT '空白样(0否、1是)',
 `sample_code` VARCHAR(100) COMMENT '样品编号',
 `sample_code_kb` VARCHAR(100) COMMENT '样品空白样编号', 
 `storage` VARCHAR(100) COMMENT '样品保存',
 `substance_id` BIGINT NOT NULL COMMENT '检测物质数据表ID',
 `substance_ids` VARCHAR(100) NULL   COMMENT '检测物质ID数组,供多物质时使用',
 `gist_method_id` BIGINT NOT NULL COMMENT '检测法规依据数据表ID',
 `replenish_type` TINYINT DEFAULT 0 COMMENT '补充类型(0否，1是补充数据)',
 `generate_sample_status` TINYINT DEFAULT 1 COMMENT '生成采样记录状态(1未生成,2需重新生成,3已生成)',
 `remarks` VARCHAR(1024) COMMENT '备注',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='现场采样和检测计划信息';


-- t_gather_point	采样和检测布点图
CREATE TABLE IF NOT EXISTS `t_gather_point`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
  `classify` INT COMMENT '类型',
  `reaction` VARCHAR(500) COMMENT '临界不良反应',
  `deduction` BOOLEAN DEFAULT 1  NULL   COMMENT '是否折算(0否,1是)',
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
 `flow` DECIMAL(10,2) COMMENT '采样流量(L/min)',
 `test_time` VARCHAR(50) COMMENT '采样时间(min)',
 `collector` VARCHAR(200) COMMENT '收集器',
 `preserve_traffic` VARCHAR(200) COMMENT '保存/运输方式',
 `preserve_require` VARCHAR(200) COMMENT '样品保存要求',
 `shelf_life` INT COMMENT '样品保存期限(天)',
 `blank_sample` VARCHAR(200) COMMENT '空白样要求',
 `blank_num` INT COMMENT '空白样数量',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `law_id` BIGINT NOT NULL COMMENT '法律法规依据表ID',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='法律法规依据对应合同关系';

-- t_project_archive	项目归档文件目录表
CREATE TABLE IF NOT EXISTS `t_project_archive`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `sys_dict_code` VARCHAR(50) NOT NULL COMMENT '技术审核项对应字典code',
 `value` TINYINT  COMMENT '选中的值(0否、1是)',
 `type` VARCHAR(50) NOT NULL COMMENT '审核reportReview；校核reportProofread',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='报告技术审核项对应关系';

-- t_report_improve 报告信息完善记录表
CREATE TABLE IF NOT EXISTS `t_report_improve` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `project_id` bigint COMMENT '项目ID（唯一索引）',
  `source` varchar(200) COMMENT '业务来源',
  `test_type` varchar(200) COMMENT '检测类别',
  `test_range` varchar(1024) COMMENT '检测范围',
  `post_setting` varchar(1024) COMMENT '劳动定员及工种（岗位）设置情况',
  `production` varchar(1024) COMMENT '检查时的生产情况',   
  `problems` TEXT COMMENT '存在的问题',
  `frequency` TEXT COMMENT '检测频次说明',
  `summary` TEXT COMMENT '检测结果评价小结';
  `report_name` varchar(255) COMMENT '报告名称',
  `report_url` varchar(255) COMMENT '报告路径',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报告信息完善记录';

-- -------- 报告技术审核记录 ----------

-- t_account 收付款记录表
CREATE TABLE IF NOT EXISTS `t_account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `project_id` int NOT NULL COMMENT '项目ID(唯一索引)',
  `ac_type` tinyint DEFAULT NULL COMMENT '款项类别',
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

-- t_project_review 合同评审表
CREATE TABLE IF NOT EXISTS `t_project_review`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
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

-- t_project_review_opinion  评审意见表
CREATE TABLE IF NOT EXISTS `t_project_review_opinion`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_review_id` BIGINT NOT NULL COMMENT '合同合同评审表ID',
  `duty` VARCHAR(200) COMMENT '部门/职务',
 `review_comment` INT COMMENT '评审意见（0不同意,1同意）',
 `signatory` VARCHAR(200) COMMENT '签字人',
 `signatory_date` DATE COMMENT '签字时间',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评审意见表';

-- t_project_review_dict  合同评审项对应关系表
CREATE TABLE IF NOT EXISTS `t_project_review_dict`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_review_id` BIGINT NOT NULL COMMENT '合同合同评审表ID',
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
 `reason` VARCHAR(1024) COMMENT '合作失败的原因',
 `enclosure` VARCHAR(200) COMMENT '附件路径(word附件)',
 `remarks` VARCHAR(1024) COMMENT '备注',
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


-- t_artisan	技术人员信息表
CREATE TABLE IF NOT EXISTS `t_artisan`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `userid` BIGINT NOT NULL COMMENT '技术人员ID',
 `type` INT NOT NULL COMMENT '技术类型',
 `job_num` VARCHAR(100) COMMENT '工号',
 `username` VARCHAR(100) COMMENT '技术人员名称',
 `dept_id` BIGINT COMMENT '部门ID',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='技术人员信息表';

-- t_artisan_task	技术人员排单记录表
CREATE TABLE IF NOT EXISTS `t_artisan_task`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `artisan_id` BIGINT NOT NULL COMMENT '技术人员记录ID',
 `plan_id` BIGINT NOT NULL COMMENT '任务排单ID',
 `task_date` DATE COMMENT '排单日期',
 `job_type` VARCHAR(50) COMMENT '任务工作类型(调查，采样)',
 `province` VARCHAR(20) COMMENT '省份',
 `city` VARCHAR(50) COMMENT '城市',
 `area` VARCHAR(50) COMMENT '区县',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='技术人员排单记录表';


-- t_company_contact	公司联系人表
CREATE TABLE IF NOT EXISTS `t_company_contact`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
`company_id` BIGINT NOT NULL COMMENT '企业ID',
 `contact` VARCHAR(50) COMMENT '联系人',
 `mobile` VARCHAR(50) COMMENT '联系方式',
 `telephone` VARCHAR(50) COMMENT '固定电话',
 `email` VARCHAR(100) COMMENT '邮箱',
 `type` VARCHAR(50) COMMENT '联系人类型',
 `is_default` INT DEFAULT 0 COMMENT '是否默认(0否，1是)',
  `remark` VARCHAR(200) COMMENT '备注',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='公司联系人表';

-- t_sales_target	业务员销售目标表
CREATE TABLE IF NOT EXISTS `t_sales_target`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
`job_num` VARCHAR(100) COMMENT '工号',
 `userid` BIGINT  COMMENT '业务员ID',
 `username` VARCHAR(100) COMMENT '业务员名称',
 `type` VARCHAR(10) NOT NULL DEFAULT 'W' COMMENT '日期类型(年Y,月M,周W,日D)',
 `sales_date` VARCHAR(20) NOT NULL  COMMENT '销售日期',
 `sales_target` DECIMAL(15,2) NOT NULL DEFAULT 0 COMMENT '销售目标金额(元)',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='业务员销售目标';

-- t_project_procedures	项目流程表 
CREATE TABLE IF NOT EXISTS `t_project_procedures`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `status` INT NOT NULL COMMENT '项目状态',
 `statusname` VARCHAR(100) COMMENT '项目状态名称',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目流程表';


-- t_division_target	事业部业绩目标表
CREATE TABLE IF NOT EXISTS `t_division_target`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
`job_num` VARCHAR(100) COMMENT '工号',
 `userid` BIGINT  COMMENT '技术员ID',
 `username` VARCHAR(100) COMMENT '技术员名称',
 `dept_id` BIGINT COMMENT '所属部门ID',
 `type` VARCHAR(10) NOT NULL DEFAULT 'W' COMMENT '日期类型(年Y,月M,周W,日D)',
 `division_date` VARCHAR(20) NOT NULL  COMMENT '业绩日期',
 `division_target` DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '业绩目标金额(元)',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='事业部业绩目标表';


-- t_commission	提成记录表
CREATE TABLE IF NOT EXISTS `t_commission`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
`project_id` BIGINT NOT NULL COMMENT '项目ID',
 `type` VARCHAR(255) NOT NULL COMMENT '提成类型',
 `state` INT NOT NULL DEFAULT 1 COMMENT '状态(1未提成 2已提成 3异常)',
`cms_amount` DECIMAL(15,2) NOT NULL COMMENT '提成金额(元)',
 `personnel` VARCHAR(255) NOT NULL COMMENT '提成人(可能是多个)',
 `subjection` VARCHAR(255) COMMENT '隶属公司',
 `commission_date` DATE COMMENT '提成日期',
 `count_date` DATE COMMENT '统计日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='提成记录表';


-- t_category 类型信息记录表
CREATE TABLE `t_category` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `pid` bigint COMMENT '父级类别ID，一级类别为0',
  `name` varchar(255) COMMENT '类型名称',
   `module` varchar(255) COMMENT '模块',
 `name_en` varchar(50) COMMENT '英文缩写',
  `order_num` int COMMENT '排序',
  `del_flag` tinyint DEFAULT 0 COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='类型信息记录';

-- t_detection 检测情况表
CREATE TABLE `t_detection`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `project_id` BIGINT NOT NULL COMMENT '项目ID',
  `sample_name` VARCHAR(255) DEFAULT '' COMMENT '样品名称',
  `sample_num` INT DEFAULT 0 COMMENT '样品数量',
  `sample_character` VARCHAR(500) DEFAULT '' COMMENT '样品性状',
  `sample_date` VARCHAR(255) DEFAULT '' COMMENT '采样日期',
  `receipt_date` VARCHAR(255) DEFAULT '' COMMENT '接收日期',
  `test_date` VARCHAR(255) DEFAULT '' COMMENT '测试日期',
  `report_date` DATE COMMENT '报告日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='检测情况表(记录检测相关的总统信息)'; 

-- t_sample_basis_equip 检测依据及设备表
CREATE TABLE `t_sample_basis_equip`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `project_id` BIGINT NOT NULL COMMENT '项目ID',
  `test_item` VARCHAR(255) DEFAULT '' COMMENT '检测物质',
  `item_cate` INT DEFAULT 0 COMMENT '物质类型(0:无,1定点,2个体)',
  `test_basis` VARCHAR(500) DEFAULT '' COMMENT '检测依据',
  `basis_name` VARCHAR(500) DEFAULT '' COMMENT '检测依据', 
  `test_method` VARCHAR(500) DEFAULT '' COMMENT '检测方法',
  `equip` VARCHAR(500) DEFAULT '' COMMENT '检测仪器及编号',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='检测依据及设备表';


-- t_protection_evaluation 个人防护用品有效性评价
CREATE TABLE `t_protection_evaluation`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `project_id` BIGINT NOT NULL COMMENT '项目ID',
  `protect_equip` VARCHAR(255) DEFAULT '' COMMENT '防护用品',
  `model` VARCHAR(255) DEFAULT '' COMMENT '型号',
  `params` VARCHAR(255) DEFAULT '' COMMENT '参数',
  `hazard_factors` VARCHAR(500) DEFAULT '' COMMENT '危害因素',
  `evaluate` VARCHAR(500) DEFAULT '' COMMENT '有效性评价',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='个人防护用品有效性评价';


-- t_conclusion 结论
CREATE TABLE `t_conclusion`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `project_id` BIGINT NOT NULL COMMENT '项目ID',
  `hazard_id` bigint(12) NOT NULL COMMENT '关联的接害情况ID', 
  `place` VARCHAR(255) DEFAULT '' COMMENT '地点',
  `workshop` VARCHAR(255) DEFAULT '' COMMENT '车间',
  `post` VARCHAR(255) DEFAULT '' COMMENT '岗位',
  `professiona` VARCHAR(255) DEFAULT '' COMMENT '工种',
  `measure_layout_code` INT COMMENT '检测点布置信息编号',
  `test_item` VARCHAR(255) DEFAULT '' COMMENT '检测物质',
  `worker_num` INT DEFAULT 0 COMMENT '作业人数',
  `test_result` VARCHAR(255) DEFAULT '' COMMENT '检测结果',
  `noise_result` FLOAT COMMENT '噪声检测结果',
  `measures` VARCHAR(500) DEFAULT '' COMMENT '补救措施',
  `conclusion` VARCHAR(255) DEFAULT '' COMMENT '评价结论',
  `high_toxicity` INT DEFAULT 0 COMMENT '是否存在高毒物品（0不存在，1存在）',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='结论';


-- t_substance_descr 检测物质备注项说明
CREATE TABLE `t_substance_descr`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `name` VARCHAR(255) DEFAULT '' COMMENT '名称',
  `instructions` VARCHAR(500) DEFAULT '' COMMENT '说明',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='检测物质备注项说明';


-- t_deliver_received 送样收样记录表
CREATE TABLE IF NOT EXISTS `t_deliver_received`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `identifier` VARCHAR(50) COMMENT '项目编号',
 `company` VARCHAR(200) COMMENT '受检单位名称',
 `office_address` VARCHAR(200) COMMENT '受检单位地址',
 `deliver_name` VARCHAR(100) COMMENT '送样人名称',
 `sample_name` VARCHAR(100) COMMENT '样品名称',
 `received` VARCHAR(100) COMMENT '总收样人',
 `gather_date` DATE COMMENT '采样日期',
 `deliver_date` DATE COMMENT '送样日期',
 `received_date` DATE COMMENT '收样日期',
 `place` VARCHAR(200) COMMENT '采样地点',
 `status` INT COMMENT '是否收样（0否，1是）',
 `substance` VARCHAR(200) COMMENT '检测物质',
 `collector` VARCHAR(50) COMMENT '收集器',
 `preserve_traffic` VARCHAR(50) COMMENT '样品状态',
 `sample_code` VARCHAR(50) COMMENT '样品编号',
 `sample_num` INT COMMENT '样品数量',
 `sample_blank` INT COMMENT '空白样(0否、1是)',
 `temperature_pressure` VARCHAR(500) COMMENT '大气压、温度',
 `sample_volume` VARCHAR(50) COMMENT '采样体积',
 `preserve_require` VARCHAR(50) COMMENT '保存方式及期限',
 `received_name` VARCHAR(100) COMMENT '接收人',
 `receive_date` DATE COMMENT '接收日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='送样收样记录表';


-- t_result_chemistry 化学因素检测处理记录表
CREATE TABLE `t_result_chemistry`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `project_id` BIGINT NOT NULL COMMENT '项目ID',
  `gather_plan_id` BIGINT NOT NULL COMMENT '对应采样计划ID',
  `substance_id` BIGINT NOT NULL COMMENT '检测物质数据表ID',
  `substance` VARCHAR(200) COMMENT '检测物质名称', 
  `min_limit` INT DEFAULT 0 COMMENT '是否小于最低检出值（0 否，1是）',
  `c_m` VARCHAR(200) COMMENT '检测浓度CM结果',
  `c_twa` VARCHAR(200) COMMENT '检测浓度CTWA结果',
  `c_stel` VARCHAR(200) COMMENT '检测浓度CSTE结果',
  `c` VARCHAR(200) COMMENT '短时间接触浓度(C)',
  `mac` VARCHAR(200) COMMENT '最高容许浓度(mg/ m³)',
  `pc_twa` VARCHAR(200) COMMENT '时间加权平均容许浓度(mg/ m³)',
  `r_f` VARCHAR(200) COMMENT '折减因子',
  `pc_stel` VARCHAR(200) COMMENT '短时间接触容许浓度(mg/ m³)',
  `pc_twa_f` VARCHAR(200) COMMENT '折算接触限制',
  `p_e` VARCHAR(200) COMMENT '峰接触浓度(PE)',
  `max_limit` FLOAT COMMENT '最大超限倍数',
  `evaluate` VARCHAR(200) COMMENT '评价(符合，不符合)',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=`InnoDB` DEFAULT CHARACTER SET utf8mb4 COMMENT='化学因素检测处理记录表';



-- t_test_basis	采样依据数据
CREATE TABLE IF NOT EXISTS `t_test_basis` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
  `test_basis_name` varchar(500)  DEFAULT NULL COMMENT '采样依据名称',
  `descriptions` VARCHAR(200) DEFAULT NULL COMMENT '采样依据实施说明',
  `defvalue` TINYINT DEFAULT 0 COMMENT '默认选择(0:无,1选中)',
  `location` INT COMMENT '采样依据位置',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采样依据数据';

-- t_test_basis_project	采样依据对应项目关系
CREATE TABLE IF NOT EXISTS `t_test_basis_project`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `test_basis_id` BIGINT NOT NULL COMMENT '法律法规依据表ID',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='采样依据对应项目关系';


-- t_question_advice	存在的问题及建议
CREATE TABLE IF NOT EXISTS `t_question_advice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
  `open_question` VARCHAR(1024) COMMENT '存在的问题',
  `advice` VARCHAR(1024) COMMENT '建议',
  `defvalue` TINYINT DEFAULT 0 COMMENT '默认选择(0:无,1选中)',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存在的问题及建议';

-- t_question_advice_project	存在的问题及建议对应项目关系
CREATE TABLE IF NOT EXISTS `t_question_advice_project`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `question_advice_id` BIGINT NOT NULL COMMENT '存在的问题及建议ID',
 `open_question` VARCHAR(1024) COMMENT '存在的问题',
 `advice` VARCHAR(1024) COMMENT '建议',
 `location` INT DEFAULT 0 COMMENT '是否自定义（0否，1是）',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='存在的问题及建议对应项目关系';

-- t_equipment_record	仪器使用记录
CREATE TABLE IF NOT EXISTS `t_equipment_record` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
  `project_id` BIGINT COMMENT '项目ID',
  `technique` VARCHAR(32)  COMMENT '排单方式',
  `date` VARCHAR(10) COMMENT '使用日期',
  `begin_time` VARCHAR(5) COMMENT '开始时间',
  `end_time` VARCHAR(5) COMMENT '结束时间',
  `minutes` BIGINT COMMENT '使用时长（min）',
  `record_id` BIGINT COMMENT '采样记录ID',
  `gather_plan_id` BIGINT COMMENT '采样计划ID',
  `equipment_id` BIGINT COMMENT '仪器ID',
  `serial_number` VARCHAR(100) COMMENT '仪器编号',
  `name` VARCHAR(255) COMMENT '仪器名称',
  `model` VARCHAR(100) COMMENT '仪器型号',
  `time_frame` VARCHAR(128) COMMENT '采样时段',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='仪器使用记录';


-- protection_evaluation_basics	个人防护用品及有效性评价基础库
CREATE TABLE IF NOT EXISTS `protection_evaluation_basics` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
  `protection_module` VARCHAR(1024) COMMENT '防护用品及型号',
  `parameter` VARCHAR(255) COMMENT '参数',
  `protective_harm` VARCHAR(255) COMMENT '防护危害',
  `evaluation` VARCHAR(1024) COMMENT '有效性评价',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人防护用品及有效性评价基础库';


-- t_signature	签名表
CREATE TABLE IF NOT EXISTS `t_signature`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `path` VARCHAR(1024) COMMENT '电子签名图路径',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='签名表';


-- t_task_flow 任务流程表
CREATE TABLE IF NOT EXISTS `t_task_flow`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `plan_id` BIGINT NOT NULL COMMENT '任务排单ID',
 `plan_status` INT  NOT NULL COMMENT '任务状态(1下发，2排单，3调查采样，10送样，20检测报告，30检测报告发送，40报告装订，50任务完成，98挂起，99终止)',
 `modifier` VARCHAR(255) COMMENT '修改人',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='任务流程表';

-- t_warning_signs 职业危害警示标识设置一览表
CREATE TABLE IF NOT EXISTS `t_warning_signs`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(12) NOT NULL COMMENT '项目ID',
  `hazard_id` bigint(12) NOT NULL COMMENT '关联的接害情况ID',
  `place` varchar(255) NOT NULL COMMENT '主要岗位或地点',
  `hazard_factors` varchar(500) COMMENT '职业病危害因素',
  `warning` varchar(255) COMMENT '警示标识',
  `instruct` varchar(255) COMMENT '指令标识',
  `card` varchar(255) COMMENT '告知卡',
  `createtime` datetime(0) DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` datetime(0) DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='职业危害警示标识设置一览表';

-- t_warning_basic 职业危害警示标识基础信息表
CREATE TABLE IF NOT EXISTS `t_warning_basic`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `warning_labels` varchar(1024) COMMENT '警示标识',
  `instruct_logo` varchar(1024) COMMENT '指令标识',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='职业危害警示标识基础信息表';


-- t_order_source 项目隶属来源表
CREATE TABLE IF NOT EXISTS `t_order_source`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_source` varchar(255) COMMENT '项目隶属来源',
  `type` INT COMMENT '类型（3项目隶属，4业务来源）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目隶属来源表';

-- t_appraise_evaluation_unit 评价单元表
CREATE TABLE IF NOT EXISTS `t_appraise_evaluation_unit`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(12) COMMENT '父级ID',
  `project_id` bigint(12) NOT NULL COMMENT '项目ID',
  `unit` varchar(255) COMMENT '主单元',
  `subunit` varchar(255) COMMENT '子单元',
  `content` varchar(500) COMMENT '内容',
  `remarks` varchar(255) COMMENT '备注',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价单元表';

-- t_industry 行业分类表
CREATE TABLE IF NOT EXISTS `t_industry`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` bigint(12) DEFAULT 0 NOT NULL COMMENT '父级ID',
  `map_id` bigint(12) DEFAULT 0 NOT NULL COMMENT '关系ID',
  `letter` varchar(255) COMMENT '字母',
  `code` varchar(255) COMMENT '编号',
  `name` varchar(500) COMMENT '名称',
  `remark` varchar(500) COMMENT '说明',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='行业分类表';

-- t_statute 评价法律法规表
CREATE TABLE IF NOT EXISTS `t_statute`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(500) COMMENT '名称',
  `issue_date` varchar(500) COMMENT '颁发日期',
  `impl_date` DATE COMMENT '实施日期',
  `expiration_date` DATE COMMENT '失效日期',
  `file_url` varchar(500) COMMENT '文件路径',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价法律法规表';

-- t_basis 评价法律依据表
CREATE TABLE IF NOT EXISTS `t_basis`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(500) COMMENT '名称',
  `code` varchar(255) COMMENT '编号',
  `impl_date` DATE COMMENT '实施日期',
  `expiration_date` DATE COMMENT '失效日期',
  `file_url` varchar(500) COMMENT '文件路径',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价法律依据表';

-- t_industry_statute_basis 行业分类与法律法规依据对应关系表
CREATE TABLE IF NOT EXISTS `t_industry_statute_basis`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `map_id` bigint(12) NOT NULL COMMENT '关系ID',
  `statute_basis_id` bigint(12) NOT NULL COMMENT '法律法规依据ID',
  `type` INT NOT NULL COMMENT '关联类型（1法律法规，2法规依据）',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='行业分类与法律法规依据对应关系表';


-- t_building_structures	建构筑物基本情况
CREATE TABLE IF NOT EXISTS `t_building_structures`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `building_name` VARCHAR(255) COMMENT '建筑物名称',
 `architecture` VARCHAR(255) COMMENT '建筑物结构',
 `total_height` VARCHAR(255) COMMENT '总高',
 `floor_area` VARCHAR(255) COMMENT '占地面积',
 `covered_area` VARCHAR(255) COMMENT '建筑面积',
 `orientation` VARCHAR(255) COMMENT '朝向',
 `floor` VARCHAR(255) COMMENT '楼层',
 `floor_height` VARCHAR(255) COMMENT '层高',
 `layout_use` VARCHAR(255) COMMENT '生产布局及用途',
 `set_position` VARCHAR(255) COMMENT '设置位置',
 `air_distribution` VARCHAR(255) COMMENT '气流组织形式',
 `ventilation` VARCHAR(255) COMMENT '通风空调设施',
 `design_air_volume` VARCHAR(255) COMMENT '设计风量',
 `quantity` VARCHAR(255) COMMENT '数量',
 `coverage` VARCHAR(255) COMMENT '服务区域',
 `cleanliness` VARCHAR(255) COMMENT '洁净等级',
 `workshop_volume` VARCHAR(255) COMMENT '车间容积',
 `ventilation_rate` VARCHAR(255) COMMENT '换气次数',
 `homework_num` VARCHAR(255) COMMENT '作业人数',
 `air_volume` VARCHAR(255) COMMENT '人均新风量',
 `tuyere_location` VARCHAR(255) COMMENT '新风口位置',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='建构筑物基本情况';


-- t_control_funding 防治经费
CREATE TABLE IF NOT EXISTS `t_control_funding`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(12) NOT NULL COMMENT '项目ID',
  `spending` VARCHAR(255) COMMENT '开支项目',
  `investment_budget` DECIMAL(14,2) DEFAULT 0 COMMENT '预算投资金额',
  `remake` VARCHAR(500) COMMENT '备注',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='防治经费';

-- t_auxiliary_room_toilet 评价辅助用室-卫生间
CREATE TABLE IF NOT EXISTS `t_auxiliary_room_toilet`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(12) NOT NULL COMMENT '项目ID',
  `location` VARCHAR(255) COMMENT '建筑物/位置',
  `male_toilet` VARCHAR(100) COMMENT '男卫生间数量',
  `female_toilet` VARCHAR(100) COMMENT '女卫生间数量',
  `squatting_man` VARCHAR(100) COMMENT '男卫生间蹲位数量',
  `squatting_woman` VARCHAR(100) COMMENT '女卫生间蹲位数量',
  `urinary_man` VARCHAR(100) COMMENT '男卫生间小便池数量',
  `washing_sink_man` VARCHAR(100) COMMENT '男卫生间洗手池数量',
  `washing_sink_woman` VARCHAR(100) COMMENT '女卫生间洗手池数量',
  `washing_tank_man` VARCHAR(100) COMMENT '男卫生间洗污池数量',
  `washing_tank_woman` VARCHAR(100) COMMENT '女卫生间洗污池数量',
  `shower_man` VARCHAR(100) COMMENT '男卫生间淋浴喷头数量',
  `shower_woman` VARCHAR(100) COMMENT '女卫生间淋浴喷头数量',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价辅助用室-卫生间';


-- t_auxiliary_room 评价辅助用室表
CREATE TABLE IF NOT EXISTS `t_auxiliary_room`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(12) NOT NULL COMMENT '项目ID',
  `type` VARCHAR(100) COMMENT '设施分类',
  `location` VARCHAR(255) COMMENT '建筑物/位置',
  `sex` INT DEFAULT 0 COMMENT '性别(0无,1男，2女)',
  `room` VARCHAR(100) COMMENT '辅助用室',
  `room_num` VARCHAR(100) COMMENT '辅助用室数量',
  `facility` VARCHAR(100) COMMENT '设施',
  `facility_num` VARCHAR(100) COMMENT '设施数量',
  `pattern` VARCHAR(100) COMMENT '方式',
  `pattern_describe` VARCHAR(100) COMMENT '方式描述',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价辅助用室表';

-- t_section 章节表
CREATE TABLE IF NOT EXISTS `t_section`  (
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `project_id` BIGINT NOT NULL COMMENT '项目ID',
 `chapter_id` BIGINT COMMENT '章节模板ID',
 `pid` BIGINT COMMENT '父级ID',
 `section_name` VARCHAR(500) COMMENT '章节名称',
 `api_name` varchar(500) COMMENT '固定接口名称',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='章节表';

-- t_tabular_information 表格信息表
CREATE TABLE IF NOT EXISTS `t_tabular_information`  (
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `further_id` BIGINT NOT NULL COMMENT '信息补充ID',
 `linenum` INT COMMENT '行号',
 `price` VARCHAR(500) COMMENT '值',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='表格信息表';


-- t_further_information 信息补充表
CREATE TABLE IF NOT EXISTS `t_further_information`  (
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `section_id` BIGINT COMMENT '章节ID',
 `generic_name` VARCHAR(255) COMMENT '通用名称',
 `title` VARCHAR(255) COMMENT '标题',
 `content` TEXT COMMENT '内容',
 `path` VARCHAR(1024) COMMENT '图片路径',
 `remark` VARCHAR(255) COMMENT '备注',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='信息补充表';


-- t_area 区域划分
CREATE TABLE IF NOT EXISTS `t_area`	 (
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `pid` BIGINT DEFAULT 0 COMMENT '父级ID',
 `code` VARCHAR(255) COMMENT '邮编',
 `area` VARCHAR(255) COMMENT '区域',
 `template_id` BIGINT COMMENT '地理环境模板id',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='区域划分';

-- t_area_template 区域划分信息模板
CREATE TABLE IF NOT EXISTS `t_area_template` (
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `area_id` BIGINT COMMENT '区域划分ID',
 `location` TEXT COMMENT '地理位置',
 `environment` TEXT COMMENT '地理环境',
 `hydrology` TEXT COMMENT '水文',
 `climate` TEXT COMMENT '气候',
 `population` TEXT COMMENT '人口',
 `economic` TEXT COMMENT '经济',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='区域划分信息模板';


-- t_hazard_assessment 危害评价表
CREATE TABLE IF NOT EXISTS `t_hazard_assessment`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(12) NOT NULL COMMENT '项目ID',
  `code` varchar(255) COMMENT '编号',
  `content` VARCHAR(500) COMMENT '内容',
   `summary` VARCHAR(500) COMMENT '总结',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='危害评价表';

-- t_hazard_assessment_project 危害评价项目表
CREATE TABLE IF NOT EXISTS `t_hazard_assessment_project`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `project_id` bigint(12) NOT NULL COMMENT '项目ID',
  `code` varchar(255) COMMENT '编号',
  `type` varchar(255) COMMENT '类型',
  `content` VARCHAR(500) COMMENT '内容',
   `basis` VARCHAR(500) COMMENT '依据',
   `result` VARCHAR(500) COMMENT '结果',
   `evaluate` VARCHAR(500) COMMENT '评价',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='危害评价项目表';

-- t_laboratory_testing_instruments 实验室检测仪器表
CREATE TABLE IF NOT EXISTS `t_laboratory_testing_instruments`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `model` varchar(255) COMMENT '仪器型号',
  `name` VARCHAR(500) COMMENT '仪器名称',
  `code` varchar(255) COMMENT '内部编号',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='实验室检测仪器表';


-- -------- 样品采集记录信息 ----------


-- t_chapter_template 章节模板表
CREATE TABLE IF NOT EXISTS `t_chapter_template`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` BIGINT DEFAULT 0 COMMENT '父级ID',
  `name` VARCHAR(500) COMMENT '章节名',
  `type` varchar(500) COMMENT '项目类型',
  `api_name` varchar(500) COMMENT '固定接口名称',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='章节模板表';

-- t_table_template表格模板表
CREATE TABLE IF NOT EXISTS `t_table_template`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `information_template_id` BIGINT COMMENT '信息模板ID',
   `line_number` INT COMMENT '行号',
  `value` varchar(500) COMMENT '值',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='表格模板表';

-- t_information_template 信息模板表
CREATE TABLE IF NOT EXISTS `t_information_template`	 (
 `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `chapter_template_id` BIGINT COMMENT '章节模板ID',
 `title` VARCHAR(255) COMMENT '标题',
 `content` TEXT COMMENT '内容',
 `currency` VARCHAR(255) COMMENT '通用名称',
 `path` VARCHAR(500) COMMENT '图路径',
 `remarks` VARCHAR(255) COMMENT '备注',
 PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='信息模板表';

-- t_company_appraise_survey 评价企业概况表
CREATE TABLE IF NOT EXISTS `t_company_appraise_survey`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `company` varchar(100) DEFAULT '' COMMENT '用人单位名称',
  `identifier` varchar(100) DEFAULT '' COMMENT '项目编号',
  `project_name` varchar(100) DEFAULT '' COMMENT '项目名称',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `office_address` varchar(200) DEFAULT '' COMMENT '单位地址',
  `legal_body` varchar(100) DEFAULT '' COMMENT '法定代表人',
  `contact` varchar(100) DEFAULT '' COMMENT '项目负责人',
  `telephone` varchar(50) DEFAULT '' COMMENT '联系方式',
  `industry_category` varchar(200) DEFAULT '' COMMENT '行业类别',
  `risk_level` tinyint COMMENT '危害风险分类',
  `three_simul` varchar(255) DEFAULT '' COMMENT '“三同时”执行情况',
  `prod_scale` varchar(255) DEFAULT '' COMMENT '近三年生产规模',
  `labor_quota` int COMMENT '劳动定员',
  `inspection_org` varchar(255) DEFAULT '' COMMENT '职业健康检查机构名称',
  `last_test_time` date COMMENT '最近一次职业健康检查时间',
  `around_survey` varchar(255) DEFAULT '' COMMENT '企业周边情况（需明确东、西、南、北四侧的情况）',
  `entrust_ins_org` varchar(100) DEFAULT '' COMMENT '委托单位内部职业卫生管理部门名称',
  `population` int COMMENT '职业卫生管理人员人数',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_project_id` (`project_id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价企业概况表';

-- t_company_appraise_note 评价企业概况关联信息表
CREATE TABLE IF NOT EXISTS `t_company_appraise_note`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `company` varchar(100) DEFAULT '' COMMENT '单位名称',
  `company_arr_sur_id` bigint NOT NULL COMMENT '单位ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `code` varchar(100) DEFAULT NULL COMMENT '字典码(详见sys_dict)',
  `key` varchar(100) NOT NULL DEFAULT '' COMMENT '字段标识KEY(详见sys_dict)',
  `value` varchar(255) DEFAULT '' COMMENT '详细记录',
  `label` varchar(100) DEFAULT '' COMMENT '前端展示文字',
  `index` INT COMMENT '前端排序依据',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价企业概况关联信息表';

-- t_notice新闻公告表
CREATE TABLE IF NOT EXISTS `t_notice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
  `title` VARCHAR(255) COMMENT '标题',
  `content` TEXT COMMENT '内容',
  `author` VARCHAR(255) COMMENT '作者',
  `number` INT DEFAULT 0 COMMENT  '访问次数',
  `like_numbers` INT DEFAULT 0 COMMENT  '点赞次数',
  `like_state` INT DEFAULT 1 COMMENT  '是否点赞（默认未点赞1;点赞2；）',
  `state` INT DEFAULT 0 COMMENT '默认选择(0:展示,1不展示)',
  `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻公告表';


-- t_timeout_project 超时项目信息表
CREATE TABLE IF NOT EXISTS `t_timeout_project`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `company` varchar(100) DEFAULT '' COMMENT '受检单位名称',
  `identifier` varchar(255) DEFAULT '' COMMENT '项目编号',
  `project_name` varchar(100) DEFAULT '' COMMENT '项目名称名称',
  `salesman` varchar(100) DEFAULT '' COMMENT '业务员',
  `principal` varchar(100) DEFAULT '' COMMENT '负责人',
  `type` varchar(100) DEFAULT '' COMMENT '项目类别',
  `status` int COMMENT '项目状态',
  `status_time` date COMMENT '项目状态更改时间',
  `exceeding_days` bigint COMMENT '项目超出天数',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='超时项目信息表';



-- t_file_sharing 文件共享表
CREATE TABLE IF NOT EXISTS `t_file_sharing`  (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `title` VARCHAR(255) COMMENT '标题',
  `instructions` VARCHAR(500) COMMENT '说明',
  `path` VARCHAR(255) COMMENT '文件路径',
  `status` INT DEFAULT 1 COMMENT '状态（0未发布，1发布）',
  `department_id` BIGINT COMMENT '可见部门ID',
  `publisher_id` BIGINT COMMENT '发布人ID',
  `publisher` VARCHAR(100) DEFAULT '' COMMENT '发布人',
  `release_date` DATE COMMENT '发布日期',
  `createtime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
  `updatetime` datetime ON UPDATE CURRENT_TIMESTAMP COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='文件共享表';


-- t_accounts_receivable 项目应收账款逾期信息表
CREATE TABLE IF NOT EXISTS `t_accounts_receivable`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `project_id` bigint NOT NULL COMMENT '项目ID',
  `project_name` varchar(100) DEFAULT '' COMMENT '项目名称名称',
  `salesman` varchar(100) DEFAULT '' COMMENT '业务员',
  `type` varchar(100) DEFAULT '' COMMENT '项目类别',
  `total_money` DECIMAL(10,2) DEFAULT 0 COMMENT '项目金额(元)',
  `receipt_money` DECIMAL(10,2) DEFAULT 0 COMMENT '项目已收款金额(元)',
  `nosettlement_money` DECIMAL(10,2) DEFAULT 0 COMMENT '项目未结算金额(元)',
  `status` int COMMENT '项目状态',
  `rpt_sent_date` date COMMENT '报告发送日期',
  `overdue_days` bigint COMMENT '逾期天数',
  PRIMARY KEY (`id`)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='项目应收账款逾期信息表';

-- t_comment 新闻评论表
CREATE TABLE IF NOT EXISTS `t_comment` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
    `notice_id` bigint NOT NULL COMMENT '新闻ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `name` VARCHAR(255) COMMENT '评论人',
    `content` VARCHAR(500) COMMENT '评论内容',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻评论表';

-- t_notice_like_user 用户点赞表
CREATE TABLE IF NOT EXISTS `t_notice_like_user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
    `notice_id` bigint NOT NULL COMMENT '新闻ID',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `name` VARCHAR(255) COMMENT '点赞人',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞表';

-- t_person_basic_files 技术人员基本档案表
CREATE TABLE IF NOT EXISTS `t_person_basic_files` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
    `user_id` bigint NOT NULL COMMENT '人员ID',
    `user_name` VARCHAR(255) COMMENT '人员名称',
    `name` VARCHAR(255) COMMENT '档案名称',
    `path` VARCHAR(255) COMMENT '档案路径',
    `number_copies` int  DEFAULT 1 COMMENT '份数',
    `number_pages` int  DEFAULT 1 COMMENT '页数',
    `remarks` VARCHAR(255) COMMENT '备注',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '文件收录时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='技术人员基本档案表';

-- t_person_technical_certificate 人员技能证书
CREATE TABLE IF NOT EXISTS `t_person_technical_certificate` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
    `user_id` bigint NOT NULL COMMENT '人员ID',
    `user_name` VARCHAR(255) COMMENT '人员名称',
    `name` VARCHAR(255) COMMENT '技能名称',
    `path` VARCHAR(255) COMMENT '技能路径',
    `number_copies`  INT DEFAULT 1 COMMENT '份数',
    `number_pages`  INT DEFAULT 1 COMMENT '页数',
    `remarks` VARCHAR(255) COMMENT '备注',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '文件收录时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员技能证书表';

-- t_person_train 人员培训
CREATE TABLE IF NOT EXISTS `t_person_train` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
    `user_id` bigint NOT NULL COMMENT '人员ID',
    `user_name` VARCHAR(255) COMMENT '人员名称',
    `name` VARCHAR(255) COMMENT '培训履历名称',
    `path` VARCHAR(255) COMMENT '培训履历路径',
    `number_copies`  INT DEFAULT 1 COMMENT '份数',
    `number_pages`  INT DEFAULT 1 COMMENT '页数',
    `remarks` VARCHAR(255) COMMENT '备注',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '文件收录时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员培训表';

-- t_person_honor_crtificate  人员荣誉证书
CREATE TABLE IF NOT EXISTS `t_person_honor_crtificate` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
    `user_id` bigint NOT NULL COMMENT '人员ID',
    `user_name` VARCHAR(255) COMMENT '人员名称',
    `name` VARCHAR(255) COMMENT '荣誉证书名称',
    `path` VARCHAR(255) COMMENT '荣誉证书路径',
    `number_copies`  INT DEFAULT 1 COMMENT '份数',
    `number_pages`  INT DEFAULT 1 COMMENT '页数',
    `remarks` VARCHAR(255) COMMENT '备注',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '文件收录时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员荣誉证书表';

-- t_person_supervision_records 人员质量监督记录
CREATE TABLE IF NOT EXISTS `t_person_supervision_records` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id（主键）',
    `user_id` bigint NOT NULL COMMENT '人员ID',
    `user_name` VARCHAR(255) COMMENT '人员名称',
    `name` VARCHAR(255) COMMENT '质量监督记录名称',
    `path` VARCHAR(255) COMMENT '质量监督记录路径',
    `number_copies`  INT DEFAULT 1 COMMENT '份数',
    `number_pages`  INT DEFAULT 1 COMMENT '页数',
    `remarks` VARCHAR(255) COMMENT '备注',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '文件收录时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员质量监督记录表';

-- t_calibration_instrument 校准仪器表
CREATE TABLE IF NOT EXISTS `t_calibration_instrument`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `subjection` VARCHAR(255) COMMENT '隶属公司',
    `dept_id` BIGINT COMMENT '部门ID',
    `user_name` varchar(255) COMMENT '隶属人员',
    `detection_type` VARCHAR(200) COMMENT '检测类型(airFixed/noiseFixed)',
    `name` VARCHAR(500) COMMENT '仪器名称',
    `model` varchar(255) COMMENT '仪器型号',
    `code` varchar(255) COMMENT '内部编号',
    `max_flow` DECIMAL(10,2) COMMENT '最大流量',
    `min_flow` DECIMAL(10,2) COMMENT '最小流量',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='校准仪器表';


-- t_gather_bar_code 采样计划条形码表
CREATE TABLE IF NOT EXISTS `t_gather_bar_code`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `gather_plan_id` BIGINT NOT NULL COMMENT '对应采样计划ID',
    `sample_code` VARCHAR(100) COMMENT '样品编号',
    `bar_code` varchar(255) COMMENT '条形码编号',
    `number` INT DEFAULT 1 COMMENT '扫描次数',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='采样计划条形码表';
    
    
-- t_material_alias 物质别名表
CREATE TABLE IF NOT EXISTS `t_material_alias`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `material` VARCHAR(255) COMMENT '物质名称',
    `material_alias` varchar(255) COMMENT '物质简称',
    `used_company` varchar(100) COMMENT '所用公司',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='物质别名表';
    
    
-- lab_material_inspector 检测人员与物质对应关系表
CREATE TABLE IF NOT EXISTS `t_material_inspector`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `substance_id` bigint(12) COMMENT '物质ID',
    `user_id` bigint(12) COMMENT '检测人员id',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='检测人员与物质对应关系表';
    
-- lab_material_file 物质与文件名称及编号对应关系表
CREATE TABLE IF NOT EXISTS `t_material_file`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `substance_id` bigint(12) COMMENT '物质ID',
    `file_number` VARCHAR(100) COMMENT '文件编号',
    `file_name` VARCHAR(100) COMMENT '文件名称',
    `original_record_type` VARCHAR(100) COMMENT '原始记录类型',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='物质与文件名称及编号对应关系表'; 
    
    
    -- 邮箱验证码
CREATE TABLE `sys_email_verify` (
  `uuid` char(36) NOT NULL COMMENT 'uuid',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  PRIMARY KEY (`uuid`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='邮箱验证码';



-- al_template_classification 评价报告模板分类表
CREATE TABLE IF NOT EXISTS `al_template_classification`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `template_name` VARCHAR(100) COMMENT '模板名称',
    `project_type` VARCHAR(100) COMMENT '项目类型',
    `industry_classification` VARCHAR(100) COMMENT '行业分类',
    `province` VARCHAR(100) DEFAULT '浙江省' COMMENT '省',
    `city` VARCHAR(100) COMMENT '市',
    `area` VARCHAR(100) COMMENT '区',
    `year` VARCHAR(100) COMMENT '年份',
    `company` VARCHAR(100) COMMENT '受检单位',
    `project_name` VARCHAR(100) COMMENT '项目名称',
    `main_products` VARCHAR(255) COMMENT '主要产品',
    `file_path` VARCHAR(100) COMMENT '文件路径',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价报告模板分类表';


-- al_report_template_project 评价项目报告章节模板对应关系表
CREATE TABLE IF NOT EXISTS `al_report_template_project`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `template_classification_id` bigint(12) COMMENT '评价报告模板分类ID',
    `plan_id` bigint(12) COMMENT '任务排单ID',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='评价项目报告章节模板对应关系表';

    
-- t_chapter_basics_template 章节基础模板表
CREATE TABLE IF NOT EXISTS `t_chapter_basics_template`  (
  `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `pid` BIGINT DEFAULT 0 COMMENT '父级ID',
  `name` VARCHAR(500) COMMENT '章节名',
  `type` varchar(500) COMMENT '项目类型',
  `report_type` varchar(500) COMMENT '报告类型',
  `sort` varchar(500) COMMENT '排序字段',
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='章节基础模板表';


-- t_qr_code 二维码信息表
CREATE TABLE IF NOT EXISTS `t_qr_code`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `uuid` char(36) NOT NULL COMMENT 'uuid', 
    `project_name` VARCHAR(255) COMMENT '项目名称',
    `file_name` varchar(255) COMMENT '文件名称',
    `img_path` varchar(255) COMMENT '二维码图片路径',
    `file_path` varchar(255) COMMENT '文件路径',
    `convert_path` varchar(255) COMMENT '转换后文件路径',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='二维码信息表';


    
    