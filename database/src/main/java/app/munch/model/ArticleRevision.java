package app.munch.model;

import app.munch.model.constraint.ArticlePublishedGroup;
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
import java.util.Optional;

/**
 * Created by: Fuxing
 * Date: 2019-07-16
 * Time: 19:14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ArticleRevision")
public final class ArticleRevision extends ArticleModel {

    @NotNull
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}")
    @Column(length = 10, updatable = true, nullable = false, unique = false)
    private String version;

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    private Article article;

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}1$")
    @Column(length = 13, updatable = false, nullable = false, unique = false)
    private String id;

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull
    @Column(updatable = true, nullable = false, unique = false)
    private Boolean published;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        // Initialising some required Article data
        if (article.getId() == null) {
            article.setId(KeyUtils.nextL12() + "1");
        }
        if (article.getCreatedAt() == null) {
            article.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        long millis = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(millis));
        setCreatedAt(new Timestamp(millis));
        setVersion("2019-08-01");

        if (getPublished() != null && getPublished()) {
            article.setStatus(ArticleStatus.PUBLISHED);
            preUpdatePublished();
        } else if (article.getStatus() == ArticleStatus.DRAFT) {
            preUpdateDrafting();
        }

        setId(article.getId());
        setProfile(article.getProfile());
        setSlug(KeyUtils.generateSlug(article.getTitle(), 200));

        if (getPublished() != null && getPublished()) {
            ValidationException.validate(this, Default.class, ArticlePublishedGroup.class);
        } else {
            ValidationException.validate(this, Default.class);
        }
    }

    /**
     * If revision is marked as published, all values will be moved to article
     */
    private void preUpdatePublished() {
        article.setTitle(getTitle());
        article.setDescription(getDescription());

        article.setImage(getImage());
        article.setTags(getTags());
        article.setContent(getContent());
        article.setOptions(getOptions());
    }

    private void preUpdateDrafting() {
        String title = Optional.ofNullable(getTitle())
                .filter(StringUtils::isNotBlank)
                .or(() -> NodeUtils.findFirstHeading(getContent()))
                .orElse("Untitled Article");
        article.setTitle(title);

        Optional.ofNullable(getDescription())
                .filter(StringUtils::isNotBlank)
                .or(() -> NodeUtils.findFirstParagraph(getContent()))
                .ifPresent(s -> {
                    article.setDescription(s);
                });

        article.setImage(getImage());
        article.setTags(getTags());
        article.setContent(getContent());
        article.setOptions(getOptions());
    }
}
