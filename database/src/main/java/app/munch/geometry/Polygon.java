package app.munch.geometry;

import app.munch.geometry.annotation.ValidPolygon;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 06:17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("polygon")
@ValidPolygon
public final class Polygon implements Geometry<List<List<Coordinate>>> {

    @NotNull
    private List<List<Coordinate>> coordinates;

    @Override
    public GeometryType getType() {
        return GeometryType.POLYGON;
    }

    @Valid
    @Override
    public List<List<Coordinate>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Coordinate>> coordinates) {
        this.coordinates = coordinates;
    }
}
