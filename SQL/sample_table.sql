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


-- t_weather	检测时气象条件表
CREATE TABLE IF NOT EXISTS `t_weather`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT COMMENT '现场采样计划ID',
 `weather` VARCHAR(200) COMMENT '天气情况',
 `temperature_min` FLOAT COMMENT '最低温度(℃)',
 `temperature_max` FLOAT COMMENT '最高温度(℃)',
 `humidity_rh` FLOAT COMMENT '湿度%RH',
 `wind_speed` FLOAT COMMENT '风速m/s',
 `pressure` FLOAT COMMENT '气压kPa',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='检测时气象条件表';

-- t_noise_fixed	噪声定点测量信息表
CREATE TABLE IF NOT EXISTS `t_noise_fixed`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `calib_model_id` VARCHAR(200) COMMENT '声校准器型号/编号',
 `calib_value` VARCHAR(50) COMMENT '声校准器校准值',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `code` VARCHAR(50) COMMENT '测量编号',
 `protective_measures` VARCHAR(500) COMMENT '生产情况、个人防护用品使用情况',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间(几点几分)',
 `test_point` VARCHAR(50) COMMENT '测量位置',
 `result1` FLOAT COMMENT '第一次测量结果',
 `result2` FLOAT COMMENT '第二次测量结果',
 `result3` FLOAT COMMENT '第三次测量结果',
 `result_avg` FLOAT COMMENT '测量结果平均值',
 `laeq` FLOAT COMMENT 'LAeq,Te [dB﹙A﹚]',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='噪声定点测量信息';

-- t_noise_individual	噪声个体测量信息表
CREATE TABLE IF NOT EXISTS `t_noise_individual`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `calib_model_id` VARCHAR(200) COMMENT '声校准器型号/编号',
 `calib_value` VARCHAR(50) COMMENT '声校准器校准值',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `people` VARCHAR(50) COMMENT '佩戴人姓名',
 `protective_measures` VARCHAR(500) COMMENT '生产情况、个人防护用品使用情况',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间(几点几分)',
 `keep_time` FLOAT COMMENT '持续时间(h)',
 `laeq` FLOAT COMMENT 'LAeq,Te [dB﹙A﹚]',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='噪声个体测量信息';

-- t_noise_pulse	脉冲噪声强度测试信息表
CREATE TABLE IF NOT EXISTS `t_noise_pulse`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `calib_model_id` VARCHAR(200) COMMENT '声校准器型号/编号',
 `calib_value` VARCHAR(50) COMMENT '声校准器校准值',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `test_date` VARCHAR(50) COMMENT '测量日期',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `peak_value` FLOAT COMMENT '脉冲峰值dB(A)',
 `pulse_times` INT COMMENT '脉冲次数(次/分钟)',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `touch_times` INT COMMENT '接触总次数',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='脉冲噪声强度测试信息';

-- t_noise_octave	噪声倍频程测量记录信息表
CREATE TABLE IF NOT EXISTS `t_noise_octave`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `test_date` VARCHAR(50) COMMENT '测量日期',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `spectrum` VARCHAR(50) COMMENT '频段',
 `result1` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 1',
 `result2` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 2',
 `result3` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 3',
 `result4` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 4',
 `result5` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 5',
 `result6` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 6',
 `result7` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 7',
 `result8` FLOAT COMMENT '1/1(1/3)倍频程测量值dB(A) 8',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='噪声倍频程测量记录信息';

