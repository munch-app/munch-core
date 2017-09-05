package munch.api.services.locations;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import munch.api.clients.NominatimClient;
import munch.api.services.AbstractService;
import munch.data.Location;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 5/9/17
 * Time: 5:44 PM
 * Project: munch-core
 */
@Singleton
public final class LocationSelector {
    private static final Location singapore = createSingapore();

    private final NominatimClient nominatimClient;

    @Inject
    public LocationSelector(NominatimClient nominatimClient) {
        this.nominatimClient = nominatimClient;
    }

    /**
     * if not latLngString is given Singapore Location Polygon will be used
     *
     * @param latLngString latitude, longitude in String
     * @return Singapore Location
     * @throws AbstractService.LatLng.ParseException unable to parse
     */
    public Location select(@Nullable String latLngString) throws AbstractService.LatLng.ParseException {
        if (latLngString == null) return singapore;

        AbstractService.LatLng latLng = new AbstractService.LatLng(latLngString);
        // Get StreetName and use as LocationName else use CurrentLocation
        String street = WordUtils.capitalizeFully(nominatimClient.getStreet(latLng));
        if (StringUtils.isBlank(street)) street = "Current Location";

        Location location = new Location();
        location.setName(street);
        location.setCenter(latLng.getString());
        location.setPoints(createPoints(latLng, 1500, 15));
        return location;
    }

    /**
     * @param latLng    center of the place
     * @param radius    radius of circle in meters
     * @param numPoints amounts of point in polygon
     * @return List of Points for the polygon
     */
    private static List<String> createPoints(AbstractService.LatLng latLng, double radius, int numPoints) {
        final double radiusInDegrees = radius / 111000f;
        final Coordinate coordinate = new Coordinate(latLng.getLng(), latLng.getLat());

        // Create circle shape
        GeometricShapeFactory shape = new GeometricShapeFactory();
        shape.setCentre(coordinate);
        shape.setSize(radiusInDegrees);
        shape.setNumPoints(numPoints);

        // Create polygon
        Polygon polygon = shape.createCircle();
        polygon.setSRID(4326);

        // Map to points from polygon created
        return Arrays.stream(polygon.getCoordinates())
                .map(coord -> coord.y + "," + coord.x)
                .collect(Collectors.toList());
    }

    /**
     * @return Singapore Default Location
     */
    private static Location createSingapore() {
        Location location = new Location();
        location.setName("Singapore");
        location.setId("special_singapore");
        location.setCenter("1.3521,103.8198");
        return location;
    }
}
