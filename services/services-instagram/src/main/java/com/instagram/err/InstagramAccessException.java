package com.instagram.err;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Date: 2/10/19
 * Time: 10:06 am
 *
 * @author Fuxing Loh
 */
public final class InstagramAccessException extends InstagramException {
    static {
        ExceptionParser.register(InstagramLimitException.class, InstagramLimitException::new);
    }

    InstagramAccessException(TransportException e) {
        super(e);
    }

    public InstagramAccessException(String message) {
        super(403, InstagramAccessException.class, message);
    }
}
