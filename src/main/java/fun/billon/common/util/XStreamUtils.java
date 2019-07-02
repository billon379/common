package fun.billon.common.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.thoughtworks.xstream.mapper.MapperWrapper;

import java.io.Reader;

/**
 * javaBean和xml的相互转换的工具类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class XStreamUtils {

    /**
     * javaBean装换为xml
     *
     * @param bean javaBean
     * @param <T>  泛型标识
     * @return xml
     */
    public static <T> String toXml(T bean) {
        return init(bean.getClass()).toXML(bean);
    }

    /**
     * xml转换为javaBean
     *
     * @param xml xml
     * @param clz 要转换的类型
     * @param <T> 泛型标识
     * @return 生成的javaBean
     */
    public static <T> T fromXml(String xml, Class<T> clz) {
        return (T) init(clz).fromXML(xml);
    }

    /**
     * xml转换为javaBean
     *
     * @param reader reader
     * @param clz    要转换的类型
     * @param <T>    泛型标识
     * @return 生成的javaBean
     */
    public static <T> T fromXml(Reader reader, Class<T> clz) {
        return (T) init(clz).fromXML(reader);
    }

    /**
     * 初始化XStream实例
     *
     * @param clz 要解析的类型
     * @return XStream
     */
    private static XStream init(Class clz) {
        //防止_被转换为"__"
        XStream xstream = new XStream(new XppDriver(new XmlFriendlyNameCoder("_-", "_"))) {
            // to enable ignoring of unknown elements
            @Override
            protected MapperWrapper wrapMapper(MapperWrapper next) {
                return new MapperWrapper(next) {
                    @Override
                    public boolean shouldSerializeMember(Class definedIn, String fieldName) {
                        if (definedIn == Object.class) {
                            try {
                                return this.realClass(fieldName) != null;
                            } catch (Exception e) {
                                return false;
                            }
                        }
                        return super.shouldSerializeMember(definedIn, fieldName);
                    }
                };
            }
        };
        XStream.setupDefaultSecurity(xstream);
        //应用注解
        xstream.processAnnotations(clz);
        //自动检测注解
        xstream.autodetectAnnotations(true);
        xstream.allowTypes(new Class[]{clz});
        return xstream;
    }

}