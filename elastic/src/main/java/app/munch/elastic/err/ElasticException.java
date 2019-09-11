package app.munch.elastic.err;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.err.TimeoutException;
import dev.fuxing.err.TransportException;
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

    public ElasticException(String message) {
        super(500, ElasticException.class, message);
    }

    public static TransportException map(Exception e) {
        if (e instanceof SocketTimeoutException) {
            return new TimeoutException(e);
        }

        // Handle: ElasticsearchException
        return new ElasticException(e);
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
