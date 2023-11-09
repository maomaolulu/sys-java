-- lab_calculation_method	计算方法表
DROP TABLE IF EXISTS lab_calculation_method;

-- lab_curve_plotting	标准曲线绘制表（实验）
DROP TABLE IF EXISTS lab_curve_plotting;

-- lab_uv_spectrophotometry_one	紫外可见分光光度法原始记录（1）
DROP TABLE IF EXISTS lab_uv_spectrophotometry_one_head;

-- lab_gravimetric_method 重量法原始记录
DROP TABLE IF EXISTS lab_gravimetric_method_head;

-- lab_dust_dispersion 粉尘分散度原始记录
DROP TABLE IF EXISTS lab_dust_dispersion_head;

-- lab_atomic_fluorescence 原子荧光法原始记录
DROP TABLE IF EXISTS lab_atomic_fluorescence_head;

-- lab_ion_selective_electrode 离子选择电极法原始记录
DROP TABLE IF EXISTS lab_ion_selective_electrode_head;

-- lab_chromatographic_one 色谱法原始记录（1）
DROP TABLE IF EXISTS lab_chromatographic_one_head;

-- lab_colorimetry  比色法原始记录
DROP TABLE IF EXISTS lab_colorimetry_head;

-- lab_free_silica  游离二氧化硅原始记录
DROP TABLE IF EXISTS lab_free_silica_head;

-- lab_atomic_absorption_spectrophotometry 原子吸收分光光度法原始记录
DROP TABLE IF EXISTS lab_atomic_absorption_spectrophotometry_head;

--------------- 原始记录 ---------------

-- lab_calculation_method 计算方法表
CREATE TABLE IF NOT EXISTS `lab_calculation_method`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `test_id` bigint(12) COMMENT '物质ID',
    `original_record_type` VARCHAR(100) COMMENT '原始记录类型',
    `calculation_method` TEXT COMMENT '计算方法',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='计算方法表';
    
    
  -- lab_curve_plotting 标准曲线绘制下表格表（实验）
CREATE TABLE IF NOT EXISTS `lab_curve_plotting`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `original_record_id` bigint(12) COMMENT '原始记录ID',
    `original_record_type` VARCHAR(100) COMMENT '原始记录类型',
    `content_concentration` VARCHAR(50) COMMENT '含量/浓度/log(Cp-)',
    `absorbance_potential` VARCHAR(50) COMMENT '吸光度/电位值',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='标准曲线绘制表（实验）';  
    
-- lab_uv_spectrophotometry_one 紫外可见分光光度法原始记录（1）
CREATE TABLE IF NOT EXISTS `lab_uv_spectrophotometry_one`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `sampling_pressure` VARCHAR(50) COMMENT '采样气压（KPa）',
    `sampling_temperature` VARCHAR(50) COMMENT '采样温度（℃）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
--    `stock_solution` VARCHAR(200) COMMENT '试剂（储备液）',
--    `stock_solution_date` DATE COMMENT '试剂（储备液）配置日期',
--    `application_solution` VARCHAR(200) COMMENT '试剂（应用液）',
--    `application_solution_date` DATE COMMENT '试剂（应用液）配置日期',
    `determination_wavelength` VARCHAR(50) COMMENT '测定波长（仪器条件）nm',
    `reference_solution` VARCHAR(50) COMMENT '参比溶液（仪器条件）',
    `cuvette` VARCHAR(50) COMMENT '比色皿（仪器条件）',
    `chromogenic_time` VARCHAR(50) COMMENT '显色时间（仪器条件）min',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
--    `curve_plotting` VARCHAR(200) COMMENT '标准曲线绘制（实验）',
    `curve_equation` VARCHAR(100) COMMENT '曲线方程（实验）',
    `correlation_coefficient` VARCHAR(100) COMMENT '相关系数（实验）',
    `detection_concentration` VARCHAR(100) COMMENT '最低检出浓度',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `sample_size` VARCHAR(50) COMMENT '取样量',
    `constant_volume` VARCHAR(50) COMMENT '定容体积',
    `absorbance` VARCHAR(50) COMMENT '吸光度A',
    `content` VARCHAR(50) COMMENT '含量',
    `v` VARCHAR(50) COMMENT 'V（L）',
    `vo` VARCHAR(50) COMMENT 'Vo（L）',
    `star_c` VARCHAR(50) COMMENT '*C',
    `c` VARCHAR(50) COMMENT 'C',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='紫外可见分光光度法原始记录（1）';
      
    
