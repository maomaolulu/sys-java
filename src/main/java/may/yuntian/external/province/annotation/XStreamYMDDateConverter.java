package may.yuntian.external.province.annotation;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期格式化注解：@XStreamConverter(value = XStreamYMDDateConverter.class)
 */
public class XStreamYMDDateConverter extends AbstractSingleValueConverter {
 
	private static final DateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
 
	@Override
	public boolean canConvert(Class type) {
		return type.equals(Date.class);
	}
 
	@Override
	public Object fromString(String str) {
		// 这里将字符串转换成日期
		try {
			return DEFAULT_DATE_FORMAT.parseObject(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		throw new ConversionException("Cannot parse date " + str);
	}
 
	@Override
	public String toString(Object obj) {
		// 这里将日期转换成字符串
		return DEFAULT_DATE_FORMAT.format((Date) obj);
	}
 
}