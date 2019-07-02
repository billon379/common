package fun.billon.common.util;

import fun.billon.common.exception.ParamException;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * StringUtils测试类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class StringUtilsTest {

    @Test
    public void testCheckParam() {
        Map<String, String> map = new HashMap<>();
        map.put("time", "2017-11-29 17:32:50");
        map.put("uid", "123");
        try {
            StringUtils.checkParam(map, new String[]{"time", "uid"}, new boolean[]{true, true}, new Class[]{Date.class, Integer.class}, new String[]{"yyyy-MM-dd HH:mm:ss"});
        } catch (ParamException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testIpFilter() {
        String regex = "192.168.*.2,191.168.*.3";
        String ip = "192.168.0.1";
        System.out.println(StringUtils.matches(regex, ip));
    }

    @Test
    public void testParseUrl() {
        String restUrl = "member/id/{id}";
        Map<String, String> param = new HashMap<>();
        param.put("name", "zhangsan");
        String url = StringUtils.parseRestUrl(restUrl, param, 123);
        System.out.println(url);
    }

    @Test
    public void testRegexGropu() {
        String PATTERN_CACHE_KEY = "([#$]\\{[^\\}]*\\})*";
        Pattern PATTERN_CACHE_KEY_GROUP = Pattern.compile("[#$]\\{[^\\}]*\\}");
        String s = "#{model.sid}#{model.name}";
        System.out.println(s.matches(PATTERN_CACHE_KEY));
        Matcher matcher = PATTERN_CACHE_KEY_GROUP.matcher(s);
        while (matcher.find()) {
            String s1 = matcher.group();
            System.out.println(s1);
            System.out.println(s1.substring(2, s1.length() - 1));
        }
    }

    @Test
    public void testGetRedisKey() {
        String namespace = "namespace";
        String field1 = "#{model.field1}";
        String field2 = "${model.field2}";
        String field3 = "#{field3}";
        String field4 = "${field4}";
        String field5 = "${field5}${field6}";
        String fieldValue = "fieldValue";
        String fieldValue1 = "fieldValue1";
        System.out.println(RedisKeyUtils.get(namespace, field1, fieldValue));
        System.out.println(RedisKeyUtils.get(namespace, field2, fieldValue));
        System.out.println(RedisKeyUtils.get(namespace, field3, fieldValue));
        System.out.println(RedisKeyUtils.get(namespace, field4, fieldValue));
        System.out.println(RedisKeyUtils.get(namespace, field5, fieldValue, fieldValue1));
    }
}