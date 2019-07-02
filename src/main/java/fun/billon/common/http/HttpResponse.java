package fun.billon.common.http;

import lombok.Data;

/**
 * Http请求的返回结果
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
@Data
public class HttpResponse {

    /**
     * 失败
     */
    public static final int RESULT_CODE_FAILED = -1;

    /**
     * 成功
     */
    public static final int RESULT_CODE_SUCCESS = 0;

    /**
     * 接口执行结果
     */
    private int resultCode;

    /**
     * 错误提示信息
     */
    private String errorMsg;

    /**
     * 返回的数据
     */
    private String content;

}