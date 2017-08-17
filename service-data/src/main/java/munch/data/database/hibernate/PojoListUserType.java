package munch.data.database.hibernate;

import java.util.Collections;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 17/8/2017
 * Time: 1:12 PM
 * Project: munch-core
 */
public abstract class PojoListUserType<T> extends PojoUserType<List<T>> {

    @SuppressWarnings("unchecked")
    public PojoListUserType() {
        super(((Class<List<T>>) Collections.<T>emptyList().getClass()));
    }
}
