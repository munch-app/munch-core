package com.munch.utils.spark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.munch.utils.spark.exceptions.ExpectedError;
import com.munch.utils.spark.exceptions.JsonException;
import com.munch.utils.spark.exceptions.ParamException;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
    protected static final ObjectMapper objectMapper = SparkUtils.objectMapper;

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
     * All exceptions that can be expected and handled is handled
     * However for website, please handle website expected explicitly
     */
    void handleException() {
        logger.info("Adding exception handling for ParamException, JsonException and Exception");
        Spark.exception(ParamException.class, (exception, request, response) -> {
            response.status(400);
            ObjectNode node = objectMapper.createObjectNode();
            node.put("id", "params_required");
            node.put("message", "Required field is available.");
            node.put("detailed", exception.getMessage());
            try {
                response.body(objectMapper.writeValueAsString(node));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        Spark.exception(JsonException.class, (exception, request, response) -> {
            response.status(400);
            ObjectNode node = objectMapper.createObjectNode();
            node.put("id", "json_error");
            node.put("message", "Unable to parse json.");
            node.put("detailed", exception.getMessage());
            try {
                response.body(objectMapper.writeValueAsString(node));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        Spark.exception(ExpectedError.class, (exception, request, response) -> {
            logger.error("Expected Error", exception);

            response.status(((ExpectedError) exception).getStatus());
            ObjectNode node = objectMapper.createObjectNode();
            node.put("id", "expected_error");
            node.put("message", exception.getMessage());
            node.put("detailed", ExceptionUtils.getStackTrace(exception));
            try {
                response.body(objectMapper.writeValueAsString(node));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        Spark.exception(Exception.class, (exception, request, response) -> {
            logger.error("Unknown Error", exception);

            response.status(500);
            ObjectNode node = objectMapper.createObjectNode();
            node.put("id", "unknown_error");
            node.put("message", exception.getMessage());
            node.put("detailed", ExceptionUtils.getStackTrace(exception));
            try {
                response.body(objectMapper.writeValueAsString(node));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
