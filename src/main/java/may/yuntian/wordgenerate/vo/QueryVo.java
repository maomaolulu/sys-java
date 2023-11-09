package may.yuntian.wordgenerate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryVo {
     private Long projectId;
     private Long id;
     private String wordType;
     private Integer type;

}
