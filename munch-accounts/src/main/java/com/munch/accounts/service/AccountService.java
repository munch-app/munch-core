package com.munch.accounts.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.munch.accounts.spark.SparkServer;
import spark.*;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 8:12 AM
 * Project: munch-core
 */
public class AccountService extends SparkServer.Controller implements Service {

    @Override
    public void route() {
        Spark.before("/api/v1/account", validateToken());
        Spark.before("/api/v1/account/*", validateToken());

        Spark.post("/api/v1/account", APP_JSON, this::update, toJson);
        Spark.get("/api/v1/account", this::get, toJson);

        Spark.post("/api/v1/account/profile/image", "multipart/form-data", this::uploadProfileImage, toJson);
    }

    // TODO all 3 services

    private JsonNode get(Request request, Response response) {
        return null;
    }

    private JsonNode update(Request request, Response response) {
        return null;
    }

    private JsonNode uploadProfileImage(Request request, Response response) throws IOException, ServletException {
        request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
        try (InputStream is = request.raw().getPart("profileImage").getInputStream()) {

        }
        return null;
    }
}
