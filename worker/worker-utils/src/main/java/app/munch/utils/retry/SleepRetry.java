package app.munch.utils.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * Default Counter, Sleep Implementation
 */
public class SleepRetry implements Retry {
    private static final Logger logger = LoggerFactory.getLogger(SleepRetry.class);
    private final int retries;
    private final Duration sleep;

    private final BiConsumer<Throwable, Integer> logConsumer;

    /**
     * @param retries    total num of retries
     * @param eachMillis each millis of sleep
     */
    public SleepRetry(int retries, long eachMillis) {
        this(retries, Duration.ofMillis(eachMillis));
    }

    /**
     * @param retries  total num of retries
     * @param timeUnit each time unit of sleep
     * @param duration each sleep duration of time unit
     */
    public SleepRetry(int retries, TimeUnit timeUnit, long duration) {
        this(retries, timeUnit.toMillis(duration));
    }

    /**
     * @param retries      total num of retries
     * @param eachDuration each duration of sleep
     */
    public SleepRetry(int retries, Duration eachDuration) {
        this(retries, eachDuration, (throwable, integer) -> {
            if (integer == 1) return;
            logger.warn("Exception caught, retry count: {}/{}, sleep: {}", integer, retries, eachDuration, throwable);
        });
    }

    /**
     * @param retries      total num of retries
     * @param eachDuration each duration of sleep
     * @param logConsumer  log consumer overrider
     */
    public SleepRetry(int retries, Duration eachDuration, BiConsumer<Throwable, Integer> logConsumer) {
        this.retries = retries;
        this.sleep = eachDuration;
        this.logConsumer = logConsumer;
    }

    @Override
    public boolean retry(final Throwable exception, final int executionCount) {
        if (executionCount > retries) {
            return false;
        }

        // Only sleep if can retry
        sleep();
        return true;
    }

    /**
     * Sleep before retry
     */
    protected void sleep() {
        try {
            Thread.sleep(sleep.toMillis());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void log(Throwable exception, int executionCount) {
        if (logConsumer != null) {
            logConsumer.accept(exception, executionCount);
        }
    }
}
