package may.yuntian.exception.annotion;

import may.yuntian.exception.enums.ExceptionCode;

import java.lang.annotation.*;

/**
 * @Description
 * @Date 2023/4/18 14:28
 * @Author maoly
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ExceptionStatus {
    /**
     * @return 状态
     */
     ExceptionCode status() default ExceptionCode.COMMON_ERROR;
    /**
     * @return 状态描述
     */
    String description() default "";
}
