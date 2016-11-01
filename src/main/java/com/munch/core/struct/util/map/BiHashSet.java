package com.munch.core.struct.util.map;

import java.util.HashSet;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 8:16 PM
 * Project: struct
 */
public class BiHashSet<O extends OneEntity, T extends ManyEntity<O>> extends HashSet<T> {

    private O single;

    public BiHashSet(O single) {
        super();
        this.single = single;
    }

    /**
     * Bi Directional Persist add
     *
     * @param many many entity to add
     * @return whether if it is added
     */
    @Override
    public boolean add(T many) {
        many.setOneEntity(single);
        return super.add(many);
    }
}
