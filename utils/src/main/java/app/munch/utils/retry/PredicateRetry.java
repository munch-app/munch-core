package app.munch.utils.retry;

import java.time.Duration;
import java.util.function.Predicate;

/**
 * Created by: Fuxing
 * Date: 13/6/2017
 * Time: 10:15 PM
 * Project: corpus-catalyst
 */
public class PredicateRetry extends SleepRetry {
    private final Predicate<Throwable> predicate;

    /**
     * @param eachDuration sleep between retry
     * @param maxRetries   max num of retries
     * @param predicate    predicate for retry
     */
    public PredicateRetry(int maxRetries, Duration eachDuration, Predicate<Throwable> predicate) {
        super(maxRetries, eachDuration);
        this.predicate = predicate;
    }

    @Override
    public boolean retry(final Throwable exception, final int executionCount) {
        if (!predicate.test(exception)) return false;
        return super.retry(exception, executionCount);
    }
}
