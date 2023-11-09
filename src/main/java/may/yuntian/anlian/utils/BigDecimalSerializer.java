package may.yuntian.anlian.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Objects;

///**
// * @author xzk
// * @Description Bigdecimal保留小数
// */
//@Data
//public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {
//
//    @Override
//    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//        if (ObjectUtil.isEmpty(value)) {
//            return;
//        }
//        gen.writeString(value.setScale(2, RoundingMode.HALF_UP).toString());
//    }
//}
@JsonComponent
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> implements ContextualSerializer {
    //格式化BigDecimal类型的小数位数(默认)
    private String format = "#####0.00";


    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(new DecimalFormat(format).format(bigDecimal));
    }

    /**
     * createContextual可以获得字段的类型以及注解。
     * createContextual方法只会在第一次序列化字段时调用（因为字段的上下文信息在运行期不会改变），所以不用担心影响性能。
     *
     * @param serializerProvider
     * @param beanProperty
     * @return
     * @throws JsonMappingException
     */
    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        //当前字段不为空
        if (beanProperty != null) {
            //如果当前字段类型为BigDecimal类型则处理,否则跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), BigDecimal.class)) {
                //获取当前字段的自定义注解
                OMSBigDecimalFormat annotation = beanProperty.getAnnotation(OMSBigDecimalFormat.class);
                //没有打自定义注解,获取上下文中的自定义注解
                if (annotation == null) {
                    annotation = beanProperty.getContextAnnotation(OMSBigDecimalFormat.class);
                }
                BigDecimalSerializer bigDecimalSerializer = new BigDecimalSerializer();
                String tmp = "#####0.";
                //打了自定义注解的字段
                if (annotation != null) {
                    //获取想保留的小数位数
                    for (int i=0;i< annotation.value();i++){
                        tmp = tmp + "0";
                    }
                    bigDecimalSerializer.format = tmp;
                }
                return bigDecimalSerializer;
            }
            return serializerProvider.findContentValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }
}
