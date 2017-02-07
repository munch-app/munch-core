package com.munch.accounts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.munch.accounts.spark.SparkServer;
import spark.Request;
import spark.Response;
import spark.Spark;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 8:12 AM
 * Project: munch-core
 */
public class SessionService extends SparkServer.Controller implements Service {

    // TODO services for partners and main app to use

    @Override
    public void route() {
        Spark.put("/api/v1/create", APP_JSON, this::create);
        Spark.post("/api/v1/login/email", APP_JSON, this::loginEmail);

        // Auto create account
        Spark.post("/api/v1/login/facebook", APP_JSON, this::loginFacebook);

        Spark.post("/api/v1/logout", APP_JSON, this::logout);
    }

    private JsonNode create(Request request, Response response) {
        // TODO create account
        return null;
    }

    private JsonNode loginEmail(Request request, Response response) {
        String email;
        String password;
        return null;
        // TODO email login
    }

    /**
     * @param request  spark req
     * @param response spark res
     * @return JsonNode {token: ""}
     */
    private JsonNode loginFacebook(Request request, Response response) {
        String facebookToken;
        return null;
        // TODO once mobile api is work in progress
    }

    // Login Instagram should not be required as it is partner only, WEB login

    private JsonNode logout(Request request, Response response) {
        String token;
        // TODO revoke token
        return null;
    }

}
