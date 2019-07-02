package fun.billon.common.encrypt;

import java.security.MessageDigest;

/**
 * SHA加密工具
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class SHA {

    /**
     * 加密算法(SHA)
     */
    private static final String ENCRYPT_ALGORITHM_SHA = "SHA";

    /**
     * 字符集
     */
    private static final String CHARSET = "UTF-8";

    /**
     * SHA加密
     *
     * @param source 要加密的字串
     * @return SHA加密后的字串
     */
    public static String encode(String source) {
        StringBuilder sb = new StringBuilder();
        MessageDigest sha;
        try {
            sha = MessageDigest.getInstance(ENCRYPT_ALGORITHM_SHA);
            sha.update(source.getBytes(CHARSET));
            for (byte b : sha.digest()) {
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