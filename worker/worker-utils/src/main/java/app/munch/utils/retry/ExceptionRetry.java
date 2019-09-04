package app.munch.utils.retry;

import org.apache.commons.lang3.exception.ExceptionUtils;

import javax.net.ssl.SSLException;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 19/12/2016
 * Time: 3:37 PM
 * Project: corpus-catalyst
 */
public class ExceptionRetry extends SleepRetry {
    private final Set<Class<? extends Throwable>> retriableClasses;

    /**
     * These 4 exception are default to retry
     * 1: InterruptedIOException
     * 2: UnknownHostException
     * 3: ConnectException
     * 4: SSLException
     *
     * @param maxRetries max num of retries
     */
    public ExceptionRetry(int maxRetries) {
        this(maxRetries, Duration.ofMillis(200), Set.of(
                InterruptedIOException.class,
                UnknownHostException.class,
                ConnectException.class,
                SSLException.class));
    }

    /**
     * @param maxRetries max num of retries
     * @param millis     sleep between retry
     * @param c1         throwable to retry
     */
    public ExceptionRetry(int maxRetries, long millis, Class<? extends Throwable> c1) {
        this(maxRetries, Duration.ofMillis(millis), Set.of(c1));
    }

    /**
     * @param maxRetries max num of retries
     * @param millis     sleep between retry
     * @param c1         throwable to retry
     * @param c2         throwable to retry
     */
    public ExceptionRetry(int maxRetries, long millis, Class<? extends Throwable> c1, Class<? extends Throwable> c2) {
        this(maxRetries, Duration.ofMillis(millis), Set.of(c1, c2));
    }

    /**
     * @param maxRetries max num of retries
     * @param millis     sleep between retry
     * @param c1         throwable to retry
     * @param c2         throwable to retry
     * @param c3         throwable to retry
     */
    public ExceptionRetry(int maxRetries, long millis, Class<? extends Throwable> c1, Class<? extends Throwable> c2,
                          Class<? extends Throwable> c3) {
        this(maxRetries, Duration.ofMillis(millis), Set.of(c1, c2, c3));
    }

    /**
     * @param maxRetries max num of retries
     * @param millis     sleep between retry
     * @param c1         throwable to retry
     * @param c2         throwable to retry
     * @param c3         throwable to retry
     * @param c4         throwable to retry
     */
    public ExceptionRetry(int maxRetries, long millis, Class<? extends Throwable> c1, Class<? extends Throwable> c2,
                          Class<? extends Throwable> c3, Class<? extends Throwable> c4) {
        this(maxRetries, Duration.ofMillis(millis), Set.of(c1, c2, c3, c4));
    }

    /**
     * @param maxRetries max num of retries
     * @param millis     sleep between retry
     * @param c1         throwable to retry
     * @param c2         throwable to retry
     * @param c3         throwable to retry
     * @param c4         throwable to retry
     * @param c5         throwable to retry
     */
    public ExceptionRetry(int maxRetries, long millis, Class<? extends Throwable> c1, Class<? extends Throwable> c2,
                          Class<? extends Throwable> c3, Class<? extends Throwable> c4, Class<? extends Throwable> c5) {
        this(maxRetries, Duration.ofMillis(millis), Set.of(c1, c2, c3, c4, c5));
    }

    /**
     * @param maxRetries max num of retries
     * @param millis     sleep between retry
     * @param c1         throwable to retry
     * @param c2         throwable to retry
     * @param c3         throwable to retry
     * @param c4         throwable to retry
     * @param c5         throwable to retry
     * @param c6         throwable to retry
     */
    public ExceptionRetry(int maxRetries, long millis, Class<? extends Throwable> c1, Class<? extends Throwable> c2,
                          Class<? extends Throwable> c3, Class<? extends Throwable> c4, Class<? extends Throwable> c5,
                          Class<? extends Throwable> c6) {
        this(maxRetries, Duration.ofMillis(millis), Set.of(c1, c2, c3, c4, c5, c6));
    }


    /**
     * @param maxRetries   max num of retries
     * @param eachDuration sleep between retry
     * @param c1           single throwable
     */
    public ExceptionRetry(int maxRetries, Duration eachDuration, Class<? extends Throwable> c1) {
        this(maxRetries, eachDuration, Set.of(c1));
    }

    /**
     * @param maxRetries   max num of retries
     * @param eachDuration sleep between retry
     * @param c1           throwable to retry
     * @param c2           throwable to retry
     */
    public ExceptionRetry(int maxRetries, Duration eachDuration, Class<? extends Throwable> c1, Class<? extends Throwable> c2) {
        this(maxRetries, eachDuration, Set.of(c1, c2));
    }

    /**
     * @param maxRetries   max num of retries
     * @param eachDuration sleep between retry
     * @param c1           throwable to retry
     * @param c2           throwable to retry
     * @param c3           throwable to retry
     */
    public ExceptionRetry(int maxRetries, Duration eachDuration, Class<? extends Throwable> c1, Class<? extends Throwable> c2,
                          Class<? extends Throwable> c3) {
        this(maxRetries, eachDuration, Set.of(c1, c2, c3));
    }

    /**
     * @param maxRetries   max num of retries
     * @param eachDuration sleep between retry
     * @param c1           throwable to retry
     * @param c2           throwable to retry
     * @param c3           throwable to retry
     * @param c4           throwable to retry
     */
    public ExceptionRetry(int maxRetries, Duration eachDuration, Class<? extends Throwable> c1, Class<? extends Throwable> c2,
                          Class<? extends Throwable> c3, Class<? extends Throwable> c4) {
        this(maxRetries, eachDuration, Set.of(c1, c2, c3, c4));
    }

    /**
     * @param maxRetries   max num of retries
     * @param eachDuration sleep between retry
     * @param c1           throwable to retry
     * @param c2           throwable to retry
     * @param c3           throwable to retry
     * @param c4           throwable to retry
     * @param c5           throwable to retry
     */
    public ExceptionRetry(int maxRetries, Duration eachDuration, Class<? extends Throwable> c1, Class<? extends Throwable> c2,
                          Class<? extends Throwable> c3, Class<? extends Throwable> c4, Class<? extends Throwable> c5) {
        this(maxRetries, eachDuration, Set.of(c1, c2, c3, c4, c5));
    }

    /**
     * @param maxRetries max num of retries
     * @param millis     sleep between retry
     * @param classes    accepted classes
     */
    public ExceptionRetry(int maxRetries, long millis, Collection<Class<? extends Throwable>> classes) {
        this(maxRetries, Duration.ofMillis(millis), classes);
    }

    /**
     * @param maxRetries   max num of retries
     * @param eachDuration sleep between retry
     * @param classes      accepted classes
     */
    public ExceptionRetry(int maxRetries, Duration eachDuration, Collection<Class<? extends Throwable>> classes) {
        super(maxRetries, eachDuration);

        this.retriableClasses = new HashSet<>(classes);
    }

    @Override
    public boolean retry(final Throwable exception, final int executionCount) {
        if (!retry(exception)) return false;
        return super.retry(exception, executionCount);
    }

    /**
     * @param exception Check if exception can be retry
     * @return true = can retry
     */
    protected boolean retry(final Throwable exception) {
        if (retriableClasses.contains(exception.getClass())) {
            return true;
        } else {
            for (Class<? extends Throwable> clazz : retriableClasses) {
                if (ExceptionUtils.hasCause(exception, clazz)) {
                    return true;
                }
            }
        }
        return false;
    }
}
