package com.hakime.sparkjava;

import spark.Filter;
import spark.Request;
import spark.Response;

import java.util.logging.Logger;

import static spark.Spark.halt;


public class AuthFilter implements Filter {

    private static final Logger LOG = Logger.getLogger(AuthFilter.class.getName());

    private static final String TOKEN_PREFIX = "Bearer";
    private static final String LOGIN_ENDPOINT = "/login";
    private static final String HTTP_POST = "POST";

    private final String authEndpointPrefix;

    private TokenService tokenService;

    public AuthFilter(String authEndpointPrefix, TokenService tokenService) {
        this.authEndpointPrefix = authEndpointPrefix;
        this.tokenService = tokenService;
    }

    public void handle(Request request, Response response) {
        if (!isLoginRequest(request) && isAdminRequest(request)) {
            String authorizationHeader = request.headers("Authorization");
            if (authorizationHeader == null) {
                LOG.warning("Missing Authorization header");
                halt(401);
            }
            else if (!tokenService.validateToken(authorizationHeader.replace(TOKEN_PREFIX, ""))) {
                LOG.warning("Invalid token " + authorizationHeader);
                halt(401);
            }
        }
    }

    private boolean isLoginRequest(Request request) {
        return request.uri().equals(authEndpointPrefix + LOGIN_ENDPOINT) && request.requestMethod().equals(HTTP_POST);
    }

    private boolean isAdminRequest(Request request) {
        return request.uri().equals("/admin/book-arrived") && request.requestMethod().equals(HTTP_POST);
    }
}
