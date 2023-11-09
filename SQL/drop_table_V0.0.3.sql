-- 删除表
use anlian;

-- t_company	企业信息表
DROP TABLE IF EXISTS t_company;

-- t_contract	合同信息表
DROP TABLE IF EXISTS t_contract;

-- t_project	项目信息表
DROP TABLE IF EXISTS t_project;

-- t_contract_template	合同模板共同信息表
DROP TABLE IF EXISTS t_contract_template;

-- t_contract_custom_template	合同模板自定义字段表
DROP TABLE IF EXISTS t_contract_custom_template;

-- t_contract_custom_data	合同模板自定义字段数据表
DROP TABLE IF EXISTS t_contract_custom_data;

-- t_department	组织架构/部门信息
DROP TABLE IF EXISTS t_department;

-- t_plan	任务排单表
DROP TABLE IF EXISTS t_plan;

-- t_plan_user	任务人员表
DROP TABLE IF EXISTS t_plan_user;

-- t_equipment	仪器设备信息记录表
DROP TABLE IF EXISTS t_equipment;

-- t_equipment_reserve	用户预定仪器表
DROP TABLE IF EXISTS t_equipment_reserve;

-- t_equipment_reserve_rel	用户预定仪器对应关系表
DROP TABLE IF EXISTS t_equipment_reserve_rel;

-- t_staff	员工信息
DROP TABLE IF EXISTS t_staff;

-- t_company_survey	用人单位概况调查表
DROP TABLE IF EXISTS t_company_survey;

-- t_craft_process	工艺流程调查表
DROP TABLE IF EXISTS t_craft_process;

-- t_equipment	生产设备调查表(现场记录中不展示但是最后报告中需要)
DROP TABLE IF EXISTS t_machine;

-- t_material	原辅材料、中间产品、产品情况调查表
DROP TABLE IF EXISTS t_material;

-- t_equipment_measure	设备布局测点布置图调查表
DROP TABLE IF EXISTS t_equipment_measure;

-- t_equipment_layout	设备布局信息表
DROP TABLE IF EXISTS t_equipment_layout;

-- t_measure_layout	检测点布置信息表
DROP TABLE IF EXISTS t_measure_layout;

-- t_labor_operation	劳动者作业情况调查表
DROP TABLE IF EXISTS t_labor_operation;

-- t_labor_hazard 劳动者作业接害情况
DROP TABLE IF EXISTS `t_labor_hazard`;

-- t_labor_hazard_item 劳动者作业接害检测项目信息
DROP TABLE IF EXISTS `t_labor_hazard_item`;

-- t_measures_supplies	职业病防护措施及个人防护用品调查表
DROP TABLE IF EXISTS t_measures_supplies;

-- t_rescue_warning	应急救援和警示标识等设置情况调查表
DROP TABLE IF EXISTS t_rescue_warning;

-- t_auxiliary_living	建筑卫生学辅助用室及生活用室设置情况调查表
DROP TABLE IF EXISTS t_auxiliary_living;

-- t_realistic_work	工作场所劳动者工作日写实调查记录表
DROP TABLE IF EXISTS t_realistic_work;

-- t_realistic_record	工作日写实信息记录表
DROP TABLE IF EXISTS t_realistic_record;

-- t_gather_plan	现场采样和检测计划信息表
DROP TABLE IF EXISTS t_gather_plan;

-- t_gather_point	采样和检测布点图
DROP TABLE IF EXISTS t_gather_point;

-- t_weather	检测时气象条件表
DROP TABLE IF EXISTS t_weather;

-- t_noise_fixed	噪声定点测量信息表
DROP TABLE IF EXISTS t_noise_fixed;

-- t_noise_individual	噪声个体测量信息表
DROP TABLE IF EXISTS t_noise_individual;

-- t_noise_pulse	脉冲噪声强度测试信息表
DROP TABLE IF EXISTS t_noise_pulse;

-- t_noise_octave	噪声倍频程测量记录信息表
DROP TABLE IF EXISTS t_noise_octave;

-- t_temperature_stable	高温(热源稳定)测试记录表
DROP TABLE IF EXISTS t_temperature_stable;

-- t_temperature_unstable	高温(热源不稳定)测试记录表
DROP TABLE IF EXISTS t_temperature_unstable;

-- t_vibration_hand	手传振动强度测试记录表
DROP TABLE IF EXISTS t_vibration_hand;

-- t_electromagnetic	高频电磁场强度测试记录表
DROP TABLE IF EXISTS t_electromagnetic;

-- t_electric	工频电场强度测试记录表
DROP TABLE IF EXISTS t_electric;

-- t_microwave	微波辐射测试记录表
DROP TABLE IF EXISTS t_microwave;

-- t_ultraviolet	紫外辐射测试记录表
DROP TABLE IF EXISTS t_ultraviolet;

-- t_illumination	照度测试记录表
DROP TABLE IF EXISTS t_illumination;

-- t_co	一氧化碳定点测试记录表
DROP TABLE IF EXISTS t_co;

-- t_co2	二氧化碳定点测试记录表
DROP TABLE IF EXISTS t_co2;

-- t_wind_speed	排风罩风速测试记录表
DROP TABLE IF EXISTS t_wind_speed;

-- t_wind_pressure	风道内压力测试记录表
DROP TABLE IF EXISTS t_wind_pressure;

-- t_microclimate	微小气候测试记录表
DROP TABLE IF EXISTS t_microclimate;

-- t_temperature_weather	高温气象条件测试记录表
DROP TABLE IF EXISTS t_temperature_weather;