-- t_temperature_stable	高温(热源稳定)测试记录表
CREATE TABLE IF NOT EXISTS `t_temperature_stable`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info`  VARCHAR(100) COMMENT '设备型号和参数',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `labor_density` VARCHAR(50) COMMENT '体力劳动等级强度',
 `wbgt_head` FLOAT COMMENT 'WBGT指数(℃) 头部',
 `wbgt_abdomen` FLOAT COMMENT 'WBGT指数(℃) 腹部',
 `wbgt_foot` FLOAT COMMENT 'WBGT指数(℃) 踝部',
 `wbgt_avg` FLOAT COMMENT 'WBGT指数平均值(℃)',
 `wbgt` FLOAT COMMENT 'WBGT(℃) 时间加权平均值',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='高温(热源稳定)测试记录';

-- t_temperature_unstable	高温(热源不稳定)测试记录表
CREATE TABLE IF NOT EXISTS `t_temperature_unstable`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info` VARCHAR(100) COMMENT '设备型号和参数',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `labor_density` VARCHAR(50) COMMENT '体力劳动等级强度',
 `wbgt_head` FLOAT COMMENT 'WBGT指数(℃) 头部',
 `wbgt_abdomen` FLOAT COMMENT 'WBGT指数(℃) 腹部',
 `wbgt_foot` FLOAT COMMENT 'WBGT指数(℃) 踝部',
 `wbgt_avg` FLOAT COMMENT 'WBGT指数平均值(℃)',
 `wbgt` FLOAT COMMENT 'WBGT(℃) 时间加权平均值',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='高温(热源不稳定)测试记录';

-- t_vibration_hand	手传振动强度测试记录表
CREATE TABLE IF NOT EXISTS `t_vibration_hand`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info` VARCHAR(100) COMMENT '设备型号和参数',
 `job_content` VARCHAR(100) COMMENT '工作内容',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time_zh` VARCHAR(50) COMMENT 'zh值的测量时间',
 `result_zh` FLOAT COMMENT '计权振动加速度(m/s²)azh',
 `test_time_xh` VARCHAR(50) COMMENT 'xh值的测量时间',
 `result_xh` FLOAT COMMENT '计权振动加速度(m/s²)axh',
 `test_time_yh` VARCHAR(50) COMMENT 'yh值的测量时间',
 `result_yh` FLOAT COMMENT '计权振动加速度(m/s²)ayh',
 `result_max` FLOAT COMMENT '最大值(m/s²)',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='手传振动强度测试记录';

-- t_electromagnetic	高频电磁场强度测试记录表
CREATE TABLE IF NOT EXISTS `t_electromagnetic`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号/修正系数',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info`  VARCHAR(100) COMMENT '设备名称和频率范围',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `electric1` FLOAT COMMENT '电场强度(v/m)1',
 `electric2` FLOAT COMMENT '电场强度(v/m)2',
 `electric3` FLOAT COMMENT '电场强度(v/m)3',
 `electric_avg` FLOAT COMMENT '电场强度(v/m)平均值',
 `magnetic1` FLOAT COMMENT '磁场强度(A/m)1',
 `magnetic2` FLOAT COMMENT '磁场强度(A/m)2',
 `magnetic3` FLOAT COMMENT '磁场强度(A/m)3',
 `magnetic_avg` FLOAT COMMENT '磁场强度(A/m)平均值',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='高频电磁场强度测试记录';

-- t_electric	工频电场强度测试记录表
CREATE TABLE IF NOT EXISTS `t_electric`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号/修正系数',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info`  VARCHAR(100) COMMENT '设备型号和参数',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `result1` FLOAT COMMENT '电场强度(KV/m)1',
 `result2` FLOAT COMMENT '电场强度(KV/m)2',
 `result3` FLOAT COMMENT '电场强度(KV/m)3',
 `result_avg` FLOAT COMMENT '电场强度(KV/m)平均值',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='工频电场强度测试记录';

-- t_microwave	微波辐射测试记录表
CREATE TABLE IF NOT EXISTS `t_microwave`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号/修正系数',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info` VARCHAR(100) COMMENT '微波设备型号和参数',
 `radiation_type` VARCHAR(50) COMMENT '微波辐射类型',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `head1` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  头1',
 `head2` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  头2',
 `head3` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  头3',
 `chest1` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  胸1',
 `chest2` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  胸2',
 `chest3` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  胸3',
 `abdomen1` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  腹1',
 `abdomen2` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  腹2',
 `abdomen3` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  腹3',
 `hand1` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  手1',
 `hand2` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  手2',
 `hand3` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  手3',
 `foot1` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  脚1',
 `foot2` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  脚2',
 `foot3` FLOAT COMMENT '微波辐射功率密度值(μw/cm²)  脚3',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='微波辐射测试记录';

