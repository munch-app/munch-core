package app.munch.geometry;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 06:27
 */
public final class Coordinate {

    private final double longitude;
    private final double latitude;

    public Coordinate(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @JsonValue
    public double[] toJson() {
        return new double[]{longitude, latitude};
    }

    @JsonCreator
    public static Coordinate fromJson(double[] values) {
        return new Coordinate(values[0], values[1]);
    }
}
