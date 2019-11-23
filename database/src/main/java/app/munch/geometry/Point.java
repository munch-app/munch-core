package app.munch.geometry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 06:17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("point")
public final class Point implements Geometry<Coordinate> {

    @NotNull
    private Coordinate coordinates;

    @Override
    public GeometryType getType() {
        return GeometryType.POINT;
    }

    @Valid
    @Override
    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }
}
