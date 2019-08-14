package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.fuxing.err.ValidationException;
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
import java.util.Map;

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

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String id;

    @NotNull
    @Pattern(regexp = "^\\.(jpg|png|gif|webp)$")
    private String ext;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = {})
    private Profile profile;

    @JsonIgnore
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

    /**
     * Not persisted, but exist because it is auto generated on the fly.
     */
    @Transient
    private Map<String, String> sizes;

    @NotNull
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
        fillSizes();
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
        fillSizes();
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
        fillSizes();
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

    /**
     * Key: Dimension (Width x Height) e.g. '400x514' <br>
     * Value: Signed URL (cached by CDN) <br>
     *
     * @return multiple signed images of different sizes.
     */
    public Map<String, String> getSizes() {
        return sizes;
    }

    public void setSizes(Map<String, String> sizes) {
        this.sizes = sizes;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    private void fillSizes() {
        if (StringUtils.isAnyBlank(getExt(), getId(), getBucket())) return;

        String key = getId() + getExt();
        String api = "https://cdn.munch.app";
        setSizes(Map.of(
                "320x320", SharpUtils.create(api, getBucket(), key, 320, 320),
                "640x640", SharpUtils.create(api, getBucket(), key, 640, 640),
                "1080x1080", SharpUtils.create(api, getBucket(), key, 1080, 1080)
        ));
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
     * Develop signed url in the future with expiry for security reason.
     */
    private static class SharpUtils {
        private static final Base64.Encoder ENCODER = Base64.getEncoder();

        static String create(String api, String bucket, String key, int width, int height) {
            ObjectNode edits = JsonUtils.createObjectNode();
            edits.putObject("resize")
                    .put("width", width)
                    .put("height", height)
                    .put("fit", "outside");

            return create(api, bucket, key, edits);
        }

        static String create(String api, String bucket, String key, JsonNode edits) {
            ObjectNode request = JsonUtils.createObjectNode(nodes -> {
                nodes.put("bucket", bucket);
                nodes.put("key", key);
                nodes.set("edits", edits);
            });
            String json = JsonUtils.toString(request);
            return api + "/" + ENCODER.encodeToString(json.getBytes());
        }
    }
}
