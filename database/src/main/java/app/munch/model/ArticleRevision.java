package app.munch.model;

import app.munch.model.constraint.ArticlePublishedGroup;
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
 * Created by: Fuxing
 * Date: 2019-07-16
 * Time: 19:14
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ArticleRevision")
public final class ArticleRevision extends ArticleModel {

    @JsonIgnore
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY, optional = false)
    private Article article;

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String revision;

    @NotNull
    @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}")
    @Column(length = 10, updatable = true, nullable = false, unique = false)
    private String version;

    @NotNull
    @Column(updatable = true, nullable = false, unique = false)
    private Boolean published;

    @NotNull
    @Column(updatable = false, nullable = false)
    private Date createdAt;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
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
        long millis = System.currentTimeMillis();
        setRevision(KeyUtils.nextULID(millis));
        setCreatedAt(new Timestamp(millis));
        setVersion("2019-08-01");

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        if (getPublished()) {
            ValidationException.validate(this, Default.class, ArticlePublishedGroup.class);
        } else {
            ValidationException.validate(this, Default.class);
        }
    }
}
