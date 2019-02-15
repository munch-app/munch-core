package munch.api.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by: Fuxing
 * Date: 2019-02-15
 * Time: 21:57
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class NamedLocation {
    private String name;
    private String latLng;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    @Override
    public String toString() {
        return "NamedLocation{" +
                "name='" + name + '\'' +
                ", latLng='" + latLng + '\'' +
                '}';
    }
}
