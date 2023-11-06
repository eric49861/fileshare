package com.eric.fileshare;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.Test;

import java.util.Calendar;

public class JWTTest
{
    private Algorithm algorithm = Algorithm.HMAC256("ERIC");
    private String tokenString;

    @Test
    public void testGenerateJWT() {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, 1);

        tokenString = JWT.create()
                .withClaim("userID", 0)
                .withExpiresAt(instance.getTime())
                .sign(algorithm);
        System.out.println("tokenString = " + tokenString);
    }

    @Test
    public void testParseToken() {
        JWTVerifier verifier = JWT.require(algorithm)
                .build();
        try {
            DecodedJWT verify = verifier.verify(tokenString);
            Integer userID = verify.getClaim("userID").as(Integer.class);
            System.out.println("userID = " + userID);
        }catch(TokenExpiredException e) {
            e.printStackTrace();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
