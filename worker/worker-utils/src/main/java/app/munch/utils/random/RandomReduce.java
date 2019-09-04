package app.munch.utils.random;

/**
 * Created by: Fuxing
 * Date: 30/1/2017
 * Time: 3:55 AM
 * Project: munch-utils
 */
@FunctionalInterface
public interface RandomReduce<T> {

    /**
     * Chance is calculated by total sum of chance of all function slated to run
     *
     * @return chance of which the random function will run
     */
    default long chance() {
        return 1;
    }

    /**
     * Reduce function
     *
     * @return Reduced Data Type
     */
    T reduce();

    /**
     * @param chance chance or biases of random
     * @param reduce reduce function
     * @param <R>    Return Type
     * @return RandomReduce with override bias
     */
    static <R> RandomReduce<R> of(long chance, RandomReduce<R> reduce) {
        return new Default<>(chance, reduce);
    }

    /**
     * Default implementation with chance and function initialization
     *
     * @param <R> Return Type
     */
    class Default<R> implements RandomReduce<R> {
        private final long chance;
        private final RandomReduce<R> reduce;

        public Default(long chance, RandomReduce<R> reduce) {
            this.chance = chance;
            this.reduce = reduce;
        }

        @Override
        public long chance() {
            return chance;
        }

        @Override
        public R reduce() {
            return reduce.reduce();
        }
    }
}
