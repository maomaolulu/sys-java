package may.yuntian.wordgenerate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubstanceDto {
    /** 物质ID */
    @Field(name = "final_id")
    private Long finalId;

    /** 物质名称 */
    @Field(name = "final_name")
    private String finalName;

    /** 别名 */
    @Field(name = "final_substance_show")
    private String finalSubstanceShow;
}
