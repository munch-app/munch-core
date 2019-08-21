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
 * Date: 2019-07-15
 * Time: 22:51
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "PlaceRevision")
public final class PlaceRevision extends PlaceModel {

    @NotNull
    @Pattern(regexp = "^[0123456789abcdefghjkmnpqrstvwxyz]{12}0$")
    @Column(length = 13, updatable = false, nullable = false, unique = false)
    private String id;

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Place place;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile createdBy;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        long time = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(time));
        setCreatedAt(new Timestamp(time));

        if (getPlace() != null) {
            setId(getPlace().getId());
        }

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
}
