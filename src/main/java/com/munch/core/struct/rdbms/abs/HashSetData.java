package com.munch.core.struct.rdbms.abs;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:14 PM
 * Project: struct
 */
public interface HashSetData {

    /**
     * @return Id required for hashcode generation
     */
    String getId();

    default boolean equals(final Object obj, Class clazz) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (clazz != obj.getClass())
            return false;
        return obj.hashCode() == this.hashCode();
    }

    /* Code to be placed at implementing class
    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return equals(obj, getClass());
    }
     */
}
