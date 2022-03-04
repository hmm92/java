package com.hakime.sparkjava;

import com.hakime.sparkjava.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;


public final class TokenService {

    private final String jwtSecretKey;


    public TokenService(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }


    public  String newToken(User user) {
        DefaultClaims claims = new DefaultClaims();
        claims.setSubject(user.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey)
                .compact();
    }


    public  boolean validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecretKey)
                .parseClaimsJws(token)
                .getBody();
        String user = claims.getSubject();
        if (!"admin".equals(user)){
            return false;
        }
        return true;
    }




}
