package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 27/9/19
 * Time: 5:20 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "MentionStatus")
public final class MentionStatus {

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile profile;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private MentionType type;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private MentionStatusStatus status;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Place place;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private ProfileMedia media;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Mention mention;

    @NotNull
    @Version
    @Column(updatable = true, nullable = false, unique = false)
    private Date updatedAt;

    @NotNull
    @Column(updatable = false, nullable = false, unique = false)
    private Date createdAt;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public MentionType getType() {
        return type;
    }

    public void setType(MentionType type) {
        this.type = type;
    }

    public MentionStatusStatus getStatus() {
        return status;
    }

    public void setStatus(MentionStatusStatus status) {
        this.status = status;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public ProfileMedia getMedia() {
        return media;
    }

    public void setMedia(ProfileMedia media) {
        this.media = media;
    }

    public Mention getMention() {
        return mention;
    }

    public void setMention(Mention mention) {
        this.mention = mention;
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
        setUid(KeyUtils.nextULID());
        setCreatedAt(new Timestamp(System.currentTimeMillis()));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        // TODO(fuxing): strict validation
        ValidationException.validate(this, Default.class);
    }
}
