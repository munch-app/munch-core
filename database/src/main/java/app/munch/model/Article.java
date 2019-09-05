package app.munch.model;

import app.munch.model.constraint.ArticleDeletedGroup;
import app.munch.model.constraint.ArticleDraftGroup;
import app.munch.model.constraint.ArticlePublishedGroup;
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
 * Date: 2019-07-14
 * Time: 14:45
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Article")
public final class Article extends ArticleModel {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}1$")
    @Id
    @Column(length = 13, updatable = false, nullable = false, unique = true)
    private String id;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private ArticleStatus status;

    /**
     * The first every date it was published.
     */
    @NotNull(groups = ArticlePublishedGroup.class)
    @Column(updatable = false, nullable = true)
    private Date publishedAt;

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

    public ArticleStatus getStatus() {
        return status;
    }

    public void setStatus(ArticleStatus status) {
        this.status = status;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
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
            setId(KeyUtils.nextL12() + "1");
        }
        if (getCreatedAt() == null) {
            setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setSlug(KeyUtils.generateSlug(getTitle(), 200));
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        switch (getStatus()) {
            case PUBLISHED:
                ValidationException.validate(this, Default.class, ArticlePublishedGroup.class);
                return;

            case DRAFT:
                ValidationException.validate(this, Default.class, ArticleDraftGroup.class);
                return;

            case DELETED:
                ValidationException.validate(this, Default.class, ArticleDeletedGroup.class);
                return;

            case UNKNOWN_TO_SDK_VERSION:
            default:

        }
    }
}
