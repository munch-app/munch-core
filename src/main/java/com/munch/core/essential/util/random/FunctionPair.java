package com.munch.core.essential.util.random;

/**
 * Created by Fuxing
 * Date: 27/6/2015
 * Time: 12:47 PM
 * Project: PuffinCore
 */
public class FunctionPair {
    public final Integer left;
    public final RandomFunction.Function right;

    public static FunctionPair of(Integer left, RandomFunction.Function right) {
        return new FunctionPair(left, right);
    }

    public static <T> ReduceFunctionPair<T> reduce(Integer left, RandomFunction.ReduceFunction<T> right) {
        return new ReduceFunctionPair<>(left, right);
    }

    public FunctionPair(Integer left, RandomFunction.Function right) {
        this.left = left;
        this.right = right;
    }

    public Integer getLeft() {
        return left;
    }

    public RandomFunction.Function getRight() {
        return right;
    }
}
