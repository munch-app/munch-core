package munch.location.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import org.hibernate.search.annotations.Indexed;

import javax.persistence.*;
import java.util.List;

/**
 * Region is bounding boxes technically
 * Only query place for actual results
 * <p>
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 6:06 PM
 * Project: munch-core
 */
@Indexed
@Entity(name = "Region")
public final class Region {

    private String code;
    private String name;
    private Geometry geometry;

    // Mapped by places
    private List<Location> locations;
    // TODO return single polygon

    /**
     * Subzone code
     *
     * @return unique string id of place
     */
    @Id
    @Column(length = 255, updatable = false, nullable = false)
    @JsonIgnore
    public String getCode() {
        return code;
    }

    public void setCode(String name) {
        this.code = name;
    }

    /**
     * @return name of subzone
     */
    @Column(length = 255, updatable = false, nullable = false)
    @JsonIgnore
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return single polygon of place
     */
    @Column(updatable = false, nullable = false)
    @JsonIgnore
    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    /**
     * Cannot be empty
     *
     * @return multi linked region of place
     */
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "regions")
    @OrderBy("sort ASC")
    @JsonIgnore
    public List<Location> getLocations() {
        return locations;
    }

    protected void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Region{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", geometry=" + geometry +
                '}';
    }
}
