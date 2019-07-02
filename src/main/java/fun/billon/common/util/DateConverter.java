package fun.billon.common.util;

import org.apache.commons.beanutils.converters.AbstractConverter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * apache BeanUtils日期转换器。apache BeanUtils不支持字串到日期的转换
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class DateConverter extends AbstractConverter {

    /**
     * 支持转换的多种日期格式，可增加时间格式
     */
    private static final DateFormat[] DATE_FORMATS = {
            new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK),
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
            new SimpleDateFormat("yyyy-MM-dd HH:mm"),
            new SimpleDateFormat("yyyy-MM-dd")
    };

    @Override
    protected Class<Date> getDefaultType() {
        return Date.class;
    }

    /**
     * 将指定格式字符串转换为日期类型
     */
    @Override
    public Date convertToType(Class target, Object src) {
        if (target == Date.class && src instanceof String) {
            // 遍历日期支持格式，进行转换
            for (int i = 0; i < DATE_FORMATS.length; i++) {
                try {
                    return DATE_FORMATS[i].parse((String) src);
                } catch (Exception e) {
                    continue;
                }
            }
        }
        return null;
    }

}