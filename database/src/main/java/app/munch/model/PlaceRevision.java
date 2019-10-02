package app.munch.model;

import app.munch.model.constraint.PlaceDefaultGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * Date: 2019-07-15
 * Time: 22:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PlaceRevision")
public final class PlaceRevision extends PlaceModel {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}0$")
    @Column(length = 13, updatable = false, nullable = false, unique = false)
    private String id;

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @JsonIgnore
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    private Place place;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile createdBy;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private ChangeGroup changeGroup;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    /**
     * @return PlaceRevision created by a profile
     */
    public Profile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Profile createdBy) {
        this.createdBy = createdBy;
    }

    public ChangeGroup getChangeGroup() {
        return changeGroup;
    }

    public void setChangeGroup(ChangeGroup changeGroup) {
        this.changeGroup = changeGroup;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        // Initialising some required Place data
        assert place != null;

        if (place.getId() == null) {
            place.setId(KeyUtils.nextL(12, '0'));
        }

        setId(place.getId());

        long time = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(time));
        setCreatedAt(new Timestamp(time));

        // Copy into Place
        copyToPlace();

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setSynonyms(getSynonyms().stream()
                .map(StringUtils::lowerCase)
                .map(StringUtils::trim)
                .collect(Collectors.toSet()));

        ValidationException.validate(this, Default.class, PlaceDefaultGroup.class);
    }

    void copyToPlace() {
        place.setName(getName());
        place.setPhone(getPhone());
        place.setWebsite(getWebsite());
        place.setDescription(getDescription());

        place.setPrice(getPrice());
        place.setLocation(getLocation());
        place.setStatus(getStatus());

        place.setSynonyms(getSynonyms());
        place.setTags(getTags());
        place.setHours(getHours());
    }
}
