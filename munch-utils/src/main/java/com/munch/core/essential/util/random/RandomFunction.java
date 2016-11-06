package com.munch.core.essential.util.random;

import org.apache.commons.lang3.RandomUtils;

import java.util.Arrays;

/**
 * Created by Fuxing
 * Date: 27/6/2015
 * Time: 11:38 AM
 * Project: PuffinCore
 */
public class RandomFunction extends RandomUtils {

    public static void random(Function... functions) {
        int random = nextInt(0, functions.length);
        functions[random].run(1);
    }

    public static <T> T randomReduce(ReduceFunction<T>... functions) {
        int random = nextInt(0, functions.length);
        return functions[random].run(1);
    }

    public static void random(FunctionPair... functionPair) {
        int totalRandom = Arrays.stream(functionPair).mapToInt(FunctionPair::getLeft).sum();
        int random = nextInt(0, totalRandom);
        int covered = 0;
        for (FunctionPair pair : functionPair) {
            if (covered <= random && random < covered + pair.getLeft()) {
                pair.getRight().run(pair.getLeft());
                break;
            }
            covered += pair.getLeft();
        }
        // find array to get
    }

    public static <T> T randomReduce(ReduceFunctionPair<T>... functionPair) {
        int totalRandom = Arrays.stream(functionPair).mapToInt(ReduceFunctionPair::getLeft).sum();
        int random = nextInt(0, totalRandom);
        int covered = 0;

        T reduce = null;
        for (ReduceFunctionPair<T> pair : functionPair) {
            if (covered <= random && random < covered + pair.getLeft()) {
                reduce = pair.getRight().run(pair.getLeft());
                break;
            }
            covered += pair.getLeft();
        }
        return reduce;
    }

    public static FunctionPair pair(Integer left, RandomFunction.Function right) {
        return new FunctionPair(left, right);
    }

    public static <T> ReduceFunctionPair<T> reducePair(Integer left, RandomFunction.ReduceFunction<T> right) {
        return new ReduceFunctionPair<>(left, right);
    }

    @FunctionalInterface
    public interface Function {
        void run(int chance);
    }

    @FunctionalInterface
    public interface ReduceFunction<T> {
        T run(int chance);
    }
}
