package fun.billon.common.util;

import fun.billon.common.cache.RedisAspect;

import java.util.regex.Matcher;

/**
 * 获取redis键的工具类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class RedisKeyUtils {

    /**
     * 根据namespace,field,fieldValue生成redis键。
     * 1)当field格式为#{model.field}或#{field}时,需要从属性中获取键值,生成的redis键格式为：namespace:field:fieldValue
     * 2)当field格式为${model.field}或${field}时,需要从属性中获取键值,生成的redis键格式为：namespace:fieldValue
     * 3)当field格式为field(固定字串)时,生成的redis键格式为：namespace:field
     *
     * @param namespace  命名空间
     * @param field      属性名称
     * @param fieldValue 属性值,可变参数列表
     * @return 根据namespace, field, fieldValue生成的redis键。格式为：namespace:field:fieldValue或namespace:field
     */
    public static String get(String namespace, String field, String... fieldValue) {
        StringBuilder sb = new StringBuilder(namespace);
        if (field.matches(RedisAspect.PATTERN_CACHE_KEY)) {
            /*
             * field的格式可能包含多个属性,例：#{model.field}${model.field}
             * 1)将field分组
             * 2)获取属性的值并进行拼接
             */
            Matcher matcher = RedisAspect.PATTERN_CACHE_KEY_GROUP.matcher(field);
            int index = 0;
            while (matcher.find()) {
                // 分组
                String groupField = matcher.group();
                // 将#{model.field}中的字串提取出来model.field
                String fieldName = groupField.substring(2, groupField.length() - 1);
                if (groupField.startsWith("#")) {
                    // 如果是以#开头,则将field拼接到键
                    sb.append(":").append(fieldName.substring(fieldName.lastIndexOf(".") + 1));
                }
                //拼接fieldValue
                sb.append(":").append(fieldValue[index++]);
            }
        } else {
            sb.append(":" + field);
        }
        return sb.toString();
    }

}