package fun.billon.common.http.api;

import lombok.Data;

/**
 * 返回给客户端的Response
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class Response<T> {

    /**
     * 返回状态码：成功
     */
    public static final int RESULT_CODE_SUCCESS = 0;

    /**
     * 返回状态码：失败
     */
    public static final int RESULT_CODE_ERROR = -1;

    /**
     * 返回状态码：数据解析异常
     */
    public static final int RESULT_CODE_PARSE_EXCEPTION = -2;

    /**
     * 状态码
     */
    private int code;

    /**
     * 错误信息
     */
    private String msg;

    /**
     * 内容
     */
    private T data;

}
