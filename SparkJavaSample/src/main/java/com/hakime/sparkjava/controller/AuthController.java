package com.hakime.sparkjava.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hakime.sparkjava.AuthFilter;
import com.hakime.sparkjava.TokenService;
import com.hakime.sparkjava.user.User;
import com.hakime.sparkjava.user.UserService;
import org.mindrot.jbcrypt.BCrypt;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class AuthController  {

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String USER_NAME_PROPERTY = "userName";
    private static final String PASSWORD_PROPERTY = "password";
    private static final String AUTH_ENDPOINT_PREFIX = "/auth";

    private static final String BCRYPT_SALT = BCrypt.gensalt();

    private final Gson gson;
    private final UserService userService;
    private final TokenService tokenService;

    public AuthController(Gson gson, UserService userService, TokenService tokenService) {
        this.gson = gson;
        this.userService = userService;
        this.tokenService = tokenService;
    }

    public void init() {
        createAdminUser();

        // AUTH FILTER
        before(new AuthFilter("/auth", tokenService));

        // LOGIN ENDPOINT
        post(AUTH_ENDPOINT_PREFIX + "/login", (request, response) -> login(request, response));

    }


    private String login(Request request, Response response) throws IOException {
        String json = request.raw().getReader().lines().collect(Collectors.joining());
        JsonObject jsonRequest = gson.fromJson(json, JsonObject.class);
        if (validatePost(jsonRequest)) {
            try {
                String encryptedPassword = BCrypt.hashpw(jsonRequest.get(PASSWORD_PROPERTY).getAsString(), BCRYPT_SALT);
                User user = userService.get(jsonRequest.get(USER_NAME_PROPERTY).getAsString());
                if (user.getPassword().equals(encryptedPassword)) {
                    response.header(AUTHORIZATION_HEADER, TOKEN_PREFIX + " " + tokenService.newToken(user));
                }
            } catch (Exception e) {
                response.status(401);
            }
        }
        return "";
    }



    private void createAdminUser() {
        userService.register("admin", BCrypt.hashpw("admin", BCRYPT_SALT), null, null); //ADMIN USER
    }

    private boolean validatePost(JsonObject jsonRequest) {
        return jsonRequest != null && jsonRequest.has(USER_NAME_PROPERTY) && jsonRequest.has(PASSWORD_PROPERTY);
    }

}
