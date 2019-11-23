package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 2019-07-14
 * Time: 14:46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Place")
public final class Place extends PlaceModel implements ElasticSerializable {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}0$")
    @Id
    @Column(length = 13, updatable = false, nullable = false, unique = true)
    private String id;

    /**
     * Catalyst id, for referencing and deprecating support.
     */
    @Column(length = 100, updatable = false, nullable = true, unique = true)
    private String cid;

    @NotNull
    @Pattern(regexp = "[0-9a-z-]{0,200}")
    @Column(length = 200, updatable = true, nullable = false, unique = false)
    private String slug;

    /**
     * Select by a worker.
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Image image;

    /**
     * Between 0.0 to 1.0, inclusive, not validated because validation for floating point value is not supported.
     * Default to 0.0, filled by a worker.
     */
    @NotNull
    @Column(updatable = true, nullable = false)
    private Double important;

    /**
     * Indicates who initially created this place.
     */
    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile createdBy;

    @JsonIgnore
    @NotNull
    @Column(updatable = true, nullable = false)
    private Date interactedAt;

    @JsonIgnore
    @NotNull
    @Version
    @Column(updatable = true, nullable = false)
    private Date updatedAt;

    @JsonIgnore
    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public String getId() {
        return id;
    }

    void setId(String id) {
        this.id = id;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Double getImportant() {
        return important;
    }

    public void setImportant(Double important) {
        this.important = important;
    }

    public Profile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Profile createdBy) {
        this.createdBy = createdBy;
    }

    public Date getInteractedAt() {
        return interactedAt;
    }

    public void setInteractedAt(Date interactedAt) {
        this.interactedAt = interactedAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    private void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    /**
     * Visibility is temporary public for now
     */
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @param o to compare
     * @return if 2 Place is the same entity. (only id/pk is checked)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return id.equals(place.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @PrePersist
    void prePersist() {
        if (getId() == null) {
            setId(L13Id.PLACE.randomId());
        }

        if (getCreatedAt() == null) {
            setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        if (getImportant() == null) {
            setImportant(0.0);
        }

        if (getInteractedAt() == null) {
            setInteractedAt(getCreatedAt());
        }

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setSlug(Model.generateSlug(getName(), 200));
        setSynonyms(Model.cleanSynonyms(getSynonyms(), 4));

        if (getUpdatedAt() == null) {
            setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        }

        ValidationException.validate(this, Default.class, Groups.PlaceDefault.class);
    }
}
