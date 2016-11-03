package com.munch.core.struct.util.map;

import java.util.ArrayList;

/**
 * Created by: Fuxing
 * Date: 2/11/2016
 * Time: 3:09 AM
 * Project: struct
 */
public class BiArrayList<O extends EntityOne, T extends EntityMany<O>> extends ArrayList<T> {

    private final O one;

    public BiArrayList(O one) {
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
