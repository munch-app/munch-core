package app.munch.model;

import app.munch.model.constraint.ManagedPagePublishedGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.fuxing.err.ValidationException;
import dev.fuxing.postgres.PojoListUserType;
import dev.fuxing.utils.KeyUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 21/8/19
 * Time: 8:17 am
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ManagedPage")
@TypeDef(name = "ManagedPage.ContentType", typeClass = ManagedPage.ContentType.class)
public final class ManagedPage {

    /**
     * This is path, encoded in base 64
     */
    @NotNull
    @Column(length = 512, updatable = false, nullable = false, unique = false)
    private String path;

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull
    private Boolean published;

    @Valid
    @NotNull
    @Type(type = "ManagedPage.ContentType")
    private List<@NotNull Node> content;

    @NotNull
    @Column(updatable = false, nullable = false, unique = false)
    private Date createdAt;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public List<Node> getContent() {
        return content;
    }

    public void setContent(List<Node> content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static class ContentType extends PojoListUserType<Node> {
        public ContentType() {
            super(Node.class);
        }
    }

    @PrePersist
    void prePersist() {
        long millis = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(millis));
        setCreatedAt(new Timestamp(millis));

        if (getPublished() != null && getPublished()) {
            ValidationException.validate(this, Default.class, ManagedPagePublishedGroup.class);
        }
    }

    /**
     * Created by: Fuxing
     * Date: 21/8/19
     * Time: 8:23 pm
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
    @JsonSubTypes({
            @JsonSubTypes.Type(value = PublicationArticlesNode.class, name = "PUBLICATION_ARTICLES"),
            @JsonSubTypes.Type(value = PublicationFeaturedArticlesNode.class, name = "PUBLICATION_FEATURED_ARTICLES"),
    })
    public interface Node {
        String getType();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicationArticlesNode implements Node {

        @NotNull(groups = {ManagedPagePublishedGroup.class})
        @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}5$")
        private String publicationId;

        @Override
        public String getType() {
            return "PUBLICATION_ARTICLES";
        }

        public String getPublicationId() {
            return publicationId;
        }

        public void setPublicationId(String publicationId) {
            this.publicationId = publicationId;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PublicationFeaturedArticlesNode implements Node {

        @NotNull(groups = {ManagedPagePublishedGroup.class})
        @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}5$")
        private String publicationId;

        @Override
        public String getType() {
            return "PUBLICATION_FEATURED_ARTICLES";
        }

        public String getPublicationId() {
            return publicationId;
        }

        public void setPublicationId(String publicationId) {
            this.publicationId = publicationId;
        }
    }
}
