package may.yuntian.anlian.vo;

import lombok.Data;

/**
 * 项目信息变更接收载体
 *
 * @author hjy
 * @date 2023/6/14 14:04
 */
@Data
public class ProjectChangeInfoVo {
    /**
     * 原项目编号
     */
    private String oldIdentifier;
    /**
     * 新项目编号
     */
    private String newIdentifier;
    /**
     * 新项目类型
     */
    private String newType;
    /**
     * 新合同编号
     */
    private String newContractIdentifier;
    /**
     * 修改原因
     */
    private String remarks;
}
