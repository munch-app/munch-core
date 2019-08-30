package app.munch.model;

import app.munch.model.constraint.AffiliateDeletedGroup;
import app.munch.model.constraint.AffiliateLinkedGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 28/8/19
 * Time: 6:21 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "Affiliate")
public final class Affiliate {

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    private AffiliateType type;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    private AffiliateStatus status;

    @Valid
    @NotNull(groups = {AffiliateLinkedGroup.class})
    @Null(groups = {AffiliateDeletedGroup.class})
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, optional = true, orphanRemoval = true, mappedBy = "affiliate")
    private PlaceAffiliate place;

    // TODO(fuxing): Identifying information
    // TODO(fuxing): Json Meta

    @URL
    @NotNull
    @Size(max = 2048)
    @Column(length = 2048, updatable = true, nullable = true, unique = false)
    private String url;

    @NotNull
    @Pattern(regexp = "^[a-z0-9.]{1,100}$")
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private String source;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9+/=]{1,2048}$")
    @Column(length = 2048, updatable = false, nullable = false, unique = true)
    private String sourceKey;

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

    public AffiliateType getType() {
        return type;
    }

    public void setType(AffiliateType type) {
        this.type = type;
    }

    public AffiliateStatus getStatus() {
        return status;
    }

    public void setStatus(AffiliateStatus status) {
        this.status = status;
    }

    public PlaceAffiliate getPlace() {
        return place;
    }

    public void setPlace(PlaceAffiliate place) {
        this.place = place;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
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

        preUpdate();
    }

    @PreUpdate
    void preUpdate() {
        setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        if (getPlace() != null) {
            getPlace().setUid(getUid());
            getPlace().setAffiliate(this);
            getPlace().setType(getType());
            getPlace().setUrl(getUrl());
        }

        if (getStatus() != null) {
            switch (getStatus()) {
                case LINKED:
                    ValidationException.validate(this, Default.class, AffiliateLinkedGroup.class);
                    break;

                case DELETED_MUNCH:
                case DELETED_SOURCE:
                    ValidationException.validate(this, Default.class, AffiliateDeletedGroup.class);
                    break;
            }
        }
    }
}
