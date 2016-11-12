package com.munch.core.struct.rdbms;

import com.munch.core.struct.util.HibernateUtil;

/**
 * Created by: Fuxing
 * Date: 13/11/2016
 * Time: 12:40 AM
 * Project: munch-core
 */
public interface EntityTestInterface {

    default void removeEntity(Class clazz, Object object) {
        HibernateUtil.with(em -> em.remove(em.find(clazz, object)));
    }

}
