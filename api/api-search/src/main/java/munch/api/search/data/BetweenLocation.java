package munch.api.search.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.data.location.Location;

/**
 * Location data used for Between more or less a holder of information
 * Not persisted.
 * <p>
 * Created by: Fuxing
 * Date: 25/9/18
 * Time: 1:12 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class BetweenLocation {
    private String name;
    private Location location;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "BetweenLocation{" +
                "name='" + name + '\'' +
                ", location=" + location +
                '}';
    }
}
