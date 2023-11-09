package may.yuntian.external.oa.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.anlian.entity.ProjectEntity;
import may.yuntian.external.oa.vo.ProjectAllQueryVo;
import may.yuntian.external.oa.vo.ProjectQueryVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author mi
 */
@Mapper
public interface ProjectQueryMapper extends BaseMapper<ProjectEntity> {


    /**
     * 项目编号+受检单位筛选我的项目
     *
     * @param wrapper
     * @return
     */
    @Select("SELECT p.username, c.contract_status,c.deal_status,(CASE c.contract_status WHEN 1 THEN c.contract_status_time ELSE NULL END) AS  contractStatusTime,(CASE c.deal_status WHEN 1 THEN c.deal_status_time ELSE NULL END) AS  dealStatusTime,p.charge,u.mobile as chargePhone, p.id,p.identifier,p.company,p.project_name,p.business_source,p.type,pd.sign_date,p.createtime as createTime,pa.nosettlement_money,p.salesmen,p.total_money,p.netvalue,pa.invoice_money,pa.receipt_money,\n" +
            "(CASE p.status\n" +
            "        WHEN 1 THEN\n" +
            "                \"项目录入\"\n" +
            "        WHEN 2 THEN\n" +
            "                \"下发\"\n" +
            "        WHEN 3 THEN\n" +
            "                \"排单\"\n" +
            "        WHEN 4 THEN\n" +
            "                \"现场调查\"\n" +
            "        WHEN 5 THEN\n" +
            "                \"采样\"\n" +
            "        WHEN 10 THEN\n" +
            "                \"送样\"\n" +
            "        WHEN 20 THEN\n" +
            "                \"检测报告\"\n" +
            "        WHEN 22 THEN\n" +
            "                \"检测报告发送\"\n" +
            "        WHEN 35 THEN\n" +
            "                \"报告编制\"\n" +
            "        WHEN 36 THEN\n" +
            "                \"审核\"\n" +
            "        WHEN 37 THEN\n" +
            "                \"专家评审\"\n" +
            "        WHEN 38 THEN\n" +
            "                \"出版前校核\"\n" +
            "        WHEN 40 THEN\n" +
            "                \"质控签发\"\n" +
            "        WHEN 50 THEN\n" +
            "                \"报告寄送\"\n" +
            "        WHEN 60 THEN\n" +
            "                \"项目归档\"\n" +
            "        WHEN 70 THEN\n" +
            "                \"项目完结\"\n" +
            "        WHEN 90 THEN\n" +
            "                \"项目挂起\"\n" +
            "        WHEN 98 THEN\n" +
            "                \"项目挂起\"\n" +
            "        WHEN 99 THEN\n" +
            "                \"项目终止\"\n" +
            "        ELSE\n" +
            "                p.status\n" +
            "END\n" +
            ") status\n" +
            "\t\n" +
            "FROM al_project p LEFT JOIN\n" +
            "al_project_date  pd  ON p.id=pd.project_id\n" +
            "LEFT JOIN  al_project_amount pa ON pd.project_id=pa.project_id\n" +
            "LEFT JOIN sys_user u on p.charge_id = u.user_id " +
            "LEFT JOIN t_contract c ON p.contract_id = c.id " +
            "${ew.customSqlSegment}")
    List<ProjectQueryVo> getMyAllProject(@Param(Constants.WRAPPER) QueryWrapper wrapper);


    /**
     * 项目编号+受检单位+业务员查询所有公司项目
     *
     * @param wrapper
     * @return
     */
    @Select("SELECT p.username, c.contract_status,c.deal_status,(CASE c.contract_status WHEN 1 THEN c.contract_status_time ELSE NULL END) AS  contractStatusTime,(CASE c.deal_status WHEN 1 THEN c.deal_status_time ELSE NULL END) AS  dealStatusTime,p.charge, p.id,p.identifier,p.company,p.project_name,p.business_source,p.type,pd.sign_date,p.createtime as createTime,pa.nosettlement_money,p.salesmen,p.total_money,p.netvalue,pa.invoice_money,pa.receipt_money,\n" +
            "(CASE p.status\n" +
            "        WHEN 1 THEN\n" +
            "                \"项目录入\"\n" +
            "        WHEN 2 THEN\n" +
            "                \"下发\"\n" +
            "        WHEN 3 THEN\n" +
            "                \"排单\"\n" +
            "        WHEN 4 THEN\n" +
            "                \"现场调查\"\n" +
            "        WHEN 5 THEN\n" +
            "                \"采样\"\n" +
            "        WHEN 10 THEN\n" +
            "                \"送样\"\n" +
            "        WHEN 20 THEN\n" +
            "                \"检测报告\"\n" +
            "        WHEN 22 THEN\n" +
            "                \"检测报告发送\"\n" +
            "        WHEN 35 THEN\n" +
            "                \"报告编制\"\n" +
            "        WHEN 36 THEN\n" +
            "                \"审核\"\n" +
            "        WHEN 37 THEN\n" +
            "                \"专家评审\"\n" +
            "        WHEN 38 THEN\n" +
            "                \"出版前校核\"\n" +
            "        WHEN 40 THEN\n" +
            "                \"质控签发\"\n" +
            "        WHEN 50 THEN\n" +
            "                \"报告寄送\"\n" +
            "        WHEN 60 THEN\n" +
            "                \"项目归档\"\n" +
            "        WHEN 70 THEN\n" +
            "                \"项目完结\"\n" +
            "        WHEN 90 THEN\n" +
            "                \"项目挂起\"\n" +
            "        WHEN 98 THEN\n" +
            "                \"项目挂起\"\n" +
            "        WHEN 99 THEN\n" +
            "                \"项目终止\"\n" +
            "        ELSE\n" +
            "                p.status\n" +
            "END\n" +
            ") status\n" +
            "\t\n" +
            "FROM al_project p LEFT JOIN\n" +
            "al_project_date  pd  ON p.id=pd.project_id\n" +
            "LEFT JOIN  al_project_amount pa ON pd.project_id=pa.project_id\n" +
            "LEFT JOIN t_contract c ON p.contract_id = c.id " +
            "${ew.customSqlSegment}")
    List<ProjectAllQueryVo> getMyAllProjectAll(@Param(Constants.WRAPPER) QueryWrapper wrapper);
}
