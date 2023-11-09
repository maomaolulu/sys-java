package may.yuntian.anlian.utils;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OMS BigDecimal类自定义注解
 */
@Retention(RetentionPolicy.RUNTIME)//运行时注解
@Target(ElementType.FIELD)//作用于字段
@JacksonAnnotationsInside
@JsonSerialize(using = BigDecimalSerializer.class)//指定序列化器
public @interface OMSBigDecimalFormat {
    int value() default 2;//bigDecimal保留的小数位数
}
