package fun.billon.common.util;

import java.security.MessageDigest;

/**
 * 为String生成MD5码
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class MD5 {

    /**
     * 生成MD5加密后的字串
     *
     * @param str 要加密的字串
     * @return MD5加密后的字串
     */
    public static String encode(String str) {
        MessageDigest messageDigest;
        StringBuilder sb = new StringBuilder();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));

            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    sb.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return sb.toString();
    }

}
