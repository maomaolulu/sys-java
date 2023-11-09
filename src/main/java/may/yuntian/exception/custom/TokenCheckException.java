package may.yuntian.exception.custom;

import may.yuntian.exception.annotion.ExceptionStatus;
import may.yuntian.exception.enums.ExceptionCode;

/**
 * @Description
 * @Date 2023/4/18 15:19
 * @Author maoly
 **/

@ExceptionStatus(status = ExceptionCode.TOKEN_CHECK)
public class TokenCheckException extends AbstractBusinessException {

    public TokenCheckException(String tips) {
        super(tips);
    }
    public TokenCheckException(String tips, String message) {
        super(tips, message);
    }
}
