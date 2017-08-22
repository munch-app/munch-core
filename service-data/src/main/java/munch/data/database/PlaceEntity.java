package munch.data.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import munch.data.Place;
import munch.data.database.hibernate.PojoUserType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;

/**
 * Created by: Fuxing
 * Date: 16/8/2017
 * Time: 10:06 PM
 * Project: munch-core
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDefs(value = {
        @TypeDef(name = "data", typeClass = PlaceEntity.PlaceUserType.class),
})
@Table(indexes = {
        // Cluster name for placeId
        @Index(name = "index_munch_place_entity_cycle_no", columnList = "cycleNo")
})
public final class PlaceEntity extends AbstractEntity<Place> {

    private String placeId;

    @Id
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }


    public final static class PlaceUserType extends PojoUserType<Place> {
        public PlaceUserType() {
            super(Place.class);
        }
    }
}
