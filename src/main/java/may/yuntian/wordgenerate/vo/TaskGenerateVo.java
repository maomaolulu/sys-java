package may.yuntian.wordgenerate.vo;


import lombok.Data;
import may.yuntian.wordgenerate.entity.WordTaskSubstancesEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class TaskGenerateVo {

    private Long id;//项目ID
    private String identifier;//项目编号
    private String projectName;//项目名称
    private String entrustCompany;//委托单位
    private String entrustOfficeAddress;//委托单位地址
    private String contact;//委托单位联系人
    private String telephone;//委托单位联系电话
    private String company;//受检单位
    private String officeAddress;//受检单位地址
    //协议所需
    private Long companyId;
    private Long entrustCompanyId;
    private String link;//受检单位联系人
    private String phone;//受检单位联系人电话
    private BigDecimal totalMoney;


    private String salesmen;//业务员
    private String phoneNumber;//业务员电话
    private String charge;//项目负责人
    private String type;//项目类型
    private String taskReleaseDate;//任务下发日期

    private String typeStr;//任务单放射word中类型字符串
    private String timeLimit;//任务单放射word中完成期限
    private String other;//任务单放射word中其他要求
    private List<WordTaskSubstancesEntity> substanceList;

    private String typeStr1;//合同评审word中类型字符串
    private String typeStr2;//合同评审word中类型字符串
    private String typeStr3;//合同评审word中类型字符串
    private String substanceString;//合同评审word中主要检测项目

    private String showType;//用于前端判断任务单展示类型  pj   hw   jp


}
