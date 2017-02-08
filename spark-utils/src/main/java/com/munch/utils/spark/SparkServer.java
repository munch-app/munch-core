package com.munch.utils.spark;

import com.munch.utils.spark.exceptions.ExpectedError;
import com.munch.utils.spark.exceptions.JsonException;
import com.munch.utils.spark.exceptions.ParamException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

/**
 * Created by: Fuxing
 * Date: 9/12/2016
 * Time: 6:47 PM
 * Project: corpus-catalyst
 */
public class SparkServer {
    protected static final Logger logger = LoggerFactory.getLogger(SparkServer.class);

    private final SparkRouter[] controllers;

    /**
     * @param controllers controllers for spark to route
     */
    public SparkServer(SparkRouter... controllers) {
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
        for (SparkRouter controller : controllers) {
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
}