-- lab_gravimetric_method 重量法原始记录
CREATE TABLE IF NOT EXISTS `lab_gravimetric_method`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `temperature_befor` VARCHAR(50) COMMENT '采样前（温度℃）',
    `humidity_befor` VARCHAR(50) COMMENT '采样前（湿度%）',
    `pressure_befor` VARCHAR(50) COMMENT '采样前气压（KPa）',
    `weighing_date_befor` DATE COMMENT '采样前称样日期',
    `temperature_after` VARCHAR(50) COMMENT '采样后（温度℃）',
    `humidity_after` VARCHAR(50) COMMENT '采样后（湿度%）',
    `pressure_after` VARCHAR(50) COMMENT '采样后气压（KPa）',
    `weighing_date_after` DATE COMMENT '采样后称样日期',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `sample_size` VARCHAR(50) COMMENT '取样量',
    `after_sampling_one` VARCHAR(50) COMMENT '采样后值1',
    `after_sampling_two` VARCHAR(50) COMMENT '采样后值2',
    `after_sampling_avg` VARCHAR(50) COMMENT '采样后平均值m2',
    `befor_sampling_one` VARCHAR(50) COMMENT '采样前值1',
    `befor_sampling_two` VARCHAR(50) COMMENT '采样前值2',
    `befor_sampling_avg` VARCHAR(50) COMMENT '采样前平均值m1',
    `sample_weight` VARCHAR(50) COMMENT '样品重量（m2-m1）',
    `sample_results` VARCHAR(50) COMMENT '样品结果',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='重量法原始记录';
    
    
-- lab_dust_dispersion 粉尘分散度原始记录
CREATE TABLE IF NOT EXISTS `lab_dust_dispersion`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `dust_particles_one` VARCHAR(50) COMMENT '尘粒数，百分比（<2）',
    `dust_particles_two` VARCHAR(50) COMMENT '尘粒数，百分比（2~）',
    `dust_particles_thr` VARCHAR(50) COMMENT '尘粒数，百分比（5~）',
    `dust_particles_fou` VARCHAR(50) COMMENT '尘粒数，百分比（≥10）',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='粉尘分散度原始记录';
    
    
-- lab_atomic_fluorescence 原子荧光法原始记录
CREATE TABLE IF NOT EXISTS `lab_atomic_fluorescence`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `sampling_pressure` VARCHAR(50) COMMENT '采样气压（KPa）',
    `sampling_temperature` VARCHAR(50) COMMENT '采样温度（℃）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
--    `stock_solution` VARCHAR(200) COMMENT '试剂（储备液）',
--    `stock_solution_date` DATE COMMENT '试剂（储备液）配置日期',
--    `application_solution` VARCHAR(200) COMMENT '试剂（应用液）',
--    `application_solution_date` DATE COMMENT '试剂（应用液）配置日期',
--    `carrier_gas_flow` VARCHAR(50) COMMENT '载气名称及流量（仪器条件）',
    `negative_high_voltage` VARCHAR(50) COMMENT '负高压（仪器条件）',
    `lamp_current` VARCHAR(50) COMMENT '灯电流（仪器条件）',
    `span` VARCHAR(50) COMMENT '量程（仪器条件）',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
--    `curve_plotting` VARCHAR(200) COMMENT '标准曲线绘制（实验）',
    `ao` VARCHAR(50) COMMENT 'ao',
    `curve_equation` VARCHAR(100) COMMENT '曲线方程（实验）',
    `correlation_coefficient` VARCHAR(100) COMMENT '相关系数（实验）',
    `detection_concentration` VARCHAR(100) COMMENT '最低检出浓度',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `sample_size` VARCHAR(50) COMMENT '取样量',
    `constant_volume` VARCHAR(50) COMMENT '定容体积',
    `absorbance` VARCHAR(50) COMMENT '吸光度A',
    `c` VARCHAR(50) COMMENT 'c',
    `v` VARCHAR(50) COMMENT 'V（L）',
    `vo` VARCHAR(50) COMMENT 'Vo（L）',
    `star_c` VARCHAR(50) COMMENT '*C',
    `c_big` VARCHAR(50) COMMENT 'C',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='原子荧光法原始记录'; 
    
   
    
-- lab_ion_selective_electrode 离子选择电极法原始记录
CREATE TABLE IF NOT EXISTS `lab_ion_selective_electrode`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `sampling_pressure` VARCHAR(50) COMMENT '采样气压（KPa）',
    `sampling_temperature` VARCHAR(50) COMMENT '采样温度（℃）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
--    `stock_solution` VARCHAR(200) COMMENT '试剂（储备液）',
--    `stock_solution_date` DATE COMMENT '试剂（储备液）配置日期',
--    `application_solution` VARCHAR(200) COMMENT '试剂（应用液）',
--    `application_solution_date` DATE COMMENT '试剂（应用液）配置日期',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
--    `curve_plotting` VARCHAR(200) COMMENT '标准曲线绘制（实验）',
    `blank_potential` VARCHAR(50) COMMENT '空白电位（实验）',
    `curve_equation` VARCHAR(100) COMMENT '曲线方程（实验）',
    `correlation_coefficient` VARCHAR(100) COMMENT '相关系数（实验）',
    `detection_concentration` VARCHAR(100) COMMENT '最低检出浓度',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `sample_size` VARCHAR(50) COMMENT '取样量',
    `constant_volume` VARCHAR(50) COMMENT '定容体积',
    `potential` VARCHAR(50) COMMENT '电位（mV）',
    `c` VARCHAR(50) COMMENT 'c',
    `v` VARCHAR(50) COMMENT 'V（L）',
    `vo` VARCHAR(50) COMMENT 'Vo（L）',
    `star_c` VARCHAR(50) COMMENT '*C',
    `c_big` VARCHAR(50) COMMENT 'C',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='离子选择电极法原始记录';    
    
   
-- lab_chromatographic_one 色谱法原始记录（1）
CREATE TABLE IF NOT EXISTS `lab_chromatographic_one`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `sampling_pressure` VARCHAR(50) COMMENT '采样气压（KPa）',
    `sampling_temperature` VARCHAR(50) COMMENT '采样温度（℃）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
--    `stock_solution` VARCHAR(200) COMMENT '试剂（储备液）',
--    `stock_solution_date` DATE COMMENT '试剂（储备液）配置日期',
--    `application_solution` VARCHAR(200) COMMENT '试剂（应用液）',
--    `application_solution_date` DATE COMMENT '试剂（应用液）配置日期',
    `chromatographic_column` VARCHAR(50) COMMENT '色谱柱',
    `column_temperature_conditions` VARCHAR(50) COMMENT '柱温条件（℃）',
    `injection_port` VARCHAR(50) COMMENT '进样口（℃）',
    `detector` VARCHAR(50) COMMENT '检测器（℃）',
    `mobile_phase_flow` VARCHAR(50) COMMENT '流动相名称及流量（mL/min）',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
    `detection_concentration` VARCHAR(100) COMMENT '最低检出浓度',
    `v_one` VARCHAR(50) COMMENT 'V1(ml)',
    `d` VARCHAR(50) COMMENT 'D(%)',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `sample_size` VARCHAR(50) COMMENT '取样体积',
    `constant_volume` VARCHAR(50) COMMENT '定容体积',
    `c` VARCHAR(50) COMMENT 'c',
    `v` VARCHAR(50) COMMENT 'V（L）',
    `vo` VARCHAR(50) COMMENT 'Vo（L）',
    `c_big` VARCHAR(50) COMMENT 'C',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='色谱法原始记录（1）';    
    
  
-- lab_colorimetry  比色法原始记录
CREATE TABLE IF NOT EXISTS `lab_colorimetry`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `sampling_pressure` VARCHAR(50) COMMENT '采样气压（KPa）',
    `sampling_temperature` VARCHAR(50) COMMENT '采样温度（℃）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
