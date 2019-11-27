package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.postgres.PojoSetUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Publication is very similar to medium.com version of publication.
 * A publication can be sort of a team/company/organisation to manage their articles in a single page.
 * Multiple profile can join a publication.
 * <p>
 * Created by: Fuxing
 * Date: 2019-07-14
 * Time: 14:46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Publication")
@TypeDef(name = "Publication.TagsType", typeClass = Publication.TagsType.class)
public final class Publication implements ElasticSerializable {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{12}5$")
    @Id
    @Column(length = 13, updatable = false, nullable = false, unique = true)
    private String id;

    @NotBlank
    @Column(length = 80, updatable = true, nullable = true, unique = false)
    private String name;

    @NotBlank
    @Column(length = 250, updatable = true, nullable = true, unique = false)
    private String description;

    @Column(length = 800, updatable = true, nullable = true, unique = false)
    private String body;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Image image;

    @Valid
    @NotNull
    @Type(type = "Publication.TagsType")
    private Set<@NotNull Tag> tags;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
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

    public static final class TagsType extends PojoSetUserType<Tag> {
        public TagsType() {
            super(Tag.class);
        }
    }

    @PrePersist
    void prePersist() {
        setId(L13Id.PUBLICATION.randomId());
        setCreatedAt(new Timestamp(System.currentTimeMillis()));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ValidationException.validate(this, Default.class);
    }


    @Override
    public ElasticDocumentType getElasticDocumentType() {
        return ElasticDocumentType.PUBLICATION;
    }

    @Override
    public Map<String, String> getElasticDocumentKeys() {
        return Map.of("id", getId());
    }
}
