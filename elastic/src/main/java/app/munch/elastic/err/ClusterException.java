package app.munch.elastic.err;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Created by: Fuxing
 * Date: 12/12/2017
 * Time: 9:42 AM
 * Project: munch-data
 */
public final class ClusterException extends ElasticException {
    private static final String MORE_INFO = "http://docs.aws.amazon.com/elasticsearch-service/latest/developerguide/aes-handling-errors.html#aes-handling-errors-watermark";

    static {
        ExceptionParser.register(ClusterException.class, ClusterException::new);
    }

    public ClusterException(TransportException e) {
        super(e);
    }

    public ClusterException(String message) {
        super(503, ClusterException.class, message, MORE_INFO);
    }
}
