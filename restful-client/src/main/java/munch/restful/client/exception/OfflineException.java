package munch.restful.client.exception;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
public class OfflineException extends RestfulException {

    public OfflineException(String message) {
        super(message);
    }

    public OfflineException(String message, Throwable cause) {
        super(message, cause);
    }

    public OfflineException(Throwable cause) {
        super(cause);
    }

}
