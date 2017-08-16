package munch.data.database;

import munch.data.database.hibernate.PojoUserType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by: Fuxing
 * Date: 16/8/2017
 * Time: 10:06 PM
 * Project: munch-core
 */
@Entity
@TypeDefs(value = {
        @TypeDef(name = "data", typeClass = PlaceEntity.DataType.class)
})
@Table(indexes = {
//        @Index(name = "index_munch_place_entity", columnList = "")
})
public final class PlaceEntity extends AbstractEntity<Place> {

    public PlaceEntity() {
        super(Place::getId);
    }

    public static class DataType extends PojoUserType<Place> {
        public DataType() {
            super(Place.class);
        }
    }
}
