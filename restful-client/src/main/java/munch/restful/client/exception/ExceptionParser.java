package munch.restful.client.exception;

import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.HttpHostConnectException;

import java.net.SocketTimeoutException;

/**
 * Created By: Fuxing Loh
 * Date: 18/3/2017
 * Time: 4:41 PM
 * Project: munch-core
 */
public final class ExceptionParser {

    /**
     * @param e all exception
     * @return converted restful exception
     */
    public static RestfulException handle(Exception e) {
        RestfulException restful;

        // Offline
        restful = handleOffline(e);
        if (restful != null) return restful;

        // Timeout
        restful = handleTimeout(e);
        if (restful != null) return restful;

        // Else default wrap still
        return new RestfulException(e);
    }

    /**
     * Try parse offline exception
     *
     * @param e all exception
     * @return Restful Offline exception
     */
    static RestfulException handleOffline(Exception e) {
        if (e instanceof HttpHostConnectException) {
            return new OfflineException(e);
        }

        if (e instanceof NoHttpResponseException) {
            return new OfflineException(e);
        }

        return null;
    }

    /**
     * Try parse timeout exception
     *
     * @param e all exception
     * @return Restful Timeout exception
     */
    static TimeoutException handleTimeout(Exception e) {
        if (e instanceof SocketTimeoutException) {
            return new TimeoutException(e);
        }

        return null;
    }
}
