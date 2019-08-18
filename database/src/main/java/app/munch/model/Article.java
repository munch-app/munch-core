package app.munch.model;

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
    @Pattern(regexp = "^[0123456789abcdefghjkmnpqrstvwxyz]{12}1$")
    @Id
    @Column(length = 13, updatable = false, nullable = false, unique = true)
    private String id;

    @ValidEnum
    private ArticleStatus status;

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
        setSlug(KeyUtils.generateSlug(getTitle()));
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        if (getStatus() == ArticleStatus.PUBLISHED) {
            ValidationException.validate(this, Default.class, ArticlePublishedGroup.class);
        } else {
            ValidationException.validate(this, Default.class);
        }
    }
}
