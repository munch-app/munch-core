package com.munch.accounts.spark;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.munch.accounts.PacConfigFactory;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.ResponseTransformer;
import spark.Spark;
import spark.TemplateEngine;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 9/12/2016
 * Time: 6:47 PM
 * Project: corpus-catalyst
 */
public class SparkServer {
    protected static final Logger logger = LoggerFactory.getLogger(SparkServer.class);

    private final Controller[] controllers;

    /**
     * @param controllers controllers for spark to route
     */
    public SparkServer(Controller... controllers) {
        this.controllers = controllers;
    }

    /**
     * Start Spark The Server
     */
    public void start() {
        Config config = ConfigFactory.load().getConfig("munch.accounts");
        // Setup port
        Spark.port(config.getInt("http.port"));

        // Setup all controllers
        for (Controller controller : controllers) {
            controller.start();
        }

        // Handle all expected exceptions
        handleException();
    }

    /**
     * All exceptions that can be handled
     */
    void handleException() {
        Spark.exception(ParamException.class, (exception, request, response) -> {
            response.status(400);
            response.body(exception.getMessage());
        });

        Spark.exception(JsonException.class, (exception, request, response) -> {
            response.status(400);
            response.body(exception.getMessage());
        });

        Spark.exception(ExpectedError.class, (exception, request, response) -> {
            response.status(((ExpectedError) exception).getStatus());
            response.body(exception.getMessage());
        });
    }


    /**
     * Created by: Fuxing
     * Date: 11/12/2016
     * Time: 5:11 AM
     * Project: corpus-catalyst
     */
    public abstract static class Controller {
        public static final String APP_JSON = "application/json";

        protected static final TemplateEngine templateEngine = new HandlebarsTemplateEngine();
        protected static final org.pac4j.core.config.Config pacConfig = new PacConfigFactory(templateEngine).build();
        protected static final ObjectMapper objectMapper = new ObjectMapper();
        protected static final ResponseTransformer toJson = objectMapper::writeValueAsString;

        /**
         * Start the controller
         */
        public void start() {
            route();
        }

        /**
         * Wire all the routes there
         */
        public abstract void route();

        /**
         * throw a expected error
         *
         * @param message message to put to response body
         * @param status  status code to response
         */
        protected void throwMessage(String message, int status) {
            throw new ExpectedError(message, status);
        }

        protected JsonNode readNode(Request request) throws JsonException {
            try {
                return objectMapper.readTree(request.bodyAsBytes());
            } catch (IOException e) {
                throw new JsonException(e);
            }
        }

        protected <T> T readJson(Request request, Class<T> clazz) throws JsonException {
            try {
                return objectMapper.readValue(request.bodyAsBytes(), clazz);
            } catch (IOException e) {
                throw new JsonException(e);
            }
        }

        protected long queryLong(Request request, String name) throws ParamException {
            String value = request.queryParams(name);
            if (StringUtils.isNotBlank(value)) {
                try {
                    return Long.parseLong(value);
                } catch (NumberFormatException e) {
                    throw new ParamException(name, e.getMessage());
                }
            }
            throw new ParamException(name, "param is blank");
        }

        protected int queryInt(Request request, String name) throws ParamException {
            String value = request.queryParams(name);
            if (StringUtils.isNotBlank(value)) {
                try {
                    return Integer.parseInt(value);
                } catch (NumberFormatException e) {
                    throw new ParamException(name, e.getMessage());
                }
            }
            throw new ParamException(name, "param is blank");
        }

        /**
         * string.equal("true")
         */
        protected boolean queryBool(Request request, String name) throws ParamException {
            String value = request.queryParams(name);
            if (StringUtils.isNotBlank(value)) {
                return Boolean.parseBoolean(value);
            }
            throw new ParamException(name, "param is blank");
        }

        protected String queryString(Request request, String name) throws ParamException {
            String value = request.queryParams(name);
            if (StringUtils.isNotBlank(value)) {
                return value;
            }
            throw new ParamException(name, "param is blank");
        }
    }
}
