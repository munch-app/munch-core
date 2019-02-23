package munch.api.feed;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.annotation.Nullable;

/**
 * Created by: Fuxing
 * Date: 2019-02-23
 * Time: 16:56
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class FeedQuery {

    /**
     * Versions History:
     * 2019-02-23
     * - Initial
     */
    public static final String VERSION = "2019-02-23";

    private Location location;

    @Nullable
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Location {
        private String latLng;

        @Nullable
        public String getLatLng() {
            return latLng;
        }

        public void setLatLng(String latLng) {
            this.latLng = latLng;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "latLng='" + latLng + '\'' +
                    '}';
        }
    }

    /**
     * @return version of SearchQuery
     */
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String toString() {
        return "FeedQuery{" +
                "location=" + location +
                '}';
    }
}
