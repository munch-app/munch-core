package munch.restful.client.exception;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 5:19 PM
 * Project: munch-core
 */
public class TimeoutException extends RestfulException {

    public TimeoutException(String message) {
        super(message);
    }

    public TimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeoutException(Throwable cause) {
        super(cause);
    }

}
