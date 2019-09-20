package app.munch.err;

import dev.fuxing.err.ExceptionParser;
import dev.fuxing.err.TransportException;

/**
 * Created by: Fuxing
 * Date: 20/9/19
 * Time: 11:12 pm
 */
public final class AuthenticateException extends TransportException {

    static {
        ExceptionParser.register(AuthenticateException.class, AuthenticateException::new);
    }

    private AuthenticateException(TransportException e) {
        super(e);
    }

    public AuthenticateException() {
        super(400, AuthenticateException.class, "Could not setup account, missing information.");
    }
}
