package fun.billon.common.constant;

/**
 * 通用状态码(1~99999)
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class CommonStatusCode {

    /**
     * hystrix fallback(9999)
     */
    public static final int HYSTRIX_FALLBACK = 9999;

    /**
     * 通用状态码起始偏移
     */
    private static final int COMMON_STATUS_OFFSET = 10000;

    /**
     * 参数不合法:(10001)
     */
    public static final int PARAM_INVALID = COMMON_STATUS_OFFSET + 1;

    /**
     * ip地址无效:(10002)
     */
    public static final int IP_INVALID = COMMON_STATUS_OFFSET + 2;

    /**
     * authentication无效:(10003)
     */
    public static final int AUTHENTICATION_INVALID = COMMON_STATUS_OFFSET + 3;

    /**
     * sid无效:(10004)
     */
    public static final int SID_INVALID = COMMON_STATUS_OFFSET + 4;

    /**
     * appId无效:(10005)
     */
    public static final int APPID_INVALID = COMMON_STATUS_OFFSET + 5;

    /**
     * token过期:(10006)
     */
    public static final int TOKEN_EXPIRED = COMMON_STATUS_OFFSET + 6;

    /**
     * token无效:(10007)
     */
    public static final int TOKEN_INVALID = COMMON_STATUS_OFFSET + 7;

    /**
     * 签名无效:(10008)
     */
    public static final int SIGNATURE_INVALID = COMMON_STATUS_OFFSET + 8;

}