-- t_air_fixed	空气中有害物质定点采样记录表
DROP TABLE IF EXISTS t_air_fixed;

-- t_air_fixed	空气中粉尘分散度定点采样记录表
DROP TABLE IF EXISTS t_air_dust;

-- t_air_individual	空气中有害物质个体采样记录表
DROP TABLE IF EXISTS t_air_individual;

-- t_sample_img	采样影像记录表
DROP TABLE IF EXISTS t_sample_img;

-- t_substance	检测物质数据表
DROP TABLE IF EXISTS t_substance;

-- t_gist_method	检测法规依据数据表
DROP TABLE IF EXISTS t_gist_method;

-- t_project_archive	项目归档文件目录表
DROP TABLE IF EXISTS t_project_archive;

-- t_linkman	企业联系人信息
DROP TABLE IF EXISTS t_linkman;

-- t_report_review	报告技术审核记录表
DROP TABLE IF EXISTS t_report_review;

-- t_report_proofread	报告技术校核记录表
DROP TABLE IF EXISTS t_report_proofread;

-- t_report_improve 报告信息完善记录表
DROP TABLE IF EXISTS t_report_improve;

-- t_law	法律依据数据
DROP TABLE IF EXISTS t_law;

-- t_law_contract	法律法规依据对应合同关系
DROP TABLE IF EXISTS t_law_contract;

-- t_report_review_dict	报告技术审核项对应关系表
DROP TABLE IF EXISTS t_report_review_dict;

-- t_account 收付款记录表
DROP TABLE IF EXISTS t_account;

-- t_project_review 合同评审表
DROP TABLE IF EXISTS `t_project_review`;

-- t_project_review_opinion  评审意见表
DROP TABLE IF EXISTS `t_project_review_opinion`;

-- t_project_review_dict  合同评审项对应关系表
DROP TABLE IF EXISTS `t_project_review_dict`;

-- t_quotation	报价记录表
DROP TABLE IF EXISTS `t_quotation`;

-- t_quotation_item	报价项目信息表
DROP TABLE IF EXISTS `t_quotation_item`;

-- t_artisan	技术人员信息表
DROP TABLE IF EXISTS `t_artisan`;

-- t_artisan_task	技术人员排单记录表
DROP TABLE IF EXISTS `t_artisan_task`;

-- t_sales_target	业务员销售目标表
DROP TABLE IF EXISTS `t_sales_target`;

-- t_company_contact	公司联系人表
DROP TABLE IF EXISTS `t_company_contact`;

-- t_project_procedures	项目流程表 
DROP TABLE IF EXISTS `t_project_procedures`;

-- t_division_target	事业部业绩目标表
DROP TABLE IF EXISTS `t_division_target`;

-- t_commission	提成记录表
DROP TABLE IF EXISTS `t_commission`;

-- t_category 类型信息记录表
DROP TABLE IF EXISTS `t_category`;

-- t_detection 检测情况表
DROP TABLE IF EXISTS `t_detection`;

-- t_sample_basis_equip 检测依据及设备表
DROP TABLE IF EXISTS `t_sample_basis_equip`;

-- t_protection_evaluation 个人防护用品有效性评价
DROP TABLE IF EXISTS `t_protection_evaluation`;

-- t_conclusion 结论
DROP TABLE IF EXISTS `t_conclusion`;

-- t_substance_descr 检测物质备注项说明
DROP TABLE IF EXISTS `t_substance_descr`;

-- t_task_flow 任务流程表
DROP TABLE IF EXISTS `t_task_flow`;

-- t_warning_signs 职业危害警示标识设置一览表
DROP TABLE IF EXISTS `t_warning_signs`;

-- t_evaluation_unit 评价单元表
DROP TABLE IF EXISTS `t_evaluation_unit`;

-- t_industry 行业分类表
DROP TABLE IF EXISTS `t_industry`;

-- t_statute 评价法律法规表
DROP TABLE IF EXISTS `t_statute`;

-- t_basis 评价法律依据表
DROP TABLE IF EXISTS `t_basis`;

-- t_industry_statute_basis 行业分类与法律法规依据对应关系表
DROP TABLE IF EXISTS `t_industry_statute_basis`;

-- t_building_structures	建构筑物基本情况
DROP TABLE IF EXISTS `t_building_structures`;

-- t_control_funding 防治经费
DROP TABLE IF EXISTS `t_control_funding`;

-- t_auxiliary_room_toilet 评价辅助用室-卫生间
DROP TABLE IF EXISTS `t_auxiliary_room_toilet`;

-- t_auxiliary_room 评价辅助用室表
DROP TABLE IF EXISTS `t_auxiliary_room`;

-- t_further_information 信息补充表
DROP TABLE IF EXISTS `t_further_information`;

-- t_information_template 信息模板表
DROP TABLE IF EXISTS `t_information_template`;

-- t_area 区域划分
DROP TABLE IF EXISTS `t_area`;

-- t_area_template 区域划分信息模板
DROP TABLE IF EXISTS `t_area_template`;

-- t_company_appraise_survey 评价企业概况表
DROP TABLE IF EXISTS `t_company_appraise_survey`;

-- t_company_appraise_note 评价企业概况关联信息表
DROP TABLE IF EXISTS `t_company_appraise_note`;

-- t_file_sharing 文件共享表
DROP TABLE IF EXISTS `t_file_sharing`;

-- t_accounts_receivable 项目应收账款逾期信息表
DROP TABLE IF EXISTS `t_accounts_receivable`;