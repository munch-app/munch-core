package com.munch.core.essential.util;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Created By: Fuxing Loh
 * Date: 17/10/2016
 * Time: 8:11 PM
 * Project: essential
 */
public final class DistinctPair<K, E> extends Pair<K, E> {

    private final K key;
    private final E element;

    public DistinctPair(K key, E element) {
        this.key = key;
        this.element = element;
    }

    @Override
    public K getLeft() {
        return key;
    }

    @Override
    public E getRight() {
        return element;
    }

    @Override
    public E setValue(E value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof DistinctPair && ((DistinctPair) other).getLeft().equals(this.getLeft());
    }

    @Override
    public int hashCode() {
        return getLeft().hashCode();
    }

    public static <K, E> DistinctPair<K, E> of(K key, E element) {
        return new DistinctPair<>(key, element);
    }
}
