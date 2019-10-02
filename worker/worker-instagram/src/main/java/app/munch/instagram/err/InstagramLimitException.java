package app.munch.instagram.err;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Date: 2/10/19
 * Time: 9:59 am
 *
 * @author Fuxing Loh
 */
public final class InstagramLimitException extends InstagramException {

    static {
        ExceptionParser.register(InstagramLimitException.class, InstagramLimitException::new);
    }

    InstagramLimitException(TransportException e) {
        super(e);
    }

    public InstagramLimitException(String message) {
        super(429, InstagramLimitException.class, message);
    }
}
