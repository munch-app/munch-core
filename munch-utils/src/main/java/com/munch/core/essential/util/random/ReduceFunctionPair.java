package com.munch.core.essential.util.random;

/**
 * Created by Fuxing
 * Date: 27/6/2015
 * Time: 12:54 PM
 * Project: PuffinCore
 */
public class ReduceFunctionPair<T> {
    public final Integer left;
    public final RandomFunction.ReduceFunction<T> right;

    public static <T> ReduceFunctionPair<T> of(Integer left, RandomFunction.ReduceFunction<T> right) {
        return new ReduceFunctionPair<>(left, right);
    }

    public ReduceFunctionPair(Integer left, RandomFunction.ReduceFunction<T> right) {
        this.left = left;
        this.right = right;
    }

    public Integer getLeft() {
        return left;
    }

    public RandomFunction.ReduceFunction<T> getRight() {
        return right;
    }

}
