package app.munch.model;

import app.munch.model.group.ImageDefaultGroup;
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

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.function.Consumer;
import java.util.regex.Matcher;

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
    /*
     * Validation groups:
     * Default.class is of all cases including Database and internal use
     * ImageDefaultGroup.class only for Database and internal use
     */

    /*
     * Improvements:
     * A ImageLinked entity to track how images are used. By setting up this entity with foreign key constraint,
     * you will effectively create cascading properties that forces user to remove this image from linked entity
     * before it can deleted.
     */

    private static final java.util.regex.Pattern LOC_PATTERN = java.util.regex.Pattern.compile(
            "^[a-z0-9]{3,}-([0123456789abcdefghjkmnpqrstvwxyz]{26})\\.[a-z0-9]{1,}"
    );

    /**
     * Universal id of Image without extension.
     * S3 Stores it as such:
     * bucket: uid+ext
     */
    @NotNull(groups = {ImageDefaultGroup.class})
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull(groups = {ImageDefaultGroup.class})
    @Pattern(regexp = "^\\.(jpg|png|gif|webp)$")
    private String ext;

    /**
     * AWS S3 Bucket hosted in ap-southeast-1
     * mh0: generic profile created
     * mh4: manually uploaded static resources, used for engineering use.
     * mh5: instagram media images
     */
    @NotNull(groups = {ImageDefaultGroup.class})
    @Pattern(regexp = "^mh[05]$")
    @Column(length = 32, updatable = false, nullable = false, unique = false)
    private String bucket;

    /**
     * URL safe unique variable of Image
     * ${bucket}-${uid}${ext}
     */
    @NotNull
    @Pattern(regexp = "^mh[05]-[0123456789abcdefghjkmnpqrstvwxyz]{26}\\.(jpg|png|gif|webp)$")
    @Column(length = 100, updatable = false, nullable = false, unique = true)
    private String loc;

    @JsonIgnore
    @NotNull(groups = {ImageDefaultGroup.class})
    @ManyToOne(fetch = FetchType.LAZY, cascade = {})
    private Profile profile;

    /**
     * Required so that UI component can know the size of the image before rendering
     */
    @NotNull
    @Min(1)
    @Column(updatable = false, nullable = false, unique = false)
    private Integer width;

    /**
     * Required so that UI component can know the size of the image before rendering
     */
    @NotNull
    @Min(1)
    @Column(updatable = false, nullable = false, unique = false)
    private Integer height;

    @ValidEnum(groups = {ImageDefaultGroup.class})
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private ImageSource source;

    @NotNull(groups = {ImageDefaultGroup.class})
    @Column(updatable = false, nullable = false, unique = false)
    private Date createdAt;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

        if (StringUtils.isNoneBlank(getExt(), getUid(), getBucket())) {
            setLoc(getBucket() + "-" + getUid() + getExt());
        }

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        ValidationException.validate(this, Default.class, ImageDefaultGroup.class);
    }

    /**
     * Sizes: URL that are generated for specific size
     * <p>
     * Technically, @PostLoad can be used to fill sizes. However, I feel that when image is converted into json,
     * the image are therefore ready for transport to client side, hence it is a better time to generate sizes.
     *
     * @return JsonNode for public with sizes url generated
     */
    @JsonValue
    public JsonNode toJson() {
        ObjectNode node = toJsonBasic();

        if (StringUtils.isNotBlank(getLoc())) {
            String[] bucketKey = CdnUtils.breakLoc(getLoc());
            node.putObject("sizes")
                    .put("320x320", CdnUtils.create(bucketKey[0], bucketKey[1], 320, 320))
                    .put("640x640", CdnUtils.create(bucketKey[0], bucketKey[1], 640, 640))
                    .put("1080x1080", CdnUtils.create(bucketKey[0], bucketKey[1], 1080, 1080));
        }
        return node;
    }

    /**
     * @return JsonNode for internal use with more details of the image
     */
    public JsonNode toJsonInternal() {
        ObjectNode node = toJsonBasic();
        node.put("bucket", getBucket());
        node.put("uid", getUid());
        node.put("ext", getExt());
        node.put("source", getSource().toString());


        HibernateUtils.initialize(this);
        HibernateUtils.initialize(getProfile());

        Profile profile = getProfile();
        if (profile != null) {
            node.putObject("profile")
                    .put("id", profile.getUid())
                    .put("username", profile.getUsername());
        }
        return node;
    }

    private ObjectNode toJsonBasic() {
        ObjectNode node = JsonUtils.createObjectNode();
        node.put("loc", getLoc());
        node.put("width", getWidth());
        node.put("height", getHeight());
        return node;
    }

    /**
     * Develop signed url in the future with expiry for security reason.
     */
    private static class CdnUtils {
        private static final String API_URL = "https://cdn.munch.app/";
        private static final Base64.Encoder ENCODER = Base64.getEncoder();

        static String[] breakLoc(String loc) {
            return loc.split("-");
        }

        @Nullable
        static String locToUid(String loc) {
            if (loc == null) return null;

            Matcher matcher = LOC_PATTERN.matcher(loc);
            if (matcher.matches()) {
                return matcher.group(1);
            }
            return null;
        }

        static String create(String bucket, String key, int width, int height) {
            ObjectNode edits = JsonUtils.createObjectNode();
            edits.putObject("resize")
                    .put("width", width)
                    .put("height", height)
                    .put("fit", "outside");

            return create(bucket, key, edits);
        }

        static String create(String bucket, String key, JsonNode edits) {
            ObjectNode request = JsonUtils.createObjectNode(nodes -> {
                nodes.put("bucket", bucket);
                nodes.put("key", key);
                if (edits != null) {
                    nodes.set("edits", edits);
                }
            });
            String json = JsonUtils.toString(request);
            return API_URL + ENCODER.encodeToString(json.getBytes());
        }
    }

    public static final class EntityUtils {

        public static void map(EntityManager entityManager, Image image, Consumer<Image> setter) {
            if (image != null) {
                map(entityManager, image.getUid(), image.getLoc(), setter);
            }
        }

        public static void map(EntityManager entityManager, JsonNode node, Consumer<Image> setter) {
            if (node.isNull()) {
                setter.accept(null);
            } else {
                map(entityManager, node.path("uid").asText(null), node.path("loc").asText(null), setter);
            }
        }

        public static void map(EntityManager entityManager, String uid, String loc, Consumer<Image> setter) {
            if (uid != null) {
                setter.accept(entityManager.find(Image.class, uid));
            } else if (loc != null) {
                uid = CdnUtils.locToUid(loc);

                if (uid != null) {
                    setter.accept(entityManager.find(Image.class, uid));
                }
            }
        }
    }

    /**
     * @param source to resolve to respective bucket
     * @return bucket name
     */
    public static String resolveBucket(ImageSource source) {
        switch (source) {
            case INSTAGRAM:
                return "mh5";

            default:
                return "mh0";

            case UNKNOWN_TO_SDK_VERSION:
                throw new IllegalStateException("ImageSource.UNKNOWN_TO_SDK_VERSION");
        }
    }

    /**
     * Quick way to generate cdn.munch.app url
     */
    public static void main(String[] args) {
        System.out.println(CdnUtils.create("mh4", "write-a-letter-to-a-loved-one.jpg", 1080, 1080));
    }
}
