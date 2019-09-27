package app.munch.model;

import com.fasterxml.jackson.annotation.*;
import dev.fuxing.postgres.PojoListUserType;
import dev.fuxing.postgres.PojoUserType;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;
import java.util.List;

/**
 * Media is a top level object that is a short-form of Social Media
 * <p>
 * Created by: Fuxing
 * Date: 25/9/19
 * Time: 11:31 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ProfileMedia")
@TypeDef(name = "ProfileMedia.Content", typeClass = ProfileMedia.ContentType.class)
@TypeDef(name = "ProfileMedia.Metric", typeClass = ProfileMedia.MetricType.class)
public final class ProfileMedia {

    /**
     * Internal id, unique to SocialObject table
     */
    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    /**
     * External id, unique from each the platform
     */
    @NotNull
    @Column(length = 512, updatable = false, nullable = false, unique = true)
    private String eid;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile profile;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private ProfileSocial profileSocial;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private ProfileMediaType type;

    @ManyToMany(cascade = {}, fetch = FetchType.EAGER)
    private List<Image> images;

    @Valid
    @NotNull
    @Type(type = "ProfileMedia.Content")
    @Size(max = 20)
    private List<@NotNull Node> content;

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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public ProfileSocial getProfileSocial() {
        return profileSocial;
    }

    public void setProfileSocial(ProfileSocial profileSocial) {
        this.profileSocial = profileSocial;
    }

    public ProfileMediaType getType() {
        return type;
    }

    public void setType(ProfileMediaType type) {
        this.type = type;
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

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
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
