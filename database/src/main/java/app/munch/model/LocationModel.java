package app.munch.model;

import app.munch.geometry.Geometry;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import dev.fuxing.postgres.PojoSetUserType;
import dev.fuxing.postgres.PojoUserType;
import dev.fuxing.validator.ValidEnum;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;

/**
 * @author Fuxing Loh
 * @since 2019-11-22 at 13:33
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@MappedSuperclass
@TypeDef(name = "LocationModel.GeometryType", typeClass = LocationModel.GeometryType.class)
@TypeDef(name = "LocationModel.SynonymsType", typeClass = LocationModel.SynonymsType.class)
public abstract class LocationModel {

    @NotBlank
    @Length(max = 100)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private String name;

    @Length(max = 250)
    @Column(length = 250, updatable = true, nullable = true, unique = false)
    private String description;

    @Length(max = 100)
    @Column(length = 100, updatable = true, nullable = true, unique = false)
    private String postcode;

    @NotNull
    @Length(max = 255)
    @Column(length = 255, updatable = true, nullable = false, unique = false)
    private String address;

    @NotNull
    @Length(max = 255)
    @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
    @Column(length = 255, updatable = true, nullable = false, unique = false)
    private String latLng;

    @ValidEnum
    @Enumerated(EnumType.STRING)
    @Column(length = 100, updatable = true, nullable = false, unique = false)
    private LocationType type;

    @Valid
    @NotNull
    @Type(type = "LocationModel.GeometryType")
    private Geometry geometry;

    @Valid
    @NotNull
    @Size(max = 4)
    @Type(type = "LocationModel.SynonymsType")
    private Set<@NotBlank @Length(max = 100) String> synonyms;

    public LocationType getType() {
        return type;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Set<String> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(Set<String> synonyms) {
        this.synonyms = synonyms;
    }

    public static final class GeometryType extends PojoUserType<Geometry> {
        public GeometryType() {
            super(Geometry.class);
        }
    }

    public static final class SynonymsType extends PojoSetUserType<String> {
        public SynonymsType() {
            super(String.class);
        }
    }
}
