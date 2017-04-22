package munch.places.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
        @Index(name = "INDEX_PLACE_IMAGE_LINK_PLACE_ID", columnList = "placeId, sourceName")
})
public final class ImageLink {
    public static final String UNIQUE_CONSTRAINT_IMAGE_KEY = "UK_PLACE_IMAGE_LINK_IMAGE_KEY";

    private String id;
    private String imageKey;

    private String placeId;
    private String sourceName;
    private String sourceId;

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
}
