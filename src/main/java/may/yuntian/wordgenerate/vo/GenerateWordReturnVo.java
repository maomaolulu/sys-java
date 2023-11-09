package may.yuntian.wordgenerate.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateWordReturnVo {
    private String temFilePath;
    private String generateFilePath;
    private String generateFileName;
    private TaskGenerateVo taskGenerateVo;
    private AgreementGenerateVo agreementGenerateVo;
}
