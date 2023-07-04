package com.demo.bloggateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.function.Function;

@Component
public class JwtUtil {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final String SECRET_KEY = "9ln6jZ1h5BuP28k5RmlOaeL5risy7xe9czd4yA8pZdfGA36zbmRt";

    public void validateToken(final String token) {
        logger.info("validateToken method started");
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
        logger.info("validateToken method successfully worked");
    }

    private Key getSignKey() {
        logger.info("getSignKey method started");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        logger.info("getSignKey method successfully worked");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
