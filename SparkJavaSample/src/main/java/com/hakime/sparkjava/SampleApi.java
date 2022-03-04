package com.hakime.sparkjava;

import com.google.gson.GsonBuilder;
import com.hakime.sparkjava.controller.AuthController;
import com.hakime.sparkjava.core.Service;
import com.hakime.sparkjava.user.UserService;


public class SampleApi {
    private static final String SECRET_JWT = "secret_jwt";

    private static final TokenService tokenService = new TokenService(SECRET_JWT);



    public static void main(String[] args)  {

        new Service().init();
        new AuthController(new GsonBuilder().create(), new UserService(), tokenService).init();




    }
}
