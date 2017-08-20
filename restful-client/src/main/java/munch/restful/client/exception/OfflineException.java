package munch.restful.client.exception;

import munch.restful.core.exception.StructuredException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.HttpHostConnectException;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
public final class OfflineException extends StructuredException {

    OfflineException(Throwable throwable) {
        super(500, "OfflineException", "Request from client to server is not reachable.", throwable);
    }

    /**
     * Try parse offline exception
     *
     * @param e all exception
     * @throws TimeoutException timeout
     */
    public static void parse(Exception e) throws OfflineException {
        for (Throwable throwable : ExceptionUtils.getThrowables(e)) {
            if (throwable instanceof HttpHostConnectException) {
                throw new OfflineException(e);
            }
            if (throwable instanceof NoHttpResponseException) {
                throw new OfflineException(e);
            }
        }
    }
}
