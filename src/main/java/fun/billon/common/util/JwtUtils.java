package fun.billon.common.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * jwt工具类型，用于签名的生成和验证
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class JwtUtils {

    /**
     * 签发token。使用JWT签发token。包含如下字段：
     * alg:HS256(加密方式HS256)
     * sub:#{uid}(主题,保存用户uid)
     * iss:#{appId}(签发者，对应应用名称)
     * iat:#{date}(签发时间)
     * exp:#{date}(过期时间)
     * ext:#{extras}(附加信息,额外添加的token信息)
     *
     * @param secret     密钥
     * @param subject    主题
     * @param issuer     签发者
     * @param expireTime 过期时间，单位毫秒(s)。签发时间+expireTime为签名过期时间
     * @param extras     附加信息
     * @return 生成的签名
     */
    public static String sign(String secret, String subject, String issuer, int expireTime, Map<String, String> extras) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        Calendar calExp = Calendar.getInstance();
        calExp.add(Calendar.SECOND, expireTime);
        return JWT.create()
                .withSubject(subject)
                .withIssuer(issuer)
                .withIssuedAt(new Date())
                .withExpiresAt(calExp.getTime())
                .withClaim("ext", JSON.toJSONString(extras))
                .sign(algorithm);
    }

    /**
     * token验证。使用JWT进行token验证。
     * 1.如果token没有超过refreshTokenExpTime则都可以验证通过
     * 2.验证不通过会抛出异常，需要在异常中做处理
     *
     * @param issuer              签发者，对应签发token的appId
     * @param secret              签发token时使用的密钥
     * @param refreshTokenExpTime 刷新token的过期时间
     * @param token               token
     * @return token验证结果，不抛异常说明验证通过
     */
    public static DecodedJWT verify(String issuer, String secret, int refreshTokenExpTime, String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .acceptLeeway(refreshTokenExpTime)
                .build();
        return verifier.verify(token);
    }

}