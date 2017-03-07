package com.munch.utils.spark;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.munch.utils.spark.exceptions.StructuredException;
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
    protected static final ObjectMapper objectMapper = JsonService.objectMapper;

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
     * Start Spark Json Server with given routers
     * Expected status code spark server should return is
     * 200: ok, no error in request
     * 400: structured error, constructed error from developer
     * 500: unknown error, all exception
     * 404: not found, endpoint not found
     * <p>
     * body: always json
     * <pre>
     * {
     *     meta: {code: 200},
     *     data: {name: "Explicit data body"},
     *     "other": {name: "Other implicit data body"}
     * }
     * </pre>
     *
     * @param port port to run server with
     */
    public void start(int port) {
        // Setup port
        Spark.port(port);

        // Spark after register json as content type
        Spark.after((req, res) -> res.type(JsonService.APP_JSON));
        logger.info("Registered all response Content-Type as application/json");

        // Setup all routers
        for (SparkRouter router : routers) {
            router.start();
            logger.info("Started SparkRouter: {}", router.getClass().getSimpleName());
        }

        // Default not found meta
        Spark.notFound((req, res) -> "{\"meta\":{\"code\":404,\"errorType\":\"EndpointNotFound\",\"errorMessage\":\"Requested endpoint is not registered.\"}}");
        logger.info("Registered not found json response.");

        // Handle all expected exceptions
        handleException();
        logger.info("Started Spark Server on port: {}", port);
    }

    /**
     * All exceptions that can be expected and handled is handled.
     * Expected status code is
     * 400: structured error
     * 500: unknown error
     */
    void handleException() {
        logger.info("Adding exception handling for StructuredError.");
        Spark.exception(StructuredException.class, (exception, request, response) -> {
            logger.warn("Structured exception thrown", exception);
            StructuredException error = (StructuredException) exception;

            ObjectNode metaNode = objectMapper.createObjectNode();
            metaNode.put("code", error.getCode());
            metaNode.put("errorType", error.getType());
            metaNode.put("errorMessage", error.getMessage());

            try {
                response.status(error.getCode());
                JsonNode node = objectMapper.createObjectNode().set("meta", metaNode);
                response.body(objectMapper.writeValueAsString(node));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

        logger.info("Adding exception handling for all Exception.");
        Spark.exception(Exception.class, (exception, request, response) -> {
            logger.error("Unknown exception thrown", exception);

            ObjectNode metaNode = objectMapper.createObjectNode();
            metaNode.put("code", 500);
            metaNode.put("errorType", "UnknownException");
            metaNode.put("errorMessage", exception.getMessage());

            try {
                response.status(500);
                JsonNode node = objectMapper.createObjectNode().set("meta", metaNode);
                response.body(objectMapper.writeValueAsString(node));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
