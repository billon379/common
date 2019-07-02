package fun.billon.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * jwt测试
 *
 * @author billon
 * @version 1.0.0
 * @since 1.0.0
 */
public class JwtTest {

    @Test
    public void testJwtSign() {
        Algorithm algorithm = Algorithm.HMAC256("379387278".getBytes());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, 60);
        Map<String, Object> map = new HashedMap();
        map.put("header", "header");
        map.put("cty", "contentType");
        String token = JWT.create()
                .withHeader(map)
                .withJWTId("jwtid")
                .withKeyId("keyid")
                .withSubject("subject")
                .withIssuer("issuer")
                .withAudience("audience1", "audience2")
                .withClaim("claim", "claim")
                .withIssuedAt(new Date())
                //.withNotBefore(calendar.getTime())
                .withExpiresAt(calendar.getTime())
                .sign(algorithm);
        System.out.println(token);

        try {
            DecodedJWT jwt = JWT.decode(token);
            System.out.println("header = " + jwt.getHeader());
            System.out.println("payload = " + jwt.getPayload());
            System.out.println("signature = " + jwt.getSignature());
            System.out.println("token = " + jwt.getToken());
            System.out.println("alg = " + jwt.getAlgorithm());
            System.out.println("typ = " + jwt.getType());
            System.out.println("cty = " + jwt.getContentType());
            System.out.println("header.header = " + jwt.getHeaderClaim("header").asString());
            System.out.println("jti = " + jwt.getId());
            System.out.println("kid = " + jwt.getKeyId());
            System.out.println("sub = " + jwt.getSubject());
            System.out.println("issuer = " + jwt.getIssuer());
            System.out.println("aud = " + jwt.getAudience());
            System.out.println("claim.claim = " + jwt.getClaim("claim").asString());
            System.out.println("iat = " + jwt.getIssuedAt());
            System.out.println("nbf = " + jwt.getNotBefore());
            System.out.println("exp = " + jwt.getExpiresAt());
        } catch (JWTDecodeException e) {
        }

        try {
            JWTVerifier verifier = JWT.require(algorithm).acceptLeeway(60).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("header = " + jwt.getHeader());
            System.out.println("payload = " + jwt.getPayload());
            System.out.println("signature = " + jwt.getSignature());
            System.out.println("token = " + jwt.getToken());
            System.out.println("alg = " + jwt.getAlgorithm());
            System.out.println("typ = " + jwt.getType());
            System.out.println("cty = " + jwt.getContentType());
            System.out.println("header.header = " + jwt.getHeaderClaim("header").asString());
            System.out.println("jti = " + jwt.getId());
            System.out.println("kid = " + jwt.getKeyId());
            System.out.println("sub = " + jwt.getSubject());
            System.out.println("issuer = " + jwt.getIssuer());
            System.out.println("aud = " + jwt.getAudience());
            System.out.println("claim.claim = " + jwt.getClaim("claim").asString());
            System.out.println("iat = " + jwt.getIssuedAt());
            System.out.println("nbf = " + jwt.getNotBefore());
            System.out.println("exp = " + jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
        }
    }

    @Test
    public void testJwtVerify() {
        String token = "eyJoZWFkZXIiOiJoZWFkZXIiLCJjdHkiOiJjb250ZW50VHlwZSIsInR5cCI6IkpXVCIsImFsZyI6IkhTMjU2Iiwia2lkIjoia2V5aWQifQ.eyJzdWIiOiJzdWJqZWN0IiwiYXVkIjpbImF1ZGllbmNlMSIsImF1ZGllbmNlMiJdLCJpc3MiOiJpc3N1ZXIiLCJjbGFpbSI6ImNsYWltIiwiZXhwIjoxNTIzODk3NDE4LCJpYXQiOjE1MjM4OTczNTgsImp0aSI6Imp3dGlkIn0.KkQayg-LAJX1UaPbomQ1vEj7bvmEB7ZG3GLTgulO6HA";
        try {
            Algorithm algorithm = Algorithm.HMAC256("379387278".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).acceptLeeway(60).build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("header = " + jwt.getHeader());
            System.out.println("payload = " + jwt.getPayload());
            System.out.println("signature = " + jwt.getSignature());
            System.out.println("token = " + jwt.getToken());
            System.out.println("alg = " + jwt.getAlgorithm());
            System.out.println("typ = " + jwt.getType());
            System.out.println("cty = " + jwt.getContentType());
            System.out.println("header.header = " + jwt.getHeaderClaim("header").asString());
            System.out.println("jti = " + jwt.getId());
            System.out.println("kid = " + jwt.getKeyId());
            System.out.println("sub = " + jwt.getSubject());
            System.out.println("issuer = " + jwt.getIssuer());
            System.out.println("aud = " + jwt.getAudience());
            System.out.println("claim.claim = " + jwt.getClaim("claim").asString());
            System.out.println("iat = " + jwt.getIssuedAt());
            System.out.println("nbf = " + jwt.getNotBefore());
            System.out.println("exp = " + jwt.getExpiresAt());
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        }
    }

}