package may.yuntian.publicity.dto;

import lombok.Data;

/**
 * 评价公示查询字段DTO
 */
@Data
public class PublicPjDto {

    private String type;//项目类型

    private String identifier;//项目编号

    private String company;//受检单位

    private Integer pubStatus;//公示状态 （6.待公示，7.已公示,8.公示失败）

    private Integer bindingStatus;//胶装状态（0.不显示，1.待胶装，2.已胶装）

    private Integer examineStatus;//审核状态（1.未提交，2.待审核，3.主管驳回，4.主管通过/待审核（质控显示‘待审核’），5.质控驳回，6.审核通过）

}
