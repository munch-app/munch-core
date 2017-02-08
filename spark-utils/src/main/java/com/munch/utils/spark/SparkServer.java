package com.munch.utils.spark;

import com.munch.utils.spark.exceptions.ExpectedError;
import com.munch.utils.spark.exceptions.JsonException;
import com.munch.utils.spark.exceptions.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 9/12/2016
 * Time: 6:47 PM
 * Project: corpus-catalyst
 */
public class SparkServer {
    protected static final Logger logger = LoggerFactory.getLogger(SparkServer.class);

    private final SparkRouter[] routers;

    /**
     * @param routers array of routes for spark server to route with
     */
    public SparkServer(SparkRouter... routers) {
        this.routers = routers;
    }

    /**
     * @param routers set of routes for spark server to route with
     */
    public SparkServer(Set<SparkRouter> routers) {
        this.routers = routers.toArray(new SparkRouter[routers.size()]);
    }

    /**
     * Start Spark Server with given routers
     */
    public void start(int port) {
        // Setup port
        Spark.port(port);

        // Setup all routers
        for (SparkRouter controller : routers) {
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
