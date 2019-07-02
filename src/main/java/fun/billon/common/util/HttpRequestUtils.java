package fun.billon.common.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpRequest工具类
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class HttpRequestUtils {

    private static final String IP_UNKOWN = "unknown";

    /**
     * 获取所有请求头
     *
     * @param request HttpServletRequest
     * @return 所有请求头
     */
    public static Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> map = new HashMap<>(16);
        while (headerNames.hasMoreElements()) {
            String key = headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取所有请求参数
     *
     * @param request HttpServletRequest
     * @return 所有请求参数
     */
    public static Map<String, String> getRequestParams(HttpServletRequest request) {
        Enumeration<String> paramNames = request.getParameterNames();
        Map<String, String> map = new HashMap<>(16);
        while (paramNames.hasMoreElements()) {
            String key = paramNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取用户真实IP地址。
     * 1.不使用request.getRemoteAddr()的原因是有可能用户使用了代理软件方式避免真实IP地址。
     * 2.如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120
     * 用户真实IP为： 192.168.1.110
     *
     * @param request HttpServletRequest
     * @return 获取用户真实IP地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isEmpty(ip) || IP_UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || IP_UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || IP_UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || IP_UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || IP_UNKOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}