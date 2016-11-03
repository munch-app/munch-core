package com.munch.core.struct.util.map;

import java.util.HashSet;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 8:16 PM
 * Project: struct
 */
public class BiHashSet<O extends EntityOne, T extends EntityMany<O>> extends HashSet<T> {

    private final O one;

    public BiHashSet(O one) {
        super();
        this.one = one;
    }

    /**
     * Bi Directional Persist add
     *
     * @param many many entity to add
     * @return whether if it is added
     */
    @Override
    public boolean add(T many) {
        many.applyEntityOne(one);
        return super.add(many);
    }

}
