package app.munch.model;

import app.munch.model.constraint.ArticlePlaceDefaultGroup;
import app.munch.model.constraint.ArticlePublishedGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 2019-07-14
 * Time: 14:46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ArticlePlace")
public final class ArticlePlace extends PlaceModel {

    @NotNull(groups = {ArticlePlaceDefaultGroup.class})
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Article article;

    @NotNull(groups = {ArticlePlaceDefaultGroup.class})
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Place place;

    @NotNull(groups = {ArticlePlaceDefaultGroup.class})
    @Column(updatable = true, nullable = false, unique = false)
    private Long position;

    @NotNull(groups = {ArticlePlaceDefaultGroup.class})
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull(groups = {ArticlePublishedGroup.class, ArticlePlaceDefaultGroup.class})
    @Pattern(regexp = "^[0123456789abcdefghjkmnpqrstvwxyz]{12}0$")
    @Column(length = 13, updatable = true, nullable = true, unique = false)
    private String id;

    @Valid
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Image image;

    @NotNull(groups = {ArticlePlaceDefaultGroup.class})
    @Version
    @Column(updatable = true, nullable = false, unique = false)
    private Date updatedAt;

    @NotNull(groups = {ArticlePlaceDefaultGroup.class})
    @Column(updatable = false, nullable = false, unique = false)
    private Date createdAt;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Long getPosition() {
        return position;
    }

    public void setPosition(Long position) {
        this.position = position;
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
        long millis = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(millis));
        setCreatedAt(new Timestamp(millis));

        if (getPosition() == null) {
            setPosition(millis);
        }

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        // Default & ArticlePublishedGroup
        ValidationException.validate(this, Default.class, ArticlePublishedGroup.class);
    }
}
