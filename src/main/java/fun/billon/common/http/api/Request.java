package fun.billon.common.http.api;

import lombok.Data;

/**
 * Http请求
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class Request {

    /**
     * 请求方式:HTTP_GET
     */
    public static final String HTTP_GET = "HTTP_GET";
    /**
     * 请求方式:HTTP_POST
     */
    public static final String HTTP_POST = "HTTP_POST";

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求参数
     */
    private RequestParam params;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 请求信息构造器
     *
     * @param url           请求url
     * @param requestMethod 请求方式
     * @param params        参数
     */
    public Request(String url, String requestMethod, RequestParam params) {
        super();
        this.url = url;
        this.requestMethod = requestMethod;
        this.params = params;
    }

}
