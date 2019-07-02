package fun.billon.common.encrypt;

import java.security.MessageDigest;

/**
 * MD5加密工具
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class MD5 {

    /**
     * 加密算法(MD5)
     */
    private static final String ENCRYPT_ALGORITHM_MD5 = "MD5";

    /**
     * 字符集
     */
    private static final String CHARSET = "UTF-8";

    /**
     * MD5加密
     *
     * @param source 要加密的字串
     * @return MD5加密后的字串
     */
    public static String encode(String source) {
        StringBuilder sb = new StringBuilder();
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(ENCRYPT_ALGORITHM_MD5);
            md5.update(source.getBytes(CHARSET));
            for (byte b : md5.digest()) {
                // 10进制转16进制，X 表示以十六进制形式输出，02 表示不足两位前面补0输出
                sb.append(String.format("%02X", b));
            }
            return sb.toString().toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}