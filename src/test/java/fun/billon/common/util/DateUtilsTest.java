package fun.billon.common.util;

import org.junit.Test;

import java.util.Date;

/**
 * 日期转换测试
 */
public class DateUtilsTest {

    @Test
    public void test() {
        String dateBeginStr = "2018-01-01 15:16:23";
        String dateEndStr = "2019-02-15 15:16:23";
        Date dateBegin = DateUtils.parse(dateBeginStr, DateUtils.PATTERN_DATETIME);
        Date dateEnd = DateUtils.parse(dateEndStr, DateUtils.PATTERN_DATETIME);
        System.out.println(dateBegin);
        System.out.println("day begin: " + DateUtils.getDayBegin(dateBegin));
        System.out.println("day end: " + DateUtils.getDayEnd(dateBegin));
        System.out.println("month begin: " + DateUtils.getMonthBegin(dateBegin));
        System.out.println("month end: " + DateUtils.getMonthEnd(dateBegin));
        System.out.println("months between: " + DateUtils.getMonthsBetween(dateBegin, dateEnd));
    }

}