package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.validator.ValidEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 2019-07-14
 * Time: 14:45
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Article")
public final class Article extends ArticleModel implements ElasticSerializable {

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
    @NotNull(groups = Groups.ArticlePublished.class)
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
            setId(L13Id.ARTICLE.randomId());
        }
        if (getCreatedAt() == null) {
            setCreatedAt(new Timestamp(System.currentTimeMillis()));
        }

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setSlug(Model.generateSlug(getTitle(), 200));
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        if (getStatus() == ArticleStatus.PUBLISHED) {
            setPublishedAt(new Timestamp(System.currentTimeMillis()));
            ValidationException.validate(this, Default.class, Groups.ArticlePublished.class);
        } else {
            ValidationException.validate(this, Default.class);
        }
    }

    @Override
    public ElasticDocumentType getElasticDocumentType() {
        return ElasticDocumentType.ARTICLE;
    }

    @Override
    public Map<String, String> getElasticDocumentKeys() {
        return Map.of("id", getId());
    }
}
