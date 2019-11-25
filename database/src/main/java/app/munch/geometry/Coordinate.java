package app.munch.geometry;

import app.munch.geometry.annotation.ValidCoordinate;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 06:27
 */
@ValidCoordinate
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Double.compare(that.longitude, longitude) == 0 &&
                Double.compare(that.latitude, latitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude);
    }
}
