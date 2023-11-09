package may.yuntian.exception.custom;

import may.yuntian.exception.annotion.ExceptionStatus;
import may.yuntian.exception.enums.ExceptionCode;

/**
 * @Description
 * @Date 2023/4/18 14:31
 * @Author maoly
 **/
@ExceptionStatus(status = ExceptionCode.NEVER_LOGIN, description = "未登录")
public class NeverLoginException extends AbstractBusinessException {

    public NeverLoginException(String tips) {
        super(tips);
    }
    public NeverLoginException(String tips, String message) {
        super(tips, message);
    }
}
