package fun.billon.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class DateUtils {

    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_DATE = "yyyy-MM-dd";
    public static final String PATTERN_SIMPLE_DATE = "yyyyMMdd";
    public static final String PATTERN_TIME = "HH:mm:ss";

    /**
     * 将一个日期字串按照指定模式格式化
     *
     * @param date    日期
     * @param pattern 要显示的格式
     * @return 格式化后的字串
     */
    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 将一个日期字串按照指定模式解析为Date
     *
     * @param date    日期字串
     * @param pattern 要转换的格式
     * @return 按照指定模式解析后的日期
     */
    public static Date parse(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取两个日期之间的月份
     *
     * @param startTime 起始日期
     * @param endTime   结束日期
     * @return 两个日期之间的月份
     */
    public static List<String> getMonthsBetween(Date startTime, Date endTime) {
        Calendar calStart = Calendar.getInstance(Locale.getDefault());
        calStart.setTime(startTime);
        calStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calEnd = Calendar.getInstance(Locale.getDefault());
        calEnd.setTime(endTime);
        List<String> list = new ArrayList<>();
        do {
            list.add(format(calStart.getTime(), "yyyyMM"));
            calStart.add(Calendar.MONTH, 1);
        } while (calStart.compareTo(calEnd) <= 0);
        Collections.reverse(list);
        return list;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     */
    public static List<String> getDaysBetween(Date startTime, Date endTime) {
        Calendar calStart = Calendar.getInstance(Locale.getDefault());
        calStart.setTime(startTime);
        Calendar calEnd = Calendar.getInstance(Locale.getDefault());
        calEnd.setTime(endTime);
        List<String> list = new ArrayList<>();
        do {
            list.add(format(calStart.getTime(), "yyyyMMdd"));
            calStart.add(Calendar.DAY_OF_MONTH, 1);
        } while (calStart.compareTo(calEnd) <= 0);
        Collections.reverse(list);
        return list;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param startTime 起始时间
     * @param endTime   结束时间
     */
    public static List<String> getDaysBetween(Date startTime, Date endTime, String pattern) {
        Calendar calStart = Calendar.getInstance(Locale.getDefault());
        calStart.setTime(startTime);
        Calendar calEnd = Calendar.getInstance(Locale.getDefault());
        calEnd.setTime(endTime);
        List<String> list = new ArrayList<>();
        do {
            list.add(format(calStart.getTime(), pattern));
            calStart.add(Calendar.DAY_OF_MONTH, 1);
        } while (calStart.compareTo(calEnd) <= 0);
        return list;
    }

    /**
     * 获取月的第一天
     *
     * @param date 日期
     */
    public static Date getMonthBegin(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取月的最后一天
     *
     * @param date 日期
     */
    public static Date getMonthEnd(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(getMonthBegin(date));
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        cal.roll(Calendar.HOUR_OF_DAY, -1);
        cal.roll(Calendar.MINUTE, -1);
        cal.roll(Calendar.SECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取天的开始时间
     *
     * @param date 日期
     */
    public static Date getDayBegin(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获取天的结束时间
     *
     * @param date 日期
     */
    public static Date getDayEnd(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(getDayBegin(date));
        cal.roll(Calendar.HOUR_OF_DAY, -1);
        cal.roll(Calendar.MINUTE, -1);
        cal.roll(Calendar.SECOND, -1);
        return cal.getTime();
    }

    /**
     * 获取UTC时间
     *
     * @param date 当前时区的时间
     * @return 转换为UTC的时间
     */
    public static Date getUTCTime(Date date) {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTime(date);
        // 取得时间偏移量
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 取得夏令时差
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return cal.getTime();
    }

}
