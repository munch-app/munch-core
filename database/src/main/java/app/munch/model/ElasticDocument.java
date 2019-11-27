package app.munch.model;

import app.munch.geometry.Geometry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.fuxing.validator.ValidEnum;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

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
     * Persisted at '_type', as all elastic variable will be prefixed with '_'
     */
    @ValidEnum
    @JsonProperty("_type")
    private ElasticDocumentType elasticType;

    /**
     * Elastic unique key with type information.
     * E.g. PLACE_1234567890100
     * E.g. LOCATION_1234567890302
     * <p>
     * This field is not an auto generated field because it don't know whether id or uid is the unique key
     * Persisted at '_key', as all elastic variable will be prefixed with '_'
     */
    @NotNull
    @Length(max = 255)
    @JsonProperty("_key")
    private String elasticKey;

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

    @Length(max = 100)
    private String type;

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

    @Valid
    private Profile profile;

    @Valid
    private Location location;

    @Valid
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

    public ElasticDocument() {
    }

    public ElasticDocument(ElasticDocumentType type, Consumer<KeyBuilder> consumer) {
        setElasticType(type);
        setElasticKey(consumer);
    }

    public ElasticDocumentType getElasticType() {
        return elasticType;
    }

    public void setElasticType(ElasticDocumentType elasticType) {
        this.elasticType = elasticType;
    }

    public String getElasticKey() {
        return elasticKey;
    }

    public void setElasticKey(String elasticKey) {
        this.elasticKey = elasticKey;
    }

    public void setElasticKey(Consumer<KeyBuilder> consumer) {
        KeyBuilder builder = new KeyBuilder();
        consumer.accept(builder);
        setElasticKey(builder.build());
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
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

    public void setSuggest(Consumer<SuggestBuilder> consumer) {
        SuggestBuilder builder = new SuggestBuilder();
        consumer.accept(builder);
        setSuggest(builder.build());
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

        /**
         * Support latitude longitude geo_point only
         */
        @Length(max = 255)
        @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
        private String latLng;

        /**
         * Supports 3 types of geo types: geo_shape
         *
         * @see app.munch.geometry.Point
         * @see app.munch.geometry.Polygon
         * @see app.munch.geometry.MultiPolygon
         */
        @Valid
        private Geometry geometry;

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

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
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

    /**
     * Key builder
     */
    public static final class KeyBuilder {
        private ElasticDocumentType type;
        private String id;
        private String uid;

        public KeyBuilder type(ElasticDocumentType type) {
            this.type = type;
            return this;
        }

        public KeyBuilder id(String id) {
            this.id = id;
            return this;
        }

        public KeyBuilder uid(String uid) {
            this.uid = uid;
            return this;
        }

        public String build() {
            Objects.requireNonNull(type);
            if (type.toString().equals("null")) {
                throw new IllegalStateException("ElasticDocumentType cannot be null.");
            }

            if (StringUtils.isNoneBlank(uid, id)) {
                return id + "_" + uid + "_" + type.toString();
            }

            if (uid != null) {
                return uid + "_" + type.toString();
            }

            if (id != null) {
                return id + "_" + type.toString();
            }

            throw new IllegalStateException("id or uid is required to create elastic key.");
        }
    }

    /**
     * Suggest Builder
     */
    public static final class SuggestBuilder {
        private ElasticDocumentType type;
        private String latLng;
        private Set<String> inputs = new HashSet<>();

        public SuggestBuilder type(ElasticDocumentType type) {
            this.type = type;
            return this;
        }

        public SuggestBuilder latLng(String latLng) {
            this.latLng = latLng;
            return this;
        }

        public SuggestBuilder input(String input) {
            this.inputs.add(StringUtils.lowerCase(input));
            return this;
        }

        public SuggestBuilder inputs(Set<String> inputs) {
            inputs.stream()
                    .map(StringUtils::lowerCase)
                    .forEach(s -> this.inputs.add(s));
            return this;
        }

        Suggest build() {
            Objects.requireNonNull(type);

            Suggest.Contexts contexts = new Suggest.Contexts();
            contexts.setType(type);
            contexts.setLatLng(latLng);

            Suggest suggest = new Suggest();
            suggest.setInput(inputs);
            suggest.setContexts(contexts);
            return suggest;
        }
    }
}
