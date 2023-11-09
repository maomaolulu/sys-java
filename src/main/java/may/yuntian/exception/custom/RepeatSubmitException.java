package may.yuntian.exception.custom;

import may.yuntian.exception.annotion.ExceptionStatus;
import may.yuntian.exception.enums.ExceptionCode;

/**
 * @Description
 * @Date 2023/4/20 9:15
 * @Author maoly
 **/
@ExceptionStatus(status = ExceptionCode.REPEAT_SUBMIT)
public class RepeatSubmitException extends AbstractBusinessException{

    public RepeatSubmitException(String tips) {
        super(tips);
    }
    public RepeatSubmitException(String tips, String message) {
        super(tips, message);
    }
}
