package com.hakime.sparkjava;

import com.hakime.sparkjava.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

import java.security.PrivateKey;
import java.security.PublicKey;


public final class TokenService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;



    public TokenService(PrivateKey privateKey, PublicKey publicKey) {
        this.privateKey=privateKey;
        this.publicKey = publicKey;
    }


    public  String newToken(User user) {
        DefaultClaims claims = new DefaultClaims();
        claims.setSubject(user.getUsername());
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }


    public  boolean validateToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(publicKey)
                .parseClaimsJws(token)
                .getBody();
        String user = claims.getSubject();
        if (!"admin".equals(user)){
            return false;
        }
        return true;
    }




}
