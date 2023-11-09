package may.yuntian.external.express.sf.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 云打印面单2.0-扩展字段extJson
 * @author: liyongqiang
 * @create: 2023-06-07 15:50
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Extend implements Serializable {
    private static final long serialVersionUID = 2774275564551643616L;

    /** 订单所属渠道（eg: “medicine” 代表医药渠道运单，cx：代表是CX预约单）	**/
    private String channel;
    /** 自定义脱敏标识，4位0和1组成的字符串，0表示不脱敏，1表示需要脱敏；顺序：第1位：收件人姓名，第2位：寄件人姓名，第3位：收件人地址，第4位：寄件人地址
     *      eg: “1010” 表示收件人姓名和收件人地址脱敏，寄件人姓名和寄件人地址不脱敏,
     *      地址脱敏规则：后10位，如果有阿拉伯数字、中文数字、字母就用*替换
     *      姓名脱敏规则：1个字的 不隐藏，2个字的 隐藏末位，3个字的 隐藏中间，超过三个字，超过部分不打印，隐藏第三位，如欧阳娜娜-欧阳*
     *      手机号脱敏规则：保留前1位和后4位 eg: 1*2345
     * **/
    private String encryptFlag;

}
