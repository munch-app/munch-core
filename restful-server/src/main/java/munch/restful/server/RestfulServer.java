package munch.restful.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import munch.restful.core.RestfulMeta;
import munch.restful.core.exception.StructuredException;
import munch.restful.core.exception.UnknownException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Response;
import spark.Spark;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 9/12/2016
 * Time: 6:47 PM
 * Project: corpus-catalyst
 */
public class RestfulServer {
    protected static final Logger logger = LoggerFactory.getLogger(RestfulServer.class);
    protected static final ObjectMapper objectMapper = JsonService.objectMapper;

    private static final JsonNode notFound = objectMapper.createObjectNode()
            .set("meta", objectMapper.valueToTree(RestfulMeta.builder()
                    .code(404)
                    .errorType("EndpointNotFound")
                    .errorMessage("Requested endpoint is not registered.")
                    .build()));

    private final RestfulService[] routers;
    private boolean started = false;

    /**
     * @param routers array of routes for spark server to route with
     */
    public RestfulServer(RestfulService... routers) {
        this.routers = routers;
    }

    /**
     * Support for guice injections
     *
     * @param routers set of routes for spark server to route with
     */
    public RestfulServer(Set<RestfulService> routers) {
        this(routers.toArray(new RestfulService[routers.size()]));
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

        // Spark after register all path content type as json
        Spark.after((req, res) -> res.type(JsonService.APP_JSON));
        logger.info("Registered all response Content-Type as application/json");

        // Setup all routers
        for (RestfulService router : routers) {
            router.start();
            logger.info("Started SparkRouter: {}", router.getClass().getSimpleName());
        }

        // Default handler for not found
        Spark.notFound((req, res) -> JsonTransformer.toJson(notFound));
        logger.info("Registered http 404 not found json response.");

        // Handle all expected exceptions
        handleException();
        logger.info("Started Spark Server on port: {}", port);
        this.started = true;
    }

    /**
     * @see StructuredException to see values and creating custom structured exception
     * @see UnknownException to see how unknown exception values is mapped
     * @see RestfulMeta to see how it is formatted
     */
    protected void handleException() {
        logger.info("Adding exception handling for StructuredError.");
        Spark.exception(StructuredException.class, (exception, request, response) -> {
            logger.warn("Structured exception thrown", exception);
            handleException(response, (StructuredException) exception);
        });

        logger.info("Adding exception handling for all Exception.");
        Spark.exception(Exception.class, (exception, request, response) -> {
            logger.warn("Structured exception thrown", exception);
            StructuredException structured = mapException(exception);
            if (structured != null) {
                // Mapped exception
                handleException(response, structured);
            } else {
                // Unknown exception
                handleException(response, new UnknownException(exception));
            }
        });
    }

    /**
     * @param exception additional exception to map
     * @return Structured exception, null = unknown
     */
    @Nullable
    protected StructuredException mapException(Exception exception) {
        return null;
    }

    /**
     * @param response     response to write to
     * @param code         int status code
     * @param errorType    error type
     * @param errorMessage error message
     */
    protected void handleException(Response response, StructuredException exception) {
        try {
            response.status(exception.getCode());
            JsonNode nodes = objectMapper.createObjectNode().set(
                    "meta", objectMapper.valueToTree(exception.toMeta()));
            response.body(objectMapper.writeValueAsString(nodes));
            response.type(JsonService.APP_JSON);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return true if restful server has started
     */
    public boolean isStarted() {
        return started;
    }

    /**
     * @return port
     * @throws IllegalStateException when the server is not started
     */
    public int getPort() {
        return Spark.port();
    }
}
