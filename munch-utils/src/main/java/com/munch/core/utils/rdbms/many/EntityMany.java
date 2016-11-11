package com.munch.core.utils.rdbms.many;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 9:21 PM
 * Project: struct
 */
public interface EntityMany<O extends EntityOne> {

    void applyEntityOne(O one);

}
