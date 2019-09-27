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
 * Date: 10/9/19
 * Time: 8:04 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Mention")
public final class Mention {

    /**
     * Mention uses L16 ids, mention is not a top level node, it's a unique node tho.
     * In comparision with places, mention can generate as many as 10_000x of place (too many of them)
     */
    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{16}$")
    @Id
    @Column(length = 16, updatable = false, nullable = false, unique = true)
    private String id;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Place place;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile profile;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Article article;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private ProfileMedia media;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private MentionType type;

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

    public void setId(String id) {
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
        setId(KeyUtils.nextL16());
        setCreatedAt(new Timestamp(System.currentTimeMillis()));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ValidationException.validate(this, Default.class);

        // TODO(fuxing): Strict Validation
    }
}
