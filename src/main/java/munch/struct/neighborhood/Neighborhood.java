package munch.struct.neighborhood;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.wololo.geojson.GeoJSON;
import org.wololo.geojson.Point;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Neighborhood {

    private String id;
    private String name;

    private Point center;
    private GeoJSON geometry;

    /**
     * TODO: not completed yet
     *
     * @return unique id of neighborhood
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name of neighborhood
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Center for like to put a pin
     *
     * @return center of geometry for visual purpose
     */
    public Point getCenter() {
        return center;
    }

    public void setCenter(Point center) {
        this.center = center;
    }

    /**
     * @return geometry of area
     */
    public GeoJSON getGeometry() {
        return geometry;
    }

    public void setGeometry(GeoJSON geometry) {
        this.geometry = geometry;
    }
}
