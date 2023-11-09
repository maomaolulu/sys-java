package may.yuntian.wordgenerate.mongoservice;

import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.wordgenerate.dto.SubstanceDto;
import may.yuntian.wordgenerate.mongoentity.EvalPlanRecordEntity;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EvalPlanRecordService {
    @Autowired
    private MongoTemplate mongoTemplate;


    /**
     * 连接MONGODB 获取数据测试
     * @param id
     * @return
     */
    //TODO 测试
    public List<EvalPlanRecordEntity> getOneById(String id){
        System.out.println(id);
        ObjectId o = new ObjectId(id);
//        Query query = new Query(Criteria.where("_id").is(o));
        Query query = new Query(Criteria.where("project_id").is(36252));
//        EvalPlanRecordEntity entity = mongoTemplate.findOne(query,EvalPlanRecordEntity.class);
        List<EvalPlanRecordEntity>  entityList = mongoTemplate.find(query,EvalPlanRecordEntity.class);
        return entityList;
    }


    /**
     * 根据项目ID获取评价采样计划最终版信息列表
     * @param projectId
     * @return
     */
    public List<EvalPlanRecordEntity> getListByProjectId(Long projectId){
        Query query = new Query(Criteria.where("project_id").is(projectId));
        List<EvalPlanRecordEntity>  entityList = mongoTemplate.find(query,EvalPlanRecordEntity.class);
        return entityList;
    }


    /**
     * 根据项目id获取所有检测物质名称--去重后
     * @param projectId
     * @return
     */
    public String getSubstances(Long projectId){
        List<EvalPlanRecordEntity>  entityList = this.getListByProjectId(projectId);
        String substances = "";
        if (StringUtils.isNotEmpty(entityList)){
            List<SubstanceDto> collect = entityList.stream().map(EvalPlanRecordEntity::getSubstance).collect(Collectors.toList());
            substances = collect.stream().map(SubstanceDto::getFinalSubstanceShow).distinct().collect(Collectors.joining("、"));
        }

        return substances;
    }

}
