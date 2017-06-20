package munch.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Location POJO is marked out for future development
 * <p>
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Location {
    private String name;
    private String center;
    private String[] points;

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
     * @return as "lat, lng"
     */
    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    /**
     * @return as ["lat, lng", "lat, lng"]
     */
    public String[] getPoints() {
        return points;
    }

    public void setPoints(String[] points) {
        this.points = points;
    }
}
