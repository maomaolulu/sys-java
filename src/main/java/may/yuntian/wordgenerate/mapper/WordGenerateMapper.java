package may.yuntian.wordgenerate.mapper;

import may.yuntian.wordgenerate.vo.AgreementGenerateVo;
import may.yuntian.wordgenerate.vo.TaskGenerateVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface WordGenerateMapper {


    @Select("select p.id,p.project_name,p.entrust_company,p.entrust_company_id,p.entrust_office_address,p.total_money,p.company,p.company_id,p.office_address,p.salesmen," +
            "p.identifier,DATE_FORMAT(pd.task_release_date,\"%Y-%m-%d\") AS taskReleaseDate,p.charge,p.type,c.contact," +
            "c.mobile AS telephone,u.username,u.mobile AS phoneNumber from al_project p " +
            "LEFT JOIN al_project_date pd on p.id = pd.project_id " +
            "LEFT JOIN t_company c ON p.entrust_company_id = c.id " +
            "LEFT JOIN sys_user u ON p.salesmenid = u.user_id WHERE p.id = #{projectId}")
    TaskGenerateVo getGenerateInfo(@Param("projectId") Long projectId);



    @Select("SELECT c.id,c.identifier,c.project_name,c.company_id,c.company,c.office_address,c.entrust_company_id," +
            "c.entrust_company,c.entrust_office_address,co.legalname AS entrustLegalname,co.code AS entrustCode,co.contact," +
            "co.mobile AS telephone,c.type,c.sign_date,c.total_money,u.username AS salesmen,u.mobile AS phoneNumber FROM t_contract c " +
            "LEFT JOIN t_company co ON c.entrust_company_id = co.id " +
            "LEFT JOIN sys_user u ON c.salesmenid = u.user_id WHERE c.id = #{id}")
    AgreementGenerateVo getContractInfo(@Param("id") Long id);
}
