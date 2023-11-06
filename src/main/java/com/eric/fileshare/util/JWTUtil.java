package com.eric.fileshare.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import com.eric.fileshare.beans.User;

import java.time.Instant;
import java.util.Calendar;


public class JWTUtil {

    private static final String secretKey = "eric";
    private static final Algorithm algorithm = Algorithm.HMAC256(secretKey);

    public static String getToken(Integer userID, String email) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.HOUR, 24);

        String jwtString = JWT.create()
                .withClaim("email", email)
                .withClaim("userID", userID)
                .withClaim("time", System.currentTimeMillis())
                .withExpiresAt(instance.getTime())
                .withAudience("issuer", "fileshare")
                .sign(algorithm);
        return jwtString;
    }

    public static String parseToken(String tokenString) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(algorithm).build();
        String email = null;
        Integer userID = null;
        try {
            DecodedJWT verify = verifier.verify(tokenString);
            email = verify.getClaim("email").as(String.class);
            userID = verify.getClaim("userID").as(Integer.class);
        } catch(JWTVerificationException e) {
            throw e;
        }
        return email;
    }

}
