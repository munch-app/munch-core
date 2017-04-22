package munch.places.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 21/4/2017
 * Time: 1:47 AM
 * Project: munch-core
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {"imageKey"},
                name = ImageLink.UNIQUE_CONSTRAINT_IMAGE_KEY
        )
}, indexes = {
        @Index(name = "index_place_image_link_place_id", columnList = "placeId, sourceName")
})
public final class ImageLink {
    public static final String UNIQUE_CONSTRAINT_IMAGE_KEY = "uk_place_image_link_image_key";

    private String id;
    private String imageKey;

    private String placeId;
    private String sourceName;
    private String sourceId;

    private Date createdDate;

    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "CHAR(36)", nullable = false)
    @Id
    @JsonIgnore
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(nullable = false, updatable = false)
    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }

    @Column(nullable = false, updatable = false)
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @Column(nullable = false, updatable = false)
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    @Column(nullable = false, updatable = false)
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Column(nullable = false, updatable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
