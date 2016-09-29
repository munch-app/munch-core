package com.munch.core.struct.util.map;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 9:21 PM
 * Project: struct
 */
public interface ManyEntity<O extends OneEntity> {

    void setOneEntity(O single);

}
