package com.munch.core.struct.util.map;

import java.util.ArrayList;

/**
 * Created by: Fuxing
 * Date: 2/11/2016
 * Time: 3:09 AM
 * Project: struct
 */
public class BiArrayList<O extends OneEntity, T extends ManyEntity<O>> extends ArrayList<T> {

    private O single;

    public BiArrayList(O single) {
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
