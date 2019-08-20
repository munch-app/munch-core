package app.munch.model;

import app.munch.model.constraint.PlaceDefaultGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.stream.Collectors;

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
    /*
        Implement 'importance' and its' worker.
     */

    @NotNull
    @Pattern(regexp = "^[0123456789abcdefghjkmnpqrstvwxyz]{12}0$")
    @Id
    @Column(length = 13, updatable = false, nullable = false, unique = true)
    private String id;

    @Column(length = 100, updatable = false, nullable = true, unique = true)
    private String deprecatedId;

    @NotNull
    @Pattern(regexp = "[0-9a-z-]{0,200}")
    @Column(length = 200, updatable = true, nullable = false, unique = false)
    private String slug;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Image image;

    @NotNull
    @Version
    @Column(updatable = true, nullable = false)
    private Date updatedAt;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    // TODO(fuxing): Provided By Profile Mapping

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeprecatedId() {
        return deprecatedId;
    }

    public void setDeprecatedId(String deprecatedId) {
        this.deprecatedId = deprecatedId;
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
        setId(KeyUtils.nextL12() + "0");
        if (getCreatedAt() == null) {
            setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setSlug(KeyUtils.generateSlug(getName()));
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        setSynonyms(getSynonyms().stream()
                .map(StringUtils::lowerCase)
                .map(StringUtils::trim)
                .collect(Collectors.toSet()));

        ValidationException.validate(this, Default.class, PlaceDefaultGroup.class);
    }
}
