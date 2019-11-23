package app.munch.geometry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author Fuxing Loh
 * @since 2019-11-23 at 06:18
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeName("multipolygon")
public final class MultiPolygon implements Geometry<List<List<List<Coordinate>>>> {

    @Valid
    @NotNull
    private List<List<List<Coordinate>>> coordinates;

    @Override
    public GeometryType getType() {
        return GeometryType.MULTIPOLYGON;
    }

    @Override
    public List<List<List<Coordinate>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<Coordinate>>> coordinates) {
        this.coordinates = coordinates;
    }
}
