package app.munch.utils.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Fuxing
 * Date: 26/11/16
 * Time: 11:57 PM
 * Project: corpus-catalyst
 */
public interface Retry {
    Logger logger = LoggerFactory.getLogger(Retry.class);

    /**
     * Pass Exception to this method to check if can be retried
     * Then check if can retry
     *
     * @param exception      exception to check if can retry
     * @param executionCount current retry count
     * @return retry, check if exception can be retried
     */
    boolean retry(final Throwable exception, final int executionCount);

    /**
     * No return type loop
     *
     * @param runnable function to run in retry loop
     */
    default <E extends Throwable> void loop(Runnable<E> runnable) throws E {
        loop(() -> {
            runnable.run();
            return null;
        });
    }

    /**
     * Exception if thrown will be wrapped to RuntimeException
     *
     * @param function function to run in retry loop
     * @param <T>      Type
     * @return reduced Type
     * @throws RuntimeException thrown if cannot be resolve in the loop
     */
    default <T, E extends Throwable> T loop(Function<T, E> function) throws E {
        int executionCount = 0;
        do {
            try {
                // Try run
                return function.run();
            } catch (Throwable exception) {
                // Check if error can be retry
                executionCount++;
                log(exception, executionCount);

                if (!retry(exception, executionCount)) {
                    // Exception cannot be retry
                    throw exception;
                }
            }
        } while (true);
    }

    /**
     * Retriable logger
     * Override for custom implementation
     * First execution error won't be logged
     *
     * @param exception      exception
     * @param executionCount executed count
     */
    default void log(final Throwable exception, final int executionCount) {
        // Default logger
        if (executionCount == 1) return;

        logger.warn("Exception caught, retry count: {}", executionCount, exception);
    }

    /**
     * Function to run in retry loop
     *
     * @param <T>
     */
    @FunctionalInterface
    interface Function<T, E extends Throwable> {
        T run() throws E;
    }

    /**
     * Function to run in retry loop
     * No return values
     */
    @FunctionalInterface
    interface Runnable<E extends Throwable> {
        void run() throws E;
    }
}
