package app.munch.model;

import app.munch.model.annotation.ValidMentionStatus;
import app.munch.model.annotation.ValidMentionType;
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
 * Date: 10/9/19
 * Time: 8:04 pm
 *
 * @author Fuxing Loh
 * @see app.munch.model.validator.MentionStatusValidator for class level validation
 * @see app.munch.model.validator.MentionTypeValidator for class level validation
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Mention", uniqueConstraints = {
        @UniqueConstraint(name = "uk_2rjxx36ptzagatrp", columnNames = {"place_id", "article_id"}),
        @UniqueConstraint(name = "uk_88jnqnzqvndsmhnm", columnNames = {"place_id", "media_uid"}),
})
@ValidMentionType
@ValidMentionStatus
public final class Mention {
    // TODO(fuxing): Future: MentionLocking, to prevent it from unwanted editing.

    /**
     * Mention uses L16 ids, mention is not a top level node, it's a unique node tho.
     * In comparision with places, mention can generate as many as 10_000x of place (too many of them)
     */
    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{16}$")
    @Id
    @Column(length = 16, updatable = false, nullable = false, unique = true)
    private String id;

    /**
     * @see ValidMentionStatus for how validation works
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Place place;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile profile;

    /**
     * @see ValidMentionType for how validation works
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Article article;

    /**
     * @see ValidMentionType for how validation works
     */
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private ProfileMedia media;

    /**
     * @see ValidMentionType for how validation works
     */
    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private MentionType type;

    /**
     * @see ValidMentionStatus for how validation works
     */
    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private MentionStatus status;

    /**
     * Who created this mention linking
     */
    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile createdBy;

    @NotNull
    @Version
    @Column(updatable = true, nullable = false, unique = false)
    private Date updatedAt;

    @NotNull
    @Column(updatable = false, nullable = false, unique = false)
    private Date createdAt;

    public String getId() {
        return id;
    }

    private void setId(String id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public ProfileMedia getMedia() {
        return media;
    }

    public void setMedia(ProfileMedia media) {
        this.media = media;
    }

    public MentionType getType() {
        return type;
    }

    public void setType(MentionType type) {
        this.type = type;
    }

    public MentionStatus getStatus() {
        return status;
    }

    public void setStatus(MentionStatus status) {
        this.status = status;
    }

    public Profile getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Profile createdBy) {
        this.createdBy = createdBy;
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

    private void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        setId(KeyUtils.nextL16());
        setCreatedAt(new Timestamp(System.currentTimeMillis()));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ValidationException.validate(this, Default.class);
    }
}
