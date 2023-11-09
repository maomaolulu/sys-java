package may.yuntian.anlian.utils;

import java.lang.reflect.Field;

public class Calculation {
	
	
	/**
	 * 曲线方程格式转换 y=ax+b -> x=(y-b)/a
	 * @param curveEquation
	 * @return
	 */
	public static String curveEquation(String x,String y,String curveEquation) {
//		String aString = "y=0.1646x+0.0022";
		String a1String = curveEquation.replace("+", "@");
		String[] aStringStr = a1String.split("@");
		String b = aStringStr[1];
		String a2String = aStringStr[0];
		String a2 = a2String.replace("x", "");
		String a3 = a2.replace("=", "@");
		String[] a4 = a3.split("@");
		String a = a4[1];
		
		return x+"="+"("+y+"-"+b+")"+"/"+a;
	}
	
    /**
     * 判断一个对象中的所有属性是否都为空
     * @param object
     * @return
     */
    public static boolean chackObjAllfieldsIsNull(Object object) {
		if (object == null) {
			return true;
		}
		try {
			for(Field f : object.getClass().getDeclaredFields()) {
				f.setAccessible(true);
				if (f.get(object) != null && StringUtils.isNotBlank(f.get(object).toString())) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return true;
	}
	
}