--    `stock_solution` VARCHAR(200) COMMENT '试剂（储备液）',
--    `stock_solution_date` DATE COMMENT '试剂（储备液）配置日期',
--    `application_solution` VARCHAR(200) COMMENT '试剂（应用液）',
--    `application_solution_date` DATE COMMENT '试剂（应用液）配置日期',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
--    `curve_plotting` VARCHAR(200) COMMENT '标准曲线绘制（实验）',
    `color_developing` VARCHAR(50) COMMENT '显色时间min（实验）',
    `detection_concentration` VARCHAR(100) COMMENT '最低检出浓度',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `content` VARCHAR(50) COMMENT '含量',
    `v` VARCHAR(50) COMMENT 'V（L）',
    `vo` VARCHAR(50) COMMENT 'Vo（L）',
    `c_big` VARCHAR(50) COMMENT 'C',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='比色法原始记录';     
    

-- lab_free_silica  游离二氧化硅原始记录
CREATE TABLE IF NOT EXISTS `lab_free_silica`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `sample_quality` VARCHAR(50) COMMENT '粉尘样品质量m（g）',
    `crucible_suttle` VARCHAR(50) COMMENT '坩埚净重m1（g）',
    `burn_weight_fir` VARCHAR(50) COMMENT '坩埚第1次灼烧后总重m2（g）',
    `burn_weight_sec` VARCHAR(50) COMMENT '坩埚第2次灼烧后总重m3（g）',
    `free_silica_content` VARCHAR(50) COMMENT '游离二氧化硅含量w（%）',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='游离二氧化硅原始记录';   
    
    
    
-- lab_atomic_absorption_spectrophotometry 原子吸收分光光度法原始记录
CREATE TABLE IF NOT EXISTS `lab_atomic_absorption_spectrophotometry`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `deliver_received_id` bigint(12) COMMENT '收样单ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `sample_name` VARCHAR(100) COMMENT '样品名称',
    `sample_status` VARCHAR(100) COMMENT '样品状态',
    `inspection_date` DATE COMMENT '检测日期',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `instrument_status` INT COMMENT '检测过程仪器状态（0异常，1正常）',
    `sampling_pressure` VARCHAR(50) COMMENT '采样气压（KPa）',
    `sampling_temperature` VARCHAR(50) COMMENT '采样温度（℃）',
    `temperature` VARCHAR(50) COMMENT '检测环境条件（温度℃）',
    `humidity` VARCHAR(50) COMMENT '检测环境条件（湿度%）',
--    `stock_solution` VARCHAR(200) COMMENT '试剂（储备液）',
--    `stock_solution_date` DATE COMMENT '试剂（储备液）配置日期',
--    `application_solution` VARCHAR(200) COMMENT '试剂（应用液）',
--    `application_solution_date` DATE COMMENT '试剂（应用液）配置日期',
    `instrument_conditions` VARCHAR(1024) COMMENT '仪器条件',
    `sample_treatment` VARCHAR(200) COMMENT '样品处理（实验）',
--    `curve_plotting` VARCHAR(200) COMMENT '标准曲线绘制（实验）',
    `ao` VARCHAR(50) COMMENT 'Ao',
    `curve_equation` VARCHAR(100) COMMENT '曲线方程（实验）',
    `correlation_coefficient` VARCHAR(100) COMMENT '相关系数（实验）',
    `detection_concentration` VARCHAR(100) COMMENT '最低检出浓度',
    `sample_code` VARCHAR(50) COMMENT '样品编号',
    `sample_size` VARCHAR(50) COMMENT '取样量',
    `constant_volume` VARCHAR(50) COMMENT '定容体积',
    `absorbance` VARCHAR(50) COMMENT '吸光度A',
    `c` VARCHAR(50) COMMENT 'c',
    `v` VARCHAR(50) COMMENT 'V（L）',
    `vo` VARCHAR(50) COMMENT 'Vo（L）',
    `star_c` VARCHAR(50) COMMENT '*C',
    `c_big` VARCHAR(50) COMMENT 'C',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='原子吸收分光光度法原始记录';     
    
    
