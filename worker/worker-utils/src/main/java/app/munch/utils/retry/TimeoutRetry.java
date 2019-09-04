package app.munch.utils.retry;

import munch.restful.core.exception.TimeoutException;

import java.time.Duration;

/**
 * Created by: Fuxing
 * Date: 8/11/18
 * Time: 8:59 AM
 * Project: catalyst
 */
public final class TimeoutRetry extends ExceptionRetry {

    /**
     * Front facing timeout retriable
     */
    public TimeoutRetry() {
        this(6, Duration.ofMillis(300));
    }

    /**
     * Front facing timeout retriable
     *
     * @param maxRetries max number of retries
     */
    public TimeoutRetry(int maxRetries) {
        this(maxRetries, Duration.ofMillis(300));
    }

    /**
     * @param duration interval between retry
     */
    public TimeoutRetry(Duration duration) {
        this(6, duration);
    }

    /**
     * @param maxRetries max number of retries
     * @param duration   interval between retry
     */
    public TimeoutRetry(int maxRetries, Duration duration) {
        super(maxRetries, duration, TimeoutException.class, java.util.concurrent.TimeoutException.class);
    }
}
