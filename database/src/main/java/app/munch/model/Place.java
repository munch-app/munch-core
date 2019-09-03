package app.munch.model;

import app.munch.model.constraint.PlaceDefaultGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 2019-07-14
 * Time: 14:46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Place")
public final class Place extends PlaceModel {

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

    @NotNull
    @Column(updatable = true, nullable = false)
    private Date interactedAt;

    @NotNull
    @Version
    @Column(updatable = true, nullable = false)
    private Date updatedAt;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        if (getId() == null) {
            setId(KeyUtils.nextL12() + "0");
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
        setSlug(KeyUtils.generateSlug(getName(), 200));
        setSynonyms(cleanSynonyms(getSynonyms()));

        if (getUpdatedAt() == null) {
            setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        }

        ValidationException.validate(this, Default.class, PlaceDefaultGroup.class);
    }
}