-- lab_reagent_experiment 试剂及曲线绘制
CREATE TABLE IF NOT EXISTS `lab_reagent_experiment`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `deliver_received_id` bigint(12) COMMENT '收样送样ID',
    `substance_id` bigint(12) COMMENT '物质ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `original_record_type` VARCHAR(100) COMMENT '原始记录类型',
    `stock_one` VARCHAR(50) COMMENT '（1.1） 标准储备液',
    `stock_two` VARCHAR(50) COMMENT '（1.1） 标准储备液',
    `stock_three` VARCHAR(50) COMMENT '（1.1） 标准储备液',
    `stock_four` VARCHAR(50) COMMENT '（1.1） 标准储备液',
    `stock_five` VARCHAR(50) COMMENT '（1.1） 标准储备液',
    `stock_date` DATE COMMENT '（1.1） 标准储备液配置日期',
    `application_one` VARCHAR(50) COMMENT '（1.2） 标准应用液',
    `application_two` VARCHAR(50) COMMENT '（1.2） 标准应用液',
    `application_three` VARCHAR(50) COMMENT '（1.2） 标准应用液',
    `application_four` VARCHAR(50) COMMENT '（1.2） 标准应用液',
    `application_five` VARCHAR(50) COMMENT '（1.2） 标准应用液',
    `application_six` VARCHAR(50) COMMENT '（1.2） 标准应用液',
    `application_date` DATE COMMENT '（1.2） 标准应用液配置日期',
    `curve_plotting_one` VARCHAR(50) COMMENT '标准曲线绘制',
    `curve_plotting_two` VARCHAR(50) COMMENT '标准曲线绘制',
    `curve_plotting_three` VARCHAR(50) COMMENT '标准曲线绘制',
    `curve_plotting_four` VARCHAR(50) COMMENT '标准曲线绘制',
    `curve_plotting_five` VARCHAR(50) COMMENT '标准曲线绘制',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='试剂及曲线绘制'; 
    
  
 --------------------  标准曲线和质控记录 ----------------
 
-- lab_calibration_curve 校准曲线信息
CREATE TABLE IF NOT EXISTS `lab_calibration_curve`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `record_id` bigint(12) COMMENT '原始记录信息ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `analysis_conditions` VARCHAR(100) COMMENT '分析条件',
    `temperature` VARCHAR(50) COMMENT '室温（℃）',
    `humidity` VARCHAR(50) COMMENT '湿度（%）',
    `analysis_number` INT COMMENT '分析编号',
    `addition_volume` VARCHAR(50) COMMENT '标液加入体积(mL)',
    `addition_amount` VARCHAR(50) COMMENT '标准加入量',
    `addition_concentration` VARCHAR(50) COMMENT '标准加入浓度',
    `response_value` VARCHAR(50) COMMENT '响应值（A）',
    `blank_response_value` VARCHAR(50) COMMENT '减空白后响应值',
    `regression_equation` VARCHAR(50) COMMENT '回归方程',
    `correlation_coefficient` VARCHAR(50) COMMENT '相关系数',
    `test_date` DATE COMMENT '校准曲线试验日期',
    `configuration_concentration` VARCHAR(50) COMMENT '配置浓度',
    `configuration_date` DATE COMMENT '配置日期',
    `term_validity` VARCHAR(50) COMMENT '有效期',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='校准曲线信息';
    
 -- lab_parallel_sample_blank 平行样空白
CREATE TABLE IF NOT EXISTS `lab_parallel_sample_blank`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `record_id` bigint(12) COMMENT '原始记录信息ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `analysis_conditions` VARCHAR(100) COMMENT '分析条件',
    `temperature` VARCHAR(50) COMMENT '室温（℃）',
    `humidity` VARCHAR(50) COMMENT '湿度（%）',
    `analysis_number` VARCHAR(100) COMMENT '平行样编号',
    `measured_concentration_one` VARCHAR(50) COMMENT '测得浓度X1',
    `measured_concentration_two` VARCHAR(50) COMMENT '测得浓度X2',
    `relative_deviation` VARCHAR(50) COMMENT '相对偏差 （％）',
    `allowable_relative_deviation` VARCHAR(50) COMMENT '允许相对偏差（％）',
    `result_evaluation` VARCHAR(50) COMMENT '结果评判',
    `lab_blank` VARCHAR(50) COMMENT '实验室空白值',
    `field_blank` VARCHAR(50) COMMENT '现场空白值',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='平行样空白';
       
