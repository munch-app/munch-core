package app.munch.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 8/9/19
 * Time: 11:25 pm
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class ElasticDocument {

    /**
     * Elastic type that is being indexed.
     */
    @ValidEnum
    private ElasticDocumentType type;

    /**
     * Elastic unique key with type information.
     * E.g. PLACE_1234567890100
     * E.g. LOCATION_1234567890302
     * <p>
     * This field is not auto field because it don't know whether id or uid is the unique key
     */
    @Length(max = 255)
    @NotNull
    private String key;

    /**
     * 'id' and 'uid' both exists because different data type use different key type.
     */
    @Length(max = 64)
    private String id;

    /**
     * 'id' and 'uid' both exists because different data type use different key type.
     */
    @Length(max = 200)
    private String uid;

    @Length(max = 255)
    private String slug;

    @Length(max = 100)
    private String name;

    @Length(max = 100)
    private String phone;

    @Length(max = 1000)
    private String website;

    @Length(max = 250)
    private String description;

    @Valid
    private Image image;

    private Double important;

    private Set<@NotBlank String> synonyms;

    @Valid
    private Set<@NotNull Tag> tags;

    @Valid
    private Set<@NotNull Hour> hours;

    @Valid
    private Status status;

    private Location location;

    private Price price;

    private Date publishedAt;

    private Date updatedAt;

    private Date createdAt;

    /**
     * Suggest is a special object created and used in ElasticDocument only
     */
    @Valid
    private Suggest suggest;

    /**
     * Timings is a special object created and used in ElasticDocument only
     */
    @Valid
    private Timings timings;

    public ElasticDocumentType getType() {
        return type;
    }

    public void setType(ElasticDocumentType type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Double getImportant() {
        return important;
    }

    public void setImportant(Double important) {
        this.important = important;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Date getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Date publishedAt) {
        this.publishedAt = publishedAt;
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

    public Suggest getSuggest() {
        return suggest;
    }

    public void setSuggest(Suggest suggest) {
        this.suggest = suggest;
    }

    public Timings getTimings() {
        return timings;
    }

    public void setTimings(Timings timings) {
        this.timings = timings;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Location {
        @Length(max = 100)
        private String unitNumber;

        @Length(max = 100)
        private String postcode;

        @Length(max = 255)
        private String address;

        @Length(max = 255)
        @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
        private String latLng;

        public String getUnitNumber() {
            return unitNumber;
        }

        public void setUnitNumber(String unitNumber) {
            this.unitNumber = unitNumber;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Suggest {

        private Set<String> input;

        @Valid
        private Contexts contexts;

        public Set<String> getInput() {
            return input;
        }

        public void setInput(Set<String> input) {
            this.input = input;
        }

        public Contexts getContexts() {
            return contexts;
        }

        public void setContexts(Contexts contexts) {
            this.contexts = contexts;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Contexts {

            @Length(max = 255)
            @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
            private String latLng;

            @ValidEnum
            private ElasticDocumentType type;

            public String getLatLng() {
                return latLng;
            }

            public void setLatLng(String latLng) {
                this.latLng = latLng;
            }

            public ElasticDocumentType getType() {
                return type;
            }

            public void setType(ElasticDocumentType type) {
                this.type = type;
            }
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Timings {

        @Valid
        private Set<@NotNull Range> mon;

        @Valid
        private Set<@NotNull Range> tue;

        @Valid
        private Set<@NotNull Range> wed;

        @Valid
        private Set<@NotNull Range> thu;

        @Valid
        private Set<@NotNull Range> fri;

        @Valid
        private Set<@NotNull Range> sat;

        @Valid
        private Set<@NotNull Range> sun;

        public Set<Range> getMon() {
            return mon;
        }

        public void setMon(Set<Range> mon) {
            this.mon = mon;
        }

        public Set<Range> getTue() {
            return tue;
        }

        public void setTue(Set<Range> tue) {
            this.tue = tue;
        }

        public Set<Range> getWed() {
            return wed;
        }

        public void setWed(Set<Range> wed) {
            this.wed = wed;
        }

        public Set<Range> getThu() {
            return thu;
        }

        public void setThu(Set<Range> thu) {
            this.thu = thu;
        }

        public Set<Range> getFri() {
            return fri;
        }

        public void setFri(Set<Range> fri) {
            this.fri = fri;
        }

        public Set<Range> getSat() {
            return sat;
        }

        public void setSat(Set<Range> sat) {
            this.sat = sat;
        }

        public Set<Range> getSun() {
            return sun;
        }

        public void setSun(Set<Range> sun) {
            this.sun = sun;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static final class Range {

            @Min(0)
            @Max(1440)
            private Integer gte;

            @Min(0)
            @Max(1440)
            private Integer lte;

            public Integer getGte() {
                return gte;
            }

            public void setGte(Integer gte) {
                this.gte = gte;
            }

            public Integer getLte() {
                return lte;
            }

            public void setLte(Integer lte) {
                this.lte = lte;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Range range = (Range) o;
                return Objects.equals(gte, range.gte) &&
                        Objects.equals(lte, range.lte);
            }

            @Override
            public int hashCode() {
                return Objects.hash(gte, lte);
            }
        }
    }
}
