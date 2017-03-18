package munch.restful.client.exception;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
public class RestfulException extends RuntimeException {

    public RestfulException(String message) {
        super(message);
    }

    public RestfulException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestfulException(Throwable cause) {
        super(cause);
    }

    protected RestfulException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
