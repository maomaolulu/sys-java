package may.yuntian.wordgenerate.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("word_contract_type")
public class WordContractTypeEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String type;
    private String name;
    private String value;

}
