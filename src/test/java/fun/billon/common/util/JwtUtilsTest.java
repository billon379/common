package fun.billon.common.util;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * jwt工具类测试
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class JwtUtilsTest {

    /**
     * 签名密钥
     */
    private static final String SECRET = "379387278";

    /**
     * 主题(sub)
     */
    private static final String SUBJECT = "100";

    /**
     * 签发者(iss)
     */
    private static final String ISSUER = "467e0ee86b2810082f31dd75296e3ec5";

    /**
     * 过期时间
     */
    private static final int EXPIRE_TIME = 100;

    /**
     * 过期时间
     */
    private static final int REFRESH_TOKEN_EXPIRE_TIME = 200;

    @Test
    public void testSign() {
        Map<String, String> tokenExtras = new HashMap<>(2);
        tokenExtras.put("key1", "value1");
        tokenExtras.put("key2", "value2");
        String token = JwtUtils.sign(SECRET, SUBJECT, ISSUER, EXPIRE_TIME, tokenExtras);
        System.out.println(token);
        DecodedJWT decodedJWT = JwtUtils.verify(ISSUER, SECRET, REFRESH_TOKEN_EXPIRE_TIME, token);
        System.out.println("sub ---> " + decodedJWT.getSubject());
        System.out.println("ext ---> " + decodedJWT.getClaim("ext").asString());
    }

}