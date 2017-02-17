package com.munch.utils.spark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.munch.utils.spark.exceptions.ExpectedError;
import com.munch.utils.spark.exceptions.JsonException;
import com.munch.utils.spark.exceptions.ParamException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;
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
        for (SparkRouter router : routers) {
            router.start();
            logger.info("Started SparkRouter: {}", router.getClass().getSimpleName());
        }

        // Handle all expected exceptions
        handleException();
        logger.info("Started Spark Server on port: {}", port);
    }

    private void responseError(Response response, String id, String message, String detailed) {
        ObjectNode errorNode = objectMapper.createObjectNode();
        errorNode.put("id", id);
        errorNode.put("message", message);
        errorNode.put("detailed", detailed);
        try {
            JsonNode node = objectMapper.createObjectNode().set("error", errorNode);
            response.body(objectMapper.writeValueAsString(node));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * All exceptions that can be expected and handled is handled
     * However for website, please handle website expected explicitly
     */
    void handleException() {
        logger.info("Adding exception handling for ParamException, JsonException and Exception");
        Spark.exception(ParamException.class, (exception, request, response) -> {
            response.status(400);
            responseError(response, "params_required", "Required field is available.", exception.getMessage());
        });

        Spark.exception(JsonException.class, (exception, request, response) -> {
            response.status(400);
            responseError(response, "json_error", "Unable to parse json.", exception.getMessage());
        });

        Spark.exception(ExpectedError.class, (exception, request, response) -> {
            logger.error("Expected Error", exception);
            response.status(((ExpectedError) exception).getStatus());
            responseError(response, "expected_error", exception.getMessage(), null);
        });

        Spark.exception(Exception.class, (exception, request, response) -> {
            logger.error("Unknown Error", exception);
            response.status(500);
            responseError(response, "unknown_error", exception.getMessage(), null);
        });
    }
}
