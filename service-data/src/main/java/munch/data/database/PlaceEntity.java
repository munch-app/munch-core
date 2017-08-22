package munch.data.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import munch.data.Place;
import munch.data.database.hibernate.PojoUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 16/8/2017
 * Time: 10:06 PM
 * Project: munch-core
 */
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@TypeDefs(value = {
        @TypeDef(name = "price", typeClass = PlaceEntity.PriceUserType.class),
        @TypeDef(name = "location", typeClass = PlaceEntity.LocationUserType.class),
        @TypeDef(name = "tags", typeClass = PlaceEntity.TagsUserType.class),
        @TypeDef(name = "hours", typeClass = PlaceEntity.HoursUserType.class),
        @TypeDef(name = "images", typeClass = PlaceEntity.ImagesUserType.class),
})
@Table(indexes = {
        @Index(name = "index_munch_place_entity_cycle_no", columnList = "cycleNo")
})
public final class PlaceEntity extends Place implements CycleEntity {

    private Long cycleNo;

    @Override
    @Column(nullable = false)
    public Long getCycleNo() {
        return cycleNo;
    }

    @Override
    public void setCycleNo(Long cycleNo) {
        this.cycleNo = cycleNo;
    }

    @Override
    @Id
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getId() {
        return super.getId();
    }

    @Override
    @Column(nullable = false, length = 255)
    public String getName() {
        return super.getName();
    }

    @Override
    @Column(nullable = true, length = 255)
    public String getPhone() {
        return super.getPhone();
    }

    @Override
    @Column(nullable = true, length = 500)
    public String getWebsite() {
        return super.getWebsite();
    }

    @Override
    @Column(nullable = true, length = 2000)
    public String getDescription() {
        return super.getDescription();
    }

    @Override
    @Type(type = "price")
    @Column(nullable = true)
    public Price getPrice() {
        return super.getPrice();
    }

    @Override
    @Type(type = "location")
    @Column(nullable = false)
    public Location getLocation() {
        return super.getLocation();
    }

    @Override
    @Type(type = "tags")
    @Column(nullable = false)
    public List<String> getTags() {
        return super.getTags();
    }

    @Override
    @Type(type = "hours")
    @Column(nullable = false)
    public List<Hour> getHours() {
        return super.getHours();
    }

    @Override
    @Type(type = "images")
    @Column(nullable = false)
    public List<Image> getImages() {
        return super.getImages();
    }

    @Override
    @Column(nullable = false)
    public Date getCreatedDate() {
        return super.getCreatedDate();
    }

    @Override
    @Column(nullable = false)
    public Date getUpdatedDate() {
        return super.getUpdatedDate();
    }

    @Override
    @Transient
    public String getType() {
        return super.getType();
    }

    public final static class PriceUserType extends PojoUserType<Place.Price> {
        public PriceUserType() {
            super(Place.Price.class);
        }
    }

    public final static class LocationUserType extends PojoUserType<Place.Location> {
        public LocationUserType() {
            super(Place.Location.class);
        }
    }

    public final static class TagsUserType extends PojoUserType<TagsUserType.ListString> {
        public TagsUserType() {
            super(ListString.class);
        }

        public final static class ListString extends ArrayList<String> {
        }
    }

    public final static class HoursUserType extends PojoUserType<HoursUserType.ListPlaceHour> {
        public HoursUserType() {
            super(ListPlaceHour.class);
        }

        public final static class ListPlaceHour extends ArrayList<Place.Hour> {
        }
    }

    public final static class ImagesUserType extends PojoUserType<ImagesUserType.ListPlaceImage> {
        public ImagesUserType() {
            super(ListPlaceImage.class);
        }

        public final static class ListPlaceImage extends ArrayList<Place.Image> {
        }
    }
}