-- t_ultraviolet	紫外辐射测试记录表
CREATE TABLE IF NOT EXISTS `t_ultraviolet`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号/修正系数',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info` VARCHAR(100) COMMENT '设备',
 `touch_time` FLOAT COMMENT '接触时间(h/d)',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `inside_longwave` FLOAT COMMENT '眼、面罩内长波(μw/cm²)',
 `inside_mediumwave` FLOAT COMMENT '眼、面罩内中波(μw/cm²)',
 `inside_shortwave` FLOAT COMMENT '眼、面罩内短波(μw/cm²)',
 `inside_illumination` FLOAT COMMENT '眼、面罩内有效照度(μw/cm²)',
 `outside_longwave` FLOAT COMMENT '眼、面罩外长波(μw/cm²)',
 `outside_mediumwave` FLOAT COMMENT '眼、面罩外中波(μw/cm²)',
 `outside_shortwave` FLOAT COMMENT '眼、面罩外短波(μw/cm²)',
 `outside_illumination` FLOAT COMMENT '眼、面罩外有效照度(μw/cm²)',
 `protective_measures` VARCHAR(500) COMMENT '防护措施',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='紫外辐射测试记录';

-- t_illumination	照度测试记录表
CREATE TABLE IF NOT EXISTS `t_illumination`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号/修正系数',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `area` FLOAT COMMENT '面积(m²)',
 `point` VARCHAR(50) COMMENT '测试位置',
 `result1` FLOAT COMMENT '照度lx(示值) 1',
 `result2` FLOAT COMMENT '照度lx(示值) 2',
 `result3` FLOAT COMMENT '照度lx(示值) 3',
 `result4` FLOAT COMMENT '照度lx(示值) 4',
 `result5` FLOAT COMMENT '照度lx(示值) 5',
 `result6` FLOAT COMMENT '照度lx(示值) 6',
 `result_avg` FLOAT COMMENT '平均值lx',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='照度测试记录';

