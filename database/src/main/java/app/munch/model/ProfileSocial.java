package app.munch.model;

import app.munch.model.constraint.TagDefaultGroup;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ConflictException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.postgres.PojoUserType;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 7:27 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "ProfileSocial")
@TypeDef(name = "ProfileSocial.Secrets", typeClass = ProfileSocial.SecretsType.class)
public final class ProfileSocial {

    /**
     * Internal unique generated id
     */
    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    /**
     * External unique id, from the platform
     */
    @NotNull
    @Column(length = 512, updatable = false, nullable = false, unique = true)
    private String eid;

    @JsonIgnore
    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private Profile profile;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private ProfileSocialType type;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private ProfileSocialStatus status;

    @JsonIgnore
    @NotNull
    @Valid
    @Type(type = "ProfileSocial.Secrets")
    private Secrets secrets;

    @NotNull
    @Column(updatable = true, nullable = false, unique = false)
    private Date connectedAt;

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

    public void setEid(String sid) {
        this.eid = sid;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public ProfileSocialType getType() {
        return type;
    }

    public void setType(ProfileSocialType type) {
        this.type = type;
    }

    public Secrets getSecrets() {
        return secrets;
    }

    public void setSecrets(Secrets secrets) {
        this.secrets = secrets;
    }

    public ProfileSocialStatus getStatus() {
        return status;
    }

    public void setStatus(ProfileSocialStatus status) {
        this.status = status;
    }

    public Date getConnectedAt() {
        return connectedAt;
    }

    public void setConnectedAt(Date connectedAt) {
        this.connectedAt = connectedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static final class SecretsType extends PojoUserType<Secrets> {
        public SecretsType() {
            super(Secrets.class);
        }
    }

    /**
     * Comes with a few fields that are commonly used as secrets
     * Max length of 2048 is set for all fields.
     * - accessKey
     * - secretKey
     * - accessToken
     * - refreshToken
     * - jwt
     * - token
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Secrets {
        private static final String CURRENT_VERSION = "2019-09-10";

        @Length(max = 2048)
        private String accessKey;

        @Length(max = 2048)
        private String secretKey;

        @Length(max = 2048)
        private String refreshToken;

        @Length(max = 2048)
        private String accessToken;

        @Length(max = 2048)
        private String jwt;

        @Length(max = 2048)
        private String token;

        @NotNull
        @Pattern(regexp = "[0-9]{4}-[0-9]{2}-[0-9]{2}")
        private String version;

        public static String getCurrentVersion() {
            return CURRENT_VERSION;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getJwt() {
            return jwt;
        }

        public void setJwt(String jwt) {
            this.jwt = jwt;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }

    @PrePersist
    void prePersist() {
        long millis = System.currentTimeMillis();
        setUid(KeyUtils.nextULID(millis));
        setCreatedAt(new Timestamp(millis));

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        if (getSecrets() != null && getSecrets().getVersion() != null) {
            // Always check this version before write
            if (getSecrets().getVersion().compareTo(Secrets.CURRENT_VERSION) > 0) {
                throw new ConflictException("Editing of ProfileSocial.Secrets with outdated api version is not allowed.");
            }
        }

        if (getSecrets() != null) {
            getSecrets().setVersion(Secrets.CURRENT_VERSION);
        }

        setUpdatedAt(new Timestamp(System.currentTimeMillis()));
        ValidationException.validate(this, Default.class, TagDefaultGroup.class);
    }
}
