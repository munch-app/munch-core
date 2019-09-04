package app.munch.utils.random;

import java.time.Duration;

/**
 * Created by Fuxing
 * Date: 27/6/2015
 * Time: 11:38 AM
 * Project: PuffinCore
 */
public class RandomUtils extends org.apache.commons.lang3.RandomUtils {

    /**
     * @param functions functions array
     * @return sum of all chance
     */
    private static <F extends RandomReduce> long sumChance(F[] functions) {
        long sum = 0;
        for (F function : functions) {
            sum += function.chance();
        }
        return sum;
    }

    /**
     * Randomly random function with the most favorable chance having higher chance
     *
     * @param functions functions with chance to random
     * @param <F>       Reduce or Function Type
     * @return The function found
     */
    private static <F extends RandomReduce> F random(F[] functions) {
        long random = nextLong(0, sumChance(functions));
        long covered = 0;
        for (F func : functions) {
            if (covered <= random && random < covered + func.chance()) {
                return func;
            }
            covered += func.chance();
        }
        throw new RuntimeException("Problem with function utils. Should never happen.");
    }

    /**
     * @param functions random functions with bias
     */
    public static void run(RandomFunction... functions) {
        random(functions).run();
    }

    /**
     * @param c1 chance 1
     * @param f1 function 1
     * @param c2 chance 2
     * @param f2 function 2
     */
    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3,
                           long c4, RandomFunction f4) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3),
                RandomFunction.of(c4, f4));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3,
                           long c4, RandomFunction f4, long c5, RandomFunction f5) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3),
                RandomFunction.of(c4, f4), RandomFunction.of(c5, f5));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3,
                           long c4, RandomFunction f4, long c5, RandomFunction f5, long c6, RandomFunction f6) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3),
                RandomFunction.of(c4, f4), RandomFunction.of(c5, f5), RandomFunction.of(c6, f6));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3,
                           long c4, RandomFunction f4, long c5, RandomFunction f5, long c6, RandomFunction f6,
                           long c7, RandomFunction f7) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3),
                RandomFunction.of(c4, f4), RandomFunction.of(c5, f5), RandomFunction.of(c6, f6),
                RandomFunction.of(c7, f7));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3,
                           long c4, RandomFunction f4, long c5, RandomFunction f5, long c6, RandomFunction f6,
                           long c7, RandomFunction f7, long c8, RandomFunction f8) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3),
                RandomFunction.of(c4, f4), RandomFunction.of(c5, f5), RandomFunction.of(c6, f6),
                RandomFunction.of(c7, f7), RandomFunction.of(c8, f8));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3,
                           long c4, RandomFunction f4, long c5, RandomFunction f5, long c6, RandomFunction f6,
                           long c7, RandomFunction f7, long c8, RandomFunction f8, long c9, RandomFunction f9) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3),
                RandomFunction.of(c4, f4), RandomFunction.of(c5, f5), RandomFunction.of(c6, f6),
                RandomFunction.of(c7, f7), RandomFunction.of(c8, f8), RandomFunction.of(c9, f9));
    }

    public static void run(long c1, RandomFunction f1, long c2, RandomFunction f2, long c3, RandomFunction f3,
                           long c4, RandomFunction f4, long c5, RandomFunction f5, long c6, RandomFunction f6,
                           long c7, RandomFunction f7, long c8, RandomFunction f8, long c9, RandomFunction f9,
                           long c10, RandomFunction f10) {
        run(RandomFunction.of(c1, f1), RandomFunction.of(c2, f2), RandomFunction.of(c3, f3),
                RandomFunction.of(c4, f4), RandomFunction.of(c5, f5), RandomFunction.of(c6, f6),
                RandomFunction.of(c7, f7), RandomFunction.of(c8, f8), RandomFunction.of(c9, f9),
                RandomFunction.of(c10, f10));
    }

    /**
     * @param functions reduce functions with bias
     */
    public static <R> R reduce(RandomReduce<R>[] functions) {
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3, RandomReduce<R> f4) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3, f4};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3, RandomReduce<R> f4,
                               RandomReduce<R> f5) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3, f4, f5};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3, RandomReduce<R> f4,
                               RandomReduce<R> f5, RandomReduce<R> f6) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3, f4, f5, f6};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3, RandomReduce<R> f4,
                               RandomReduce<R> f5, RandomReduce<R> f6, RandomReduce<R> f7) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3, f4, f5, f6, f7};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3, RandomReduce<R> f4,
                               RandomReduce<R> f5, RandomReduce<R> f6, RandomReduce<R> f7, RandomReduce<R> f8) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3, f4, f5, f6, f7, f8};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3, RandomReduce<R> f4,
                               RandomReduce<R> f5, RandomReduce<R> f6, RandomReduce<R> f7, RandomReduce<R> f8,
                               RandomReduce<R> f9) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3, f4, f5, f6, f7, f8, f9};
        return random(functions).reduce();
    }

    @SuppressWarnings("unchecked")
    public static <R> R reduce(RandomReduce<R> f1, RandomReduce<R> f2, RandomReduce<R> f3, RandomReduce<R> f4,
                               RandomReduce<R> f5, RandomReduce<R> f6, RandomReduce<R> f7, RandomReduce<R> f8,
                               RandomReduce<R> f9, RandomReduce<R> f10) {
        RandomReduce<R>[] functions = new RandomReduce[]{f1, f2, f3, f4, f5, f6, f7, f8, f9, f10};
        return random(functions).reduce();
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3,
                               long c4, RandomReduce<R> f4) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3),
                RandomReduce.of(c4, f4));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3,
                               long c4, RandomReduce<R> f4, long c5, RandomReduce<R> f5) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3),
                RandomReduce.of(c4, f4), RandomReduce.of(c5, f5));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3,
                               long c4, RandomReduce<R> f4, long c5, RandomReduce<R> f5, long c6, RandomReduce<R> f6) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3),
                RandomReduce.of(c4, f4), RandomReduce.of(c5, f5), RandomReduce.of(c6, f6));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3,
                               long c4, RandomReduce<R> f4, long c5, RandomReduce<R> f5, long c6, RandomReduce<R> f6,
                               long c7, RandomReduce<R> f7) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3),
                RandomReduce.of(c4, f4), RandomReduce.of(c5, f5), RandomReduce.of(c6, f6),
                RandomReduce.of(c7, f7));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3,
                               long c4, RandomReduce<R> f4, long c5, RandomReduce<R> f5, long c6, RandomReduce<R> f6,
                               long c7, RandomReduce<R> f7, long c8, RandomReduce<R> f8) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3),
                RandomReduce.of(c4, f4), RandomReduce.of(c5, f5), RandomReduce.of(c6, f6),
                RandomReduce.of(c7, f7), RandomReduce.of(c8, f8));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3,
                               long c4, RandomReduce<R> f4, long c5, RandomReduce<R> f5, long c6, RandomReduce<R> f6,
                               long c7, RandomReduce<R> f7, long c8, RandomReduce<R> f8, long c9, RandomReduce<R> f9) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3),
                RandomReduce.of(c4, f4), RandomReduce.of(c5, f5), RandomReduce.of(c6, f6),
                RandomReduce.of(c7, f7), RandomReduce.of(c8, f8), RandomReduce.of(c9, f9));
    }

    public static <R> R reduce(long c1, RandomReduce<R> f1, long c2, RandomReduce<R> f2, long c3, RandomReduce<R> f3,
                               long c4, RandomReduce<R> f4, long c5, RandomReduce<R> f5, long c6, RandomReduce<R> f6,
                               long c7, RandomReduce<R> f7, long c8, RandomReduce<R> f8, long c9, RandomReduce<R> f9,
                               long c10, RandomReduce<R> f10) {
        return reduce(RandomReduce.of(c1, f1), RandomReduce.of(c2, f2), RandomReduce.of(c3, f3),
                RandomReduce.of(c4, f4), RandomReduce.of(c5, f5), RandomReduce.of(c6, f6),
                RandomReduce.of(c7, f7), RandomReduce.of(c8, f8), RandomReduce.of(c9, f9),
                RandomReduce.of(c10, f10));
    }

    /**
     * Obtain random days duration between start and end
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     * @return a random days {@code Duration}, not null
     */
    public static Duration nextDays(final long startInclusive, final long endExclusive) {
        return nextHours(startInclusive * 24, endExclusive * 24);
    }

    /**
     * Obtain random hours duration between start and end
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     * @return a random hours {@code Duration}, not null
     */
    public static Duration nextHours(final long startInclusive, final long endExclusive) {
        return nextMinutes(startInclusive * 60, endExclusive * 60);
    }

    /**
     * Obtain random minutes duration between start and end
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     * @return a random minutes {@code Duration}, not null
     */
    public static Duration nextMinutes(final long startInclusive, final long endExclusive) {
        return nextSeconds(startInclusive * 60, endExclusive * 60);
    }

    /**
     * Obtain random seconds duration between start and end
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     * @return a random seconds {@code Duration}, not null
     */
    public static Duration nextSeconds(final long startInclusive, final long endExclusive) {
        return nextMillis(startInclusive * 1000, endExclusive * 1000);
    }

    /**
     * Obtain random millis duration between start and end
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     * @return a random millis {@code Duration}, not null
     */
    public static Duration nextMillis(final long startInclusive, final long endExclusive) {
        return Duration.ofMillis(nextLong(startInclusive, endExclusive));
    }

    /**
     * Obtain random nanos duration between start and end
     *
     * @param startInclusive the smallest value that can be returned, must be non-negative
     * @param endExclusive   the upper bound (not included)
     * @return a random nanos {@code Duration}, not null
     */
    public static Duration nextNanos(final long startInclusive, final long endExclusive) {
        return Duration.ofNanos(nextLong(startInclusive, endExclusive));
    }
}
