package app.munch.model;

import app.munch.model.constraint.ArticlePublishedGroup;
import app.munch.model.constraint.PlaceDefaultGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.postgres.PojoSetUserType;
import dev.fuxing.postgres.PojoUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

/**
 * @see ArticleModel for reasons why it's design like this.
 * <p>
 * Created by: Fuxing
 * Date: 2019-07-31
 * Time: 15:22
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@MappedSuperclass
@TypeDef(name = "PlaceModel.PriceType", typeClass = PlaceModel.PriceType.class)
@TypeDef(name = "PlaceModel.StatusType", typeClass = PlaceModel.StatusType.class)
@TypeDef(name = "PlaceModel.LocationType", typeClass = PlaceModel.LocationType.class)
@TypeDef(name = "PlaceModel.SynonymsType", typeClass = PlaceModel.SynonymsType.class)
@TypeDef(name = "PlaceModel.HoursType", typeClass = PlaceModel.HoursType.class)
@TypeDef(name = "PlaceModel.TagsType", typeClass = PlaceModel.TagsType.class)
public abstract class PlaceModel {
    // Add Brand, Menu

    @NotBlank(groups = {PlaceDefaultGroup.class, ArticlePublishedGroup.class})
    @Size(max = 100)
    @Column(length = 100, updatable = true, nullable = true, unique = false)
    private String name;

    @Size(max = 100)
    @Column(length = 100, updatable = true, nullable = true, unique = false)
    private String phone;

    @URL(groups = {PlaceDefaultGroup.class, ArticlePublishedGroup.class})
    @Size(max = 1000)
    @Column(length = 1000, updatable = true, nullable = true, unique = false)
    private String website;

    @Size(max = 250)
    @Column(length = 250, updatable = true, nullable = true, unique = false)
    private String description;

    @Valid
    @Type(type = "PlaceModel.PriceType")
    private Price price;

    @Valid
    @NotNull
    @Type(type = "PlaceModel.LocationType")
    private Location location;

    @Valid
    @NotNull
    @Type(type = "PlaceModel.StatusType")
    private Status status;

    @Valid
    @NotNull
    @Size(max = 4)
    @Type(type = "PlaceModel.SynonymsType")
    private Set<@NotNull @Length(min = 0, max = 100) String> synonyms;

    /**
     * Regardless of Groups, validation is default because data is retrieved from external source
     */
    @Valid
    @NotNull
    @Size(max = 12)
    @Type(type = "PlaceModel.TagsType")
    private Set<@NotNull Tag> tags;

    /**
     * Regardless of Groups, validation is default because data is retrieved from external source
     */
    @Valid
    @NotNull
    @Size(max = 24)
    @Type(type = "PlaceModel.HoursType")
    private Set<@NotNull Hour> hours;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        this.synonyms = synonyms;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<Hour> getHours() {
        return hours;
    }

    public void setHours(Set<Hour> hours) {
        this.hours = hours;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Location {
        // Might need to auto fill country/city

        private String unitNumber;

        private String postcode;

        @NotNull(groups = {PlaceDefaultGroup.class, ArticlePublishedGroup.class})
        private String address;

        @NotNull(groups = {PlaceDefaultGroup.class, ArticlePublishedGroup.class})
        @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
        private String latLng;

        public String getUnitNumber() {
            return unitNumber;
        }

        public void setUnitNumber(String unitNumber) {
            this.unitNumber = unitNumber;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getLatLng() {
            return latLng;
        }

        public void setLatLng(String latLng) {
            this.latLng = latLng;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Price {

        @Min(0)
        private BigDecimal perPax;

        public BigDecimal getPerPax() {
            return perPax;
        }

        public void setPerPax(BigDecimal perPax) {
            this.perPax = perPax;
        }
    }

    public static final class LocationType extends PojoUserType<Location> {
        public LocationType() {
            super(Location.class);
        }
    }

    public static final class PriceType extends PojoUserType<Price> {
        public PriceType() {
            super(Price.class);
        }
    }

    public static final class StatusType extends PojoUserType<Status> {
        public StatusType() {
            super(Status.class);
        }
    }

    public static final class SynonymsType extends PojoSetUserType<String> {
        public SynonymsType() {
            super(String.class);
        }
    }

    public static final class HoursType extends PojoSetUserType<Hour> {
        public HoursType() {
            super(Hour.class);
        }
    }

    public static final class TagsType extends PojoSetUserType<Tag> {
        public TagsType() {
            super(Tag.class);
        }
    }
}
