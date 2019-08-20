package app.munch.model;

import app.munch.model.constraint.ImageDefaultGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.fuxing.err.ValidationException;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.utils.JsonUtils;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;

/**
 * Image is a container for the real resource in aws s3. <br>
 * Url will contains the location of the resource.
 * <p>
 * Created by: Fuxing
 * Date: 2019-07-14
 * Time: 14:46
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Image")
public final class Image {

    @NotNull // Required for sizes generation
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String id;

    @NotNull // Required for sizes generation
    @Pattern(regexp = "^\\.(jpg|png|gif|webp)$")
    private String ext;

    @JsonIgnore
    @NotNull(groups = {ImageDefaultGroup.class})
    @OneToOne(fetch = FetchType.LAZY, cascade = {})
    private Profile profile;

    @NotNull // Required for sizes generation
    @Pattern(regexp = "^mh0$")
    @Column(length = 32, updatable = false, nullable = false, unique = false)
    private String bucket;

    @Min(1)
    @NotNull
    @Column(updatable = false, nullable = false, unique = false)
    private Integer width;

    @Min(1)
    @NotNull
    @Column(updatable = false, nullable = false, unique = false)
    private Integer height;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    private ImageSource source;

    @NotNull(groups = {ImageDefaultGroup.class})
    @Column(updatable = false, nullable = false, unique = false)
    private Date createdAt;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public ImageSource getSource() {
        return source;
    }

    public void setSource(ImageSource source) {
        this.source = source;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @PrePersist
    void prePersist() {
        setCreatedAt(new Timestamp(System.currentTimeMillis()));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        ValidationException.validate(this, Default.class);
    }

    /**
     * Sizes: URL that are generated for specific size
     *
     * @return JsonNode for public with sizes url generated
     */
    @JsonValue
    public JsonNode toJson() {
        ObjectNode node = toJsonBasic();

        if (StringUtils.isNoneBlank(getExt(), getId(), getBucket())) {
            node.putObject("sizes")
                    .put("320x320", CdnUtils.create(getBucket(), getId(), getExt(), 320, 320))
                    .put("640x640", CdnUtils.create(getBucket(), getId(), getExt(), 640, 640))
                    .put("1080x1080", CdnUtils.create(getBucket(), getId(), getExt(), 1080, 1080));
        }
        return node;
    }

    /**
     * @return JsonNode for internal use with more details of the image
     */
    public JsonNode toJsonInternal() {
        ObjectNode node = toJsonBasic();

        HibernateUtils.initialize(this);
        HibernateUtils.initialize(getProfile());

        Profile profile = getProfile();
        if (profile != null) {
            node.putObject("profile")
                    .put("id", profile.getId())
                    .put("username", profile.getUsername());
        }
        return node;
    }

    private ObjectNode toJsonBasic() {
        ObjectNode node = JsonUtils.createObjectNode();
        node.put("id", getId());
        node.put("ext", getExt());
        node.put("bucket", getBucket());
        node.put("width", getWidth());
        node.put("height", getHeight());
        node.put("source", getSource().toString());
        return node;
    }

    /**
     * Develop signed url in the future with expiry for security reason.
     */
    private static class CdnUtils {
        private static final String API_URL = "https://cdn.munch.app/";
        private static final Base64.Encoder ENCODER = Base64.getEncoder();

        static String create(String bucket, String id, String ext, int width, int height) {
            ObjectNode edits = JsonUtils.createObjectNode();
            edits.putObject("resize")
                    .put("width", width)
                    .put("height", height)
                    .put("fit", "outside");

            return create(bucket, id, ext, edits);
        }

        static String create(String bucket, String id, String ext, JsonNode edits) {
            ObjectNode request = JsonUtils.createObjectNode(nodes -> {
                nodes.put("bucket", bucket);
                nodes.put("key", id + ext);
                nodes.set("edits", edits);
            });
            String json = JsonUtils.toString(request);
            return API_URL + ENCODER.encodeToString(json.getBytes());
        }
    }
}
