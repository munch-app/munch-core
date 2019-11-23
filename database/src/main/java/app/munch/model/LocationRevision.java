package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * @author Fuxing Loh
 * @since 2019-11-22 at 13:34
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "LocationRevision")
public final class LocationRevision extends LocationModel {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}2$")
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
    private Location location;

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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        // Initialising some required Location data
        assert location != null;
        if (location.getId() == null) {
            location.setId(L13Id.LOCATION.randomId());
        }

        setId(location.getId());

        long time = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(time));
        setCreatedAt(new Timestamp(time));

        // Copy into Location
        copyToLocation();

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setSynonyms(Model.cleanSynonyms(getSynonyms(), 4));

        ValidationException.validate(this, Default.class);
    }

    void copyToLocation() {
        location.setName(getName());
        location.setDescription(getDescription());

        location.setPostcode(getPostcode());
        location.setAddress(getAddress());
        location.setLatLng(getLatLng());

        location.setType(getType());
        location.setGeometry(getGeometry());
        location.setSynonyms(getSynonyms());
    }
}
