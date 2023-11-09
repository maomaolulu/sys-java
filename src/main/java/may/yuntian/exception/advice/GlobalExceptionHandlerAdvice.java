package may.yuntian.exception.advice;

import lombok.extern.slf4j.Slf4j;
import may.yuntian.common.utils.R;
import may.yuntian.exception.annotion.ExceptionStatus;
import may.yuntian.exception.custom.AbstractBusinessException;
import may.yuntian.exception.custom.NeverLoginException;
import may.yuntian.exception.custom.RepeatSubmitException;
import may.yuntian.exception.custom.TokenCheckException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description 全局异常捕获
 * @Date 2023/4/18 13:54
 * @Author maoly
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandlerAdvice {



    @ExceptionHandler(value = Exception.class)
    public R handleException(Exception ex) {
        log.error(ex.getMessage(),ex);
        return R.error(ex.getMessage());
    }

    @ExceptionHandler(value = NeverLoginException.class)
    public R handleNullPointerException(AbstractBusinessException ex) {
        ExceptionStatus annotation = ex.getClass().getAnnotation(ExceptionStatus.class);
        if (null == annotation) {
            log.error("Invalid annotation config, {}", ex.getClass(), ex);
            return R.error("系统异常");
        }
        log.error("系统异常", ex);
        return R.error(annotation.status().getCode(), ex.getTips());
    }

    @ExceptionHandler(value = TokenCheckException.class)
    public R handleTokenCheckException(AbstractBusinessException ex) {
        ExceptionStatus annotation = ex.getClass().getAnnotation(ExceptionStatus.class);
        if (null == annotation) {
            log.error("Invalid annotation config, {}", ex.getClass(), ex);
            return R.error("系统异常");
        }
        log.error("系统异常", ex);
        return R.error(annotation.status().getCode(), ex.getTips());
    }

    @ExceptionHandler(value = RepeatSubmitException.class)
    public R handleRepeatSubmitException(AbstractBusinessException ex) {
        ExceptionStatus annotation = ex.getClass().getAnnotation(ExceptionStatus.class);
        if (null == annotation) {
            log.error("Invalid annotation config, {}", ex.getClass(), ex);
            return R.error("系统异常");
        }
        log.error("重复提交", ex);
        return R.error(annotation.status().getCode(), ex.getTips());
    }
}
