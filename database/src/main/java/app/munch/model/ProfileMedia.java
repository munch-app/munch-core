package app.munch.model;

import com.fasterxml.jackson.annotation.*;
import dev.fuxing.err.ValidationException;
import dev.fuxing.postgres.PojoListUserType;
import dev.fuxing.postgres.PojoUserType;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Media is a top level object that is a short-form of Social Media
 * <p>
 * Date: 25/9/19
 * Time: 11:31 pm
 *
 * @author Fuxing Loh
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ProfileMedia", uniqueConstraints = {
        @UniqueConstraint(name = "uk_an27370fqpajyefr", columnNames = {"type", "eid"})
})
@TypeDef(name = "ProfileMedia.Content", typeClass = ProfileMedia.ContentType.class)
@TypeDef(name = "ProfileMedia.Metric", typeClass = ProfileMedia.MetricType.class)
public final class ProfileMedia {

    @NotNull
    @Pattern(regexp = "^[0-9a-hjkmnp-tv-z]{15}s$")
    @Id
    @Column(length = 16, updatable = false, nullable = false, unique = true)
    private String id;

    /**
     * External id, unique from each the platform
     */
    @NotNull
    @Column(length = 512, updatable = false, nullable = false, unique = false)
    private String eid;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = false)
    private Profile profile;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private ProfileSocial social;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private ProfileMediaType type;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private ProfileMediaStatus status;

    @OrderBy("createdAt DESC")
    @JoinColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(cascade = {})
    private List<Image> images;

    @Valid
    @NotNull
    @Type(type = "ProfileMedia.Content")
    @Size(max = 20)
    private List<@NotNull Node> content;

    /**
     * Non cascading, edit from Mention
     */
    @OrderBy("createdAt DESC")
    @JoinColumn
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade = {}, mappedBy = "media", orphanRemoval = false)
    private List<Mention> mentions;

    @NotNull
    @Valid
    @Type(type = "ProfileMedia.Metric")
    private Metric metric;

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

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ProfileSocial getSocial() {
        return Lazy.load(social, null);
    }

    public void setSocial(ProfileSocial profileSocial) {
        this.social = profileSocial;
    }

    public ProfileMediaType getType() {
        return type;
    }

    public void setType(ProfileMediaType type) {
        this.type = type;
    }

    public ProfileMediaStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileMediaStatus status) {
        this.status = status;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Node> getContent() {
        return content;
    }

    public void setContent(List<Node> content) {
        this.content = content;
    }

    public List<Mention> getMentions() {
        return mentions;
    }

    public void setMentions(List<Mention> mentions) {
        this.mentions = mentions;
    }

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
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

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static class ContentType extends PojoListUserType<Node> {
        public ContentType() {
            super(Node.class);
        }
    }

    public static class MetricType extends PojoUserType<Metric> {
        public MetricType() {
            super(Metric.class);
        }
    }

    @PrePersist
    void prePersist() {
        setId(KeyUtils.nextL(15, 's'));
        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        ValidationException.validate(this, Default.class);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Metric {

        @Min(0)
        private Long up;

        @Min(0)
        private Long down;

        public Long getUp() {
            return up;
        }

        public void setUp(Long up) {
            this.up = up;
        }

        public Long getDown() {
            return down;
        }

        public void setDown(Long down) {
            this.down = down;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(TextNode.class),
    })
    public interface Node {
        @JsonIgnore
        String getType();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonTypeName("text")
    public static final class TextNode implements Node {

        @NotBlank
        private String text;

        @Override
        public String getType() {
            return "text";
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
