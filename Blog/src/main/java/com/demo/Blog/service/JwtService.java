package com.demo.Blog.service;

import com.demo.Blog.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${security.jwt.secret}")
    private String SECRET_KEY;

    @Value("${security.token.expires.in}")
    private long EXPIRES_IN;

    Logger logger = LoggerFactory.getLogger(getClass());

    public String findUserName(String token) {
        logger.info("findUserName method started");
        String userName = exportToken(token, Claims::getSubject);
        logger.info("User found with token: {}", userName);
        logger.info("findUserName method successfully worked");
        return userName;
    }

    private <T> T exportToken(String token, Function<Claims, T>  claimFunction){
        logger.info("exportToken method started");
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
        logger.info("exportToken method successfully worked");
        return claimFunction.apply(claims);
    }

    private Key getKey() {
        logger.info("getKey method started");
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        logger.info("getKey method successfully worked");
        return Keys.hmacShaKeyFor(key);
    }

    public boolean tokenControl(String jwt, UserDetails userDetails) {
        logger.info("tokenControl method started");
        final String userName = findUserName(jwt);
        boolean isSame = (userName.equals(userDetails.getUsername()) && !exportToken(jwt, Claims::getExpiration).before(new Date()));
        logger.info("Username is same: {}", isSame);
        logger.info("tokenControl method successfully worked");
        return isSame;
    }

    public String generateToken(User user) {
        logger.info("generateToken method started");
        String token = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(user.getUserName())
                .setIssuer("Blog.app")
                .claim("roles", user.getRole().toString())
                .claim("userId" , user.getId().toString())
                .claim("email" , user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() +EXPIRES_IN))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
        logger.info("generateToken method successfully worked");
        return token;
    }
}
