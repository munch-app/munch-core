package munch.restful.client.exception;

import munch.restful.core.exception.StructuredException;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.SocketTimeoutException;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 5:19 PM
 * Project: munch-core
 */
public final class TimeoutException extends StructuredException {

    public TimeoutException(Throwable throwable) {
        super(408, "TimeoutException", "Request from client to server has timeout.", throwable);
    }

    /**
     * Try parse timeout exception
     *
     * @param e all exception
     * @throws TimeoutException Timeout exception
     */
    public static void parse(Exception e) throws TimeoutException {
        for (Throwable throwable : ExceptionUtils.getThrowables(e)) {
            if (throwable instanceof SocketTimeoutException) {
                throw new TimeoutException(e);
            }
        }
    }
}
