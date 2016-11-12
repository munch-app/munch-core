package com.munch.core.struct.util;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 7:14 PM
 * Project: struct
 */
public interface CollectionEntity {

    /**
     * @return Id required for hashcode generation
     */
    String getId();

    default boolean equals(final Object obj, Class clazz) {
        if (this == obj)
            return true;
        if (obj == null || clazz != obj.getClass())
            return false;
        return ((CollectionEntity) obj).getId().equals(this.getId());
    }

    /* Code to be placed at implementing class
    @Override
    public int hashCode() {
        if (getId() == null) {
            return super.hashCode();
        } else {
            return getId().hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        return equals(obj, getClass());
    }
    */
}
