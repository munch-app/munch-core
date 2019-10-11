package app.munch.model;

import app.munch.model.annotation.ValidAffiliateStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.KeyUtils;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
@TypeDef(name = "PlaceStruct", typeClass = PlaceStruct.UserType.class)
@ValidAffiliateStatus
public final class Affiliate {

    @NotNull
    @Pattern(regexp = KeyUtils.ULID_REGEX)
    @Id
    @Column(length = 26, updatable = false, nullable = false, unique = true)
    private String uid;

    @NotNull
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = false)
    private AffiliateBrand brand;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private AffiliateType type;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private AffiliateStatus status;

    /**
     * Note: why there is Place: place & PlaceAffiliate: linked
     * <p>
     * This Place association is for house keeping purpose, and indicates when affiliate is linked even if it's linked historically.
     */
    @JsonIgnore
    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private Place place;

    /**
     * Note: why there is Place: place & PlaceAffiliate: linked
     * <p>
     * Linked is for operational purpose, this is used to show user places with affiliates
     */
    @Valid
    @OneToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER, optional = true, orphanRemoval = true, mappedBy = "affiliate")
    private PlaceAffiliate linked;

    @Valid
    @NotNull
    @Type(type = "PlaceStruct")
    private PlaceStruct placeStruct;

    @ManyToOne(cascade = {}, fetch = FetchType.EAGER, optional = true)
    private Profile editedBy;

    @URL
    @NotNull
    @Length(max = 2048)
    @Column(length = 2048, updatable = true, nullable = true, unique = false)
    private String url;

    /**
     * Regardless, group is used for easy look up
     */
    @NotNull
    @Pattern(regexp = "^[a-z0-9.]{1,100}$")
    @Column(length = 100, updatable = false, nullable = false, unique = false)
    private String source;

    /**
     * Note: This is similar to 'eid', can be renamed to such
     *
     * <p>
     * One field used for uniqueness indexing
     * www.munch.app_base64+/==
     */
    @NotNull
    @Pattern(regexp = "^[a-z0-9.]{1,100}_[a-zA-Z0-9+/=]{1,1948}$")
    @Column(length = 2048, updatable = false, nullable = false, unique = true)
    private String sourceKey;

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY, optional = true)
    private ChangeGroup changeGroup;

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

    private void setUid(String uid) {
        this.uid = uid;
    }

    public AffiliateBrand getBrand() {
        return brand;
    }

    public void setBrand(AffiliateBrand brand) {
        this.brand = brand;
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

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public PlaceAffiliate getLinked() {
        return linked;
    }

    public void setLinked(PlaceAffiliate linked) {
        this.linked = linked;
    }

    public PlaceStruct getPlaceStruct() {
        return placeStruct;
    }

    public void setPlaceStruct(PlaceStruct placeStruct) {
        this.placeStruct = placeStruct;
    }

    public Profile getEditedBy() {
        return editedBy;
    }

    public void setEditedBy(Profile editedBy) {
        this.editedBy = editedBy;
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

    private void setSource(String source) {
        this.source = source;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    private void setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
    }

    public void setSourceKey(String source, String key) {
        setSource(source);
        setSourceKey(source + "_" + key);
    }

    public ChangeGroup getChangeGroup() {
        return changeGroup;
    }

    public void setChangeGroup(ChangeGroup changeGroup) {
        this.changeGroup = changeGroup;
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

    private void setCreatedAt(Date createdAt) {
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

        if (getLinked() != null) {
            copy(this, getLinked());
        }

        ValidationException.validate(this, Default.class);
    }

    static void copy(Affiliate affiliate, PlaceAffiliate linked) {
        linked.setUid(affiliate.getUid());
        linked.setAffiliate(affiliate);
        linked.setBrand(affiliate.getBrand());
        linked.setType(affiliate.getType());
        linked.setUrl(affiliate.getUrl());

        // Place copy from Linked.place
        affiliate.setPlace(linked.getPlace());
    }
}
