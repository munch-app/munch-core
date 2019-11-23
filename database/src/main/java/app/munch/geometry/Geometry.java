package app.munch.geometry;

import com.fasterxml.jackson.annotation.*;
import dev.fuxing.validator.ValidEnum;

import javax.validation.constraints.NotNull;

/**
 * GeoJSON compatible POJO.
 *
 * @author Fuxing Loh
 * @since 2019-11-23 at 05:05
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(Point.class),
        @JsonSubTypes.Type(Polygon.class),
        @JsonSubTypes.Type(MultiPolygon.class),
})
public interface Geometry<T> {

    @ValidEnum
    @JsonIgnore
    GeometryType getType();

    @NotNull
    T getCoordinates();
}
