package may.yuntian.anlian.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 安联检测相关的配置
 * 包含文件上传、下载的路径信息
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2020年09月13日
 */
@Configuration
public class AnlianConfig {
	/**
	 * 文件上传、下载的存储路径
	 */
	@Value("${anlian.path.file-path: /home/anliandata/}")
	public String filePath;
	
	/**
	 * 工艺流程图路径
	 */
	@Value("${anlian.path.prefix.craftProcess: /home/anliandata/craftProcess/}")
	public String craftProcessPath;
	
	/**
	 * 设备布局测点布置图路径
	 */
	@Value("${anlian.path.prefix.equipment: /home/anliandata/equipment/}")
	public String equipment;

	/**
	 * 第三方业务接口：汝成科技-万达仓库 （测试地址url前缀）
	 */
	@Value("${interface.WD.url}")
	public String wandaPath;

	/**
	 * 技术服务机构密钥
	 */
	@Value("${interface.WD.orgKey}")
	public String orgKey;

	/**
	 * 技术服务机构名称
	 */
	@Value("${interface.WD.orgName}")
	public String orgName;

	/**
	 * 技术服务机构统一社会信用代码
	 */
	@Value("${interface.WD.orgCode}")
	public String orgCode;

	/**
	 * 同步成功的第三方技术服务机构id
	 */
	@Value("${interface.WD.orgId}")
	public String orgId;

	/**
	 * 技术服务机构法人名称
	 */
	@Value("${interface.WD.legalName}")
	public String legalName;
//
//	/**
//	 * 技术服务机构法人电话
//	 */
//	@Value("${interface.WD.legalPhone}")
//	public String legalPhone;
//
//	/**
//	 * 技术服务机构注册行政区划
//	 */
//	@Value("${interface.WD.registerArea}")
//	public String registerArea;
//
//	/**
//	 * 技术服务机构注册行政区划编码
//	 */
//	@Value("${interface.WD.registerArea}")
//	public String registerAreaCode;
//
//	/**
//	 * 技术服务机构注册地址详情
//	 */
//	@Value("${interface.WD.registerArea}")
//	public String registerAddress;

	/**
	 * SF：顾客编码
	 */
	@Value("${interface.SF.clientCode}")
	public String clientCode;

	/**
	 * SF：月结卡号
	 */
	@Value("${interface.SF.monthlyCard}")
	public String monthlyCard;

	/**
	 * SF：校验码
	 */
	@Value("${interface.SF.checkWord}")
	public String checkWord;

	/**
	 * SF：url地址
	 */
	@Value("${interface.SF.callUrl}")
	public String callUrl;

	/**
     * 获取工艺流程图上传路径
     * @return
     */
//    public static String getCraftProcessUploadPath() {
//    	System.out.println("文件上传、下载的存储路径:"+AnlianConfig.filePath+" 工艺流程图路径:"+AnlianConfig.craftProcessPath);
//    	System.out.println("设备布局测点布置图路径:"+(newCommission AnlianConfig()).equipment);
//        return AnlianConfig.filePath + AnlianConfig.craftProcessPath;
//    }
    
//	# 项目相关配置
//	anlian:
//	  # 文件上传、下载的存储路径
//	  path:
//	    file-path: /Users/mayong/develop/anlian/
//	    prefix:
//	      #工艺流程图路径
//	      craftProcess: craftProcess/
//	      #设备布局测点布置图路径
//	      equipment: equipment/
//	      download: download/
//	      upload: upload/

}
