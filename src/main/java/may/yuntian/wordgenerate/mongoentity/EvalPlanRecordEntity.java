package may.yuntian.wordgenerate.mongoentity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import may.yuntian.wordgenerate.dto.SubstanceDto;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.io.Serializable;

@Data
@Document("eval_plan_record")
@AllArgsConstructor
@NoArgsConstructor
public class EvalPlanRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** _id 唯一识别ID */
    @MongoId(FieldType.OBJECT_ID)
    private String id;

    /** 项目ID */
    @Field(name = "project_id")
    private Long projectId;

    /** 物质信息 */
    @Field
    private SubstanceDto substance;
}
