package may.yuntian.external.province.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 包裹<header> 和 <body>
 * @author: liyongqiang
 * @create: 2023-04-04 14:01
 */
@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
@XStreamAlias("data")
public class Data implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 请求头：<header></header>
     */
    private Header header;
    /**
     * 请求体：<body></body>
     */
    private Body body;
}
