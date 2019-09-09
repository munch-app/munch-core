package app.munch.elastic.err;

import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.err.TimeoutException;
import dev.fuxing.err.TransportException;
import dev.fuxing.utils.JsonUtils;
import io.searchbox.core.DocumentResult;
import org.apache.commons.lang3.StringUtils;

import java.net.SocketTimeoutException;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 1:28 pm
 */
public class ElasticException extends TransportException {
    static {
        ExceptionParser.register(ElasticException.class, ElasticException::new);
    }

    protected ElasticException(TransportException e) {
        super(e);
    }

    protected ElasticException(int code, Class<? extends TransportException> clazz, String message, String stacktrace) {
        super(code, clazz, message, stacktrace);
    }

    protected ElasticException(int code, Class<? extends TransportException> clazz, String message) {
        super(code, clazz, message);
    }

    private ElasticException(String message, String stacktrace) {
        super(500, ElasticException.class, message, stacktrace);
    }

    private ElasticException(Throwable throwable) {
        super(500, ElasticException.class, "Search indexing error.", throwable);
    }

    private ElasticException(String message) {
        super(500, ElasticException.class, message);
    }

    public static TransportException map(Exception e) {
        if (e instanceof SocketTimeoutException) {
            return new TimeoutException(e);
        }
        return new ElasticException(e);
    }

    public static void parse(DocumentResult result) throws ElasticException {
        if (result.getErrorMessage() == null) return;
        JsonNode json = JsonUtils.jsonToTree(result.getJsonString());

        // Attempt to parse know types
        parseType(json.path("error").path("type").asText());

        // Attempt to parse know result
        parseResult(json.path("result").asText());

        // If can't parse into known types: throw a generic error.
        throw new ElasticException("Search indexing error.", JsonUtils.toString(json.path("error")));
    }

    private static void parseType(String type) {
        if (StringUtils.isBlank(type)) return;

        if (StringUtils.equals(type, "cluster_block_exception")) {
            throw new ClusterException("cluster_block_exception");
        }
    }

    private static void parseResult(String result) {
        if (StringUtils.isBlank(result)) return;

        if (StringUtils.equals(result, "not_found")) {
            throw new NotFoundException();
        }
    }
}
