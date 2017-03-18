package munch.restful.client.exception;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.conn.HttpHostConnectException;

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
        if (e instanceof UnirestException) {
            return handle((UnirestException) e);
        }
        return new RestfulException(e);
    }

    /**
     * @param e all unirest exception
     * @return Restful exception
     */
    static RestfulException handle(UnirestException e) {
        if (e.getCause() instanceof HttpHostConnectException) {
            return new OfflineException(e);
        }

        if (e.getCause() instanceof NoHttpResponseException) {
            return new OfflineException(e);
        }

        return new RestfulException(e);
    }

}
