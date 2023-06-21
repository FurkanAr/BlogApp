package com.demo.Blog.service;

import com.demo.Blog.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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


    public String findUserName(String token) {
        return exportToken(token, Claims::getSubject);
    }

    private <T> T exportToken(String token, Function<Claims, T>  claimFunction){
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
        return claimFunction.apply(claims);
    }

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(key);
    }

    public boolean tokenControl(String jwt, UserDetails userDetails) {
        final String userName = findUserName(jwt);
        return (userName.equals(userDetails.getUsername()) && !exportToken(jwt, Claims::getExpiration).before(new Date()));
    }

    public String generateToken(User user) {
        return Jwts.builder()
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
    }
}
