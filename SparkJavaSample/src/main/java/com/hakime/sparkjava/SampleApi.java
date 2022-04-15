package com.hakime.sparkjava;

import com.google.gson.GsonBuilder;
import com.hakime.sparkjava.controller.AuthController;
import com.hakime.sparkjava.core.Service;
import com.hakime.sparkjava.user.UserService;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;


public class SampleApi {
    static KeyPairGenerator keyGenerator;

    static {
        try {
            keyGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    static KeyPair kp = keyGenerator.genKeyPair();
    static PublicKey publicKey = (PublicKey) kp.getPublic();
    static PrivateKey privateKey = (PrivateKey) kp.getPrivate();


    private static final TokenService tokenService = new TokenService(privateKey,publicKey);

    public SampleApi() throws NoSuchAlgorithmException {
    }


    public static void main(String[] args)  {

        new Service().init();
        new AuthController(new GsonBuilder().create(), new UserService(), tokenService).init();




    }
}
