package may.yuntian.external.province.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.Writer;

/**
 * @author: liyongqiang
 * @create: 2023-04-03 13:53
 */
public class XmlUtil {
    private static String XML_HEAD = "";

    public static String toXml(Object obj){
        return toXml(XML_HEAD, obj);
    }

    public static String toXml(Object obj, String charset){
        return toXml(XML_HEAD.replaceAll("UTF-8", charset), obj);
    }

    /**
     * java 转换成xml
     * @param obj 对象实例
     * @return String xml字符串
     */
    public static String toXml(String head, Object obj){
        if (obj == null) {
            return "";
        }

//		XStream xstream=newCommission XStream();
//		XStream xstream=newCommission XStream(newCommission DomDriver()); //直接用jaxp dom来解释
        //XStream xstream=newCommission XStream(newCommission DomDriver("utf-8")); //指定编码解析器,直接用jaxp dom来解释
        XStream xstream = createXStream();
        ////如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
        //通过注解方式的，一定要有这句话
        xstream.processAnnotations(obj.getClass());

        if (head == null) {
            return xstream.toXML(obj);
        }

        return head + xstream.toXML(obj);
    }

    /**
     *  将传入xml文本转换成Java对象
     * @param xmlStr
     * @param cls  xml对应的class类
     * @return T   xml对应的class类的实例对象
     * 调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr, PersonBean.class);
     */
    @SuppressWarnings("unchecked")
    public static <T> T  toBean(String xmlStr,Class<T> cls){
        if (xmlStr == null) {
            return null;
        }

        //注意：不是new Xstream(); 否则报错：java.lang.NoClassDefFoundError: org/xmlpull/v1/XmlPullParserFactory
        //XStream xstream=newCommission XStream(newCommission DomDriver("utf-8"));
        XStream xstream = createXStream();
        xstream.processAnnotations(cls);
        T obj = (T)xstream.fromXML(xmlStr);
        return obj;
    }

    public static XStream createXStream() {
        return new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        super.writeText(writer, text);
                    }

                    @Override
                    public String encodeNode(String name) {
                        return name;
                    }
                };
            }
        }){
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @SuppressWarnings("rawtypes")
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            try {
                                return (this.realClass(fieldName) != null);
                            } catch(Exception e) {
                                return false;
                            }
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
    }
}
