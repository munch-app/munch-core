package com.instagram.err;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Date: 31/7/18
 * Time: 4:47 PM
 * Project: instagram-system
 *
 * @author Fuxing Loh
 */
public class InstagramException extends TransportException {

    static {
        ExceptionParser.register(InstagramException.class, InstagramException::new);
    }

    InstagramException(TransportException e) {
        super(e);
    }

    public InstagramException(int code, String message) {
        super(code, InstagramException.class, message);
    }

    public InstagramException(Exception e) {
        super(500, InstagramException.class, e.getMessage(), e);
    }

    protected InstagramException(int code, Class<? extends InstagramException> clazz, String message) {
        super(code, clazz, message);
    }
}
