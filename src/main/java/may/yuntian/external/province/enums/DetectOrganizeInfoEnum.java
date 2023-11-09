package may.yuntian.external.province.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 检测机构信息：杭州、宁波、嘉兴安联
 * @author: liyongqiang
 * @create: 2023-05-15 10:03
 */
public enum DetectOrganizeInfoEnum {

    /**
     * // Java代码中使用RSA算法生成的唯一固定密钥对（其中，公钥已提供给省疾控研发人员！）
     * // 后期如果需要，亦可生成随机密钥对，从而保证数据传输过程中的安全。参考：RSAUtils工具类。
     */
    HZ("杭州安联", "浙江安联检测技术服务有限公司", "913301085714775903", "王勇", "anlian-test", "anlian123456", "0571-85028656", "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"),
    NB("宁波安联", "宁波安联检测有限公司", "91330201MA282UF146", "钟狄阳", "anlian-ningbo", "ningbo123456", "0574-87913916", "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"),
    JX("嘉兴安联", "嘉兴安联检测技术服务有限公司", "91330401MA28ADM67R", "张袁金", "anlian-jiaxing", "jiaxing123456", "0573-82581393", "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");

    /**
     * 公司简称
     */
    private final String shortName;
    /**
     * 机构名称
     */
    private final String orgName;
    /**
     * 统一社会信用代码
     */
    private final String creditCode;
    /**
     * 机构负责人
     */
    private final String orgDirectorName;
    /**
     * 请求用户标识：账号
     */
    private final String userID;
    /**
     * （省平台分配）密码
     */
    private final String password;
    /**
     * 填表人联系方式
     */
    private final String preparePhone;
    /**
     * 机构公钥：Public key
     */
    private final String publicKey;
    /**
     * 机构私钥：Secret key
     */
    private final String secretKey;

    DetectOrganizeInfoEnum(String shortName, String orgName, String creditCode, String orgDirectorName, String userID, String password, String preparePhone, String publicKey, String secretKey) {
        this.shortName = shortName;
        this.orgName = orgName;
        this.creditCode = creditCode;
        this.orgDirectorName = orgDirectorName;
        this.userID = userID;
        this.password = password;
        this.preparePhone = preparePhone;
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public String getShortName() {
        return shortName;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getCreditCode() {
        return creditCode;
    }

    public String getOrgDirectorName() {
        return orgDirectorName;
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public String getPreparePhone() {
        return preparePhone;
    }

    public String getSecretKey() {
        return secretKey;
    }

    /**
     * 根据当前登录用户所属公司，获取该机构详细信息
     * @param subjection 隶属公司
     * @return 公司枚举常量
     */
    public static DetectOrganizeInfoEnum getOrganizeInfoByLoginUser(String subjection) {
        Optional<DetectOrganizeInfoEnum> first = Arrays.stream(DetectOrganizeInfoEnum.values()).filter(p -> p.getShortName().equals(subjection)).findFirst();
        return first.orElse(null);
    }

}