-- lab_spiked_ecovery_quality 加标回收率及质控样
CREATE TABLE IF NOT EXISTS `lab_spiked_ecovery_quality`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `project_id` bigint(12) COMMENT '项目ID',
    `record_id` bigint(12) COMMENT '原始记录信息ID',
    `identifier` VARCHAR(100) COMMENT '项目编号',
    `test_item_id` bigint(12) COMMENT '检测项目ID',
    `test_item` VARCHAR(100) COMMENT '检测项目',
    `test_basis` VARCHAR(100) COMMENT '检测依据',
    `instrumentation_number` VARCHAR(100) COMMENT '检测仪器及编号',
    `analysis_conditions` VARCHAR(100) COMMENT '分析条件',
    `temperature` VARCHAR(50) COMMENT '室温（℃）',
    `humidity` VARCHAR(50) COMMENT '湿度（%）',
    `analysis_number` INT COMMENT '分析编号',
    `spiked_concentration` VARCHAR(50) COMMENT '加标液浓度',
    `spiked_volume` VARCHAR(50) COMMENT '加标体积 （ mL）',
    `spiked_amount_c` VARCHAR(50) COMMENT '加标量C',
    `measured_value_b` VARCHAR(50) COMMENT '测得值B',
    `sample_measured_value_a` VARCHAR(50) COMMENT '原样品测得值A',
    `rate_recovery` VARCHAR(50) COMMENT '回收率',
    `allowable_recovery_rate` VARCHAR(50) COMMENT '允许回收率',
    `result_evaluation_recovery` VARCHAR(50) COMMENT '结果评判(加标回收率)',
    `number` VARCHAR(50) COMMENT '编号',
    `fixed_value_s` VARCHAR(50) COMMENT '定值S',
    `measured_value_x` VARCHAR(50) COMMENT '测得值X',
    `relative_error` VARCHAR(50) COMMENT '相对误差（％）',
    `allowable_relative_error` VARCHAR(50) COMMENT '允许相对误差（％）',
    `result_evaluation_quality` VARCHAR(50) COMMENT '结果评判(质控样)',
    `configuration_date` DATE COMMENT '配置日期',
    `term_validity` VARCHAR(50) COMMENT '有效期',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='加标回收率及质控样';
     
-- lab_field_unit 原始记录选择单位字段
CREATE TABLE IF NOT EXISTS `lab_field_unit`  (
    `id` bigint(12) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `test_id` bigint(12) COMMENT '物质ID',
    `record_id` bigint(12) COMMENT '原始记录信息ID',
    `original_record_type` VARCHAR(100) COMMENT '原始记录类型',
    `addition_amount` VARCHAR(50) COMMENT '标准加入量',
    `addition_concentration` VARCHAR(50) COMMENT '标准加入浓度',
    `measured_concentration_one` VARCHAR(50) COMMENT '测得浓度X1',
    `measured_concentration_two` VARCHAR(50) COMMENT '测得浓度X2',
    `lab_blank` VARCHAR(50) COMMENT '实验室空白值',
    `field_blank` VARCHAR(50) COMMENT '现场空白值',
    `spiked_concentration` VARCHAR(50) COMMENT '加标液浓度',
    `spiked_amount_c` VARCHAR(50) COMMENT '加标量C',
    `measured_value_b` VARCHAR(50) COMMENT '测得值B',
    `sample_measured_value_a` VARCHAR(50) COMMENT '原样品测得值A',
    `fixed_value_s` VARCHAR(50) COMMENT '定值S',
    `measured_value_x` VARCHAR(50) COMMENT '测得值X',
    `sample_size` VARCHAR(50) COMMENT '取样量',
    `constant_volume` VARCHAR(50) COMMENT '定容体积',
    `content` VARCHAR(50) COMMENT '含量',
    `star_c` VARCHAR(50) COMMENT '*C',
    `c` VARCHAR(50) COMMENT 'c',
    `after_sampling_one` VARCHAR(50) COMMENT '采样后值1',
    `after_sampling_two` VARCHAR(50) COMMENT '采样后值2',
    `after_sampling_avg` VARCHAR(50) COMMENT '采样后平均值m2',
    `befor_sampling_one` VARCHAR(50) COMMENT '采样前值1',
    `befor_sampling_two` VARCHAR(50) COMMENT '采样前值2',
    `befor_sampling_avg` VARCHAR(50) COMMENT '采样前平均值m1',
    `sample_weight` VARCHAR(50) COMMENT '样品重量（m2-m1）',
    `sample_results` VARCHAR(50) COMMENT '样品结果',
    `c_big` VARCHAR(50) COMMENT 'C',
    `detection_concentration` VARCHAR(50) COMMENT '最低检出浓度',
    `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
    `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`)
    ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='原始记录选择单位字段';
    
    