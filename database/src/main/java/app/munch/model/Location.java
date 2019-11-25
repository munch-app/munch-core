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

/**
 * @author Fuxing Loh
 * @since 2019-11-22 at 13:15
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Location")
public final class Location extends LocationModel implements ElasticSerializable {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}2$")
    @Id
    @Column(length = 13, updatable = false, nullable = false, unique = true)
    private String id;

    @NotNull
    @Pattern(regexp = "[0-9a-z-]{0,200}")
    @Column(length = 200, updatable = true, nullable = false, unique = false)
    private String slug;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Image image;

    /**
     * Indicates who initially created this location.
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

    public void setId(String id) {
        this.id = id;
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
            setId(L13Id.LOCATION.randomId());
        }

        if (getCreatedAt() == null) {
            setCreatedAt(new Timestamp(System.currentTimeMillis()));
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

        ValidationException.validate(this, Default.class);
    }

    @Override
    public ElasticDocumentType getElasticDocumentType() {
        return ElasticDocumentType.LOCATION;
    }
}
