package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 请求头！<header></header>
 * @author: liyongqiang
 * @create: 2023-04-04 14:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("header")
public class Header implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 业务请求编码
     */
    private String service;
    /**
     * 请求用户标识
     */
    private String userId;
    /**
     * 请求时间
     */
    private String requestTime;
    /**
     * 用户认证密钥
     */
    private String headSign;
    /**
     * 请求数据签名
     */
    private String bodySign;

}