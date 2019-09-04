package app.munch.utils.random;

/**
 * Created by: Fuxing
 * Date: 30/1/2017
 * Time: 4:16 AM
 * Project: munch-utils
 */
@FunctionalInterface
public interface RandomFunction extends RandomReduce<Void> {

    /**
     * Chance is calculated by total sum of chance of all function slated to run
     *
     * @return chance of which the random function will run
     */
    default long chance() {
        return 1;
    }

    /**
     * Run when random hits
     */
    void run();

    /**
     * Overrides with default for cross compatibility with reduce func
     *
     * @return Void
     */
    default Void reduce() {
        run();
        return null;
    }

    /**
     * @param chance   chance or biases of random
     * @param function reduce function
     * @return RandomFunction with override bias
     */
    static RandomFunction of(long chance, RandomFunction function) {
        return new Default(chance, function);
    }

    /**
     * Default implementation with chance and function initialization
     */
    class Default implements RandomFunction {
        private final long chance;
        private final RandomFunction function;

        public Default(long chance, RandomFunction function) {
            this.chance = chance;
            this.function = function;
        }

        @Override
        public long chance() {
            return chance;
        }

        @Override
        public void run() {
            function.run();
        }
    }
}