-- t_co	一氧化碳定点测试记录表
CREATE TABLE IF NOT EXISTS `t_co`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info` VARCHAR(100) COMMENT '仪器编号',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `protective_measures` VARCHAR(500) COMMENT '生产情况、个人防护用品使用情况',
 `result1` FLOAT COMMENT '检测读数(PPM) 1',
 `result2` FLOAT COMMENT '检测读数(PPM) 2',
 `result3` FLOAT COMMENT '检测读数(PPM 3',
 `avg` FLOAT COMMENT '平均值(PPM)',
 `result` FLOAT COMMENT '结果浓度(mg/ m³)',
 `scene` VARCHAR(50) COMMENT '温度、湿度、气压',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='一氧化碳定点测试记录';

-- t_co2	二氧化碳定点测试记录表
CREATE TABLE IF NOT EXISTS `t_co2`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `equipment_info` VARCHAR(100) COMMENT '仪器编号',
 `test_time` VARCHAR(50) COMMENT '测量时间',
 `protective_measures` VARCHAR(500) COMMENT '生产情况、个人防护用品使用情况',
 `result1` FLOAT COMMENT '检测读数(%) 1',
 `result2` FLOAT COMMENT '检测读数(%) 2',
 `result3` FLOAT COMMENT '检测读数(%) 3',
 `avg` FLOAT COMMENT '平均值(%)',
 `result` FLOAT COMMENT '结果浓度(mg/ m³)',
 `scene` VARCHAR(50) COMMENT '温度、湿度、气压',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='二氧化碳定点测试记录';

-- t_wind_speed	排风罩风速测试记录表
CREATE TABLE IF NOT EXISTS `t_wind_speed`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `point` VARCHAR(50) COMMENT '测试位置',
 `form` VARCHAR(50) COMMENT '排风罩形式(矩形、条缝罩、圆形罩、其它)',
 `area` FLOAT COMMENT '排风罩面积(长*宽/半径*半径m²)',
 `result1` FLOAT COMMENT '测量值(m/s) 1',
 `result2` FLOAT COMMENT '测量值(m/s) 2',
 `result3` FLOAT COMMENT '测量值(m/s) 3',
 `result4` FLOAT COMMENT '测量值(m/s) 4',
 `result5` FLOAT COMMENT '测量值(m/s) 5',
 `result6` FLOAT COMMENT '测量值(m/s) 6',
 `result_avg` FLOAT COMMENT '平均值(m/s)',
 `scene` VARCHAR(200) COMMENT '现场情况',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='排风罩风速测试记录';

-- t_wind_pressure	风道内压力测试记录表
CREATE TABLE IF NOT EXISTS `t_wind_pressure`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `form` VARCHAR(50) COMMENT '管道形状(矩形、圆形、其它)',
 `area` FLOAT COMMENT '管道截面积(m²)',
 `point` VARCHAR(50) COMMENT '测试位置',
 `result_dynamic` FLOAT COMMENT '动压(Pa)',
 `result_static` FLOAT COMMENT '静压(Pa)',
 `result_total` FLOAT COMMENT '全压(Pa)',
 `scene` VARCHAR(200) COMMENT '现场情况',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='风道内压力测试记录';

-- t_microclimate	微小气候测试记录表
CREATE TABLE IF NOT EXISTS `t_microclimate`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `test_time` VARCHAR(50) COMMENT '测定时间',
 `temperature` FLOAT COMMENT '温度(℃)',
 `humidity_rh` FLOAT COMMENT '湿度%RH',
 `wind_speed` FLOAT COMMENT '风速m/s',
 `pressure` FLOAT COMMENT '气压kPa',
 `weather` VARCHAR(200) COMMENT '天气情况',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='微小气候测试记录';

-- t_temperature_weather	高温气象条件测试记录表
CREATE TABLE IF NOT EXISTS `t_temperature_weather`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `humidity` VARCHAR(50) COMMENT '环境湿度',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `test_time` VARCHAR(50) COMMENT '测定时间',
 `heat_source` VARCHAR(200) COMMENT '热源情况',
 `dry_ball` FLOAT COMMENT '干球 (℃)',
 `wet_bulb` FLOAT COMMENT '湿球 (℃)',
 `black_ball` FLOAT COMMENT '黑球 (℃)',
 `wind_speed` FLOAT COMMENT '风速(m/s)',
 `pressure` FLOAT COMMENT '气压(kPa)',
 `weather` FLOAT COMMENT '天气情况',
 `radiation` FLOAT COMMENT '热辐射强度(Em,J/( cm²·min)',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='高温气象条件测试记录';

-- t_air_fixed	空气中有害物质定点采样记录表
CREATE TABLE IF NOT EXISTS `t_air_fixed`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `calibration_info` VARCHAR(200) COMMENT '校准仪器名称/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `substance` VARCHAR(200) COMMENT '检测物质',
 `sample_code` VARCHAR(50) COMMENT '样品编号',
 `instrument_code` VARCHAR(50) COMMENT '仪器编号',
 `protective_measures` VARCHAR(500) COMMENT '生产情况、个人防护用品使用情况',
 `before_flow` FLOAT COMMENT '采样前流量(L/min)',
 `after_flow` FLOAT COMMENT '采样后流量(L/min)',
 `begin_time` VARCHAR(50) COMMENT '采样开始时间',
 `end_time` VARCHAR(50) COMMENT '采样结束时间',
 `touch_time` FLOAT COMMENT '代表接触时间(h)',
 `temperature_pressure` VARCHAR(500) COMMENT '温度气压',
 `result` FLOAT COMMENT '检测结果(mg/m3)',
 `operator` VARCHAR(50) DEFAULT '=' COMMENT '运算符号(小于号为低于检出值)',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='空气中有害物质定点采样记录';

-- t_air_dust	空气中粉尘分散度定点采样记录表
CREATE TABLE IF NOT EXISTS `t_air_dust`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `material` VARCHAR(200) COMMENT '待测物',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `sample_code` VARCHAR(50) COMMENT '样品编号',
 `instrument_code` VARCHAR(50) COMMENT '仪器编号',
 `protective_measures` VARCHAR(500) COMMENT '生产情况、个人防护用品使用情况',
 `before_flow` FLOAT COMMENT '采样前流量(L/min)',
 `after_flow` FLOAT COMMENT '采样后流量(L/min)',
 `begin_time` VARCHAR(50) COMMENT '采样开始时间',
 `end_time` VARCHAR(50) COMMENT '采样结束时间',
 `touch_time` FLOAT COMMENT '代表接触时间(h)',
 `temperature_pressure` VARCHAR(500) COMMENT '温度气压',
 `result` FLOAT COMMENT '检测结果(mg/m3)',
 `operator` VARCHAR(50) DEFAULT '=' COMMENT '运算符号(小于号为低于检出值)',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='空气中粉尘分散度定点采样记录';

-- t_air_individual	空气中有害物质个体采样记录表
CREATE TABLE IF NOT EXISTS `t_air_individual`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `basis` VARCHAR(500) COMMENT '检测依据',
 `name_model_id` VARCHAR(200) COMMENT '仪器名称/型号/编号',
 `calibration_info` VARCHAR(200) COMMENT '校准仪器名称/编号',
 `workshop` VARCHAR(100) COMMENT '车间',
 `post` VARCHAR(100) COMMENT '岗位',
 `profession` VARCHAR(100) COMMENT '工种',
 `place` VARCHAR(200) COMMENT '采样地点',
 `people` VARCHAR(50) COMMENT '佩戴人姓名',
 `substance` VARCHAR(200) COMMENT '检测物质',
 `sample_code` VARCHAR(50) COMMENT '样品编号',
 `instrument_code` VARCHAR(50) COMMENT '仪器编号',
 `protective_measures` VARCHAR(500) COMMENT '生产情况、个人防护用品使用情况',
 `before_flow` FLOAT COMMENT '采样前流量(L/min)',
 `after_flow` FLOAT COMMENT '采样后流量(L/min)',
 `begin_time` VARCHAR(50) COMMENT '采样开始时间',
 `end_time` VARCHAR(50) COMMENT '采样结束时间',
 `touch_time` FLOAT COMMENT '代表接触时间(h)',
 `temperature_pressure` VARCHAR(500) COMMENT '温度气压',
 `result` FLOAT COMMENT '检测结果(mg/m3)',
 `operator` VARCHAR(50) DEFAULT '=' COMMENT '运算符号(小于号为低于检出值)',
 `gather_date` DATE COMMENT '采集日期',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='空气中有害物质个体采样记录';

-- t_sample_img	采样影像记录表
CREATE TABLE IF NOT EXISTS `t_sample_img`(
`id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '自增主键ID',
 `contract_id` BIGINT NOT NULL COMMENT '合同ID',
 `gather_plan_id` BIGINT  COMMENT '现场采样计划ID',
 `url` VARCHAR(1024) COMMENT '影像路径',
 `type` TINYINT DEFAULT 0 COMMENT '类型(0车间/岗位, 1厂区大门)',
 `point` VARCHAR(50) COMMENT '车间/岗位/厂区大门',
 `editor` VARCHAR(50) COMMENT '编制人',
 `createtime` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '数据入库时间(日期)',
 `updatetime` DATETIME ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
 PRIMARY KEY (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COMMENT='采样影像记录';
