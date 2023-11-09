package may.yuntian.norepeatsubmit.annotion;


import java.lang.annotation.*;

/**
 * @author ANLIAN
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmit {

    /**
     * 延迟多少秒后可以重复提交
     * @return
     */

    long expire() default 20;

}
