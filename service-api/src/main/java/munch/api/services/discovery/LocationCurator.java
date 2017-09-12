package munch.api.services.discovery;

import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import munch.api.services.AbstractService;
import munch.api.services.CachedService;
import munch.data.Location;
import munch.data.SearchQuery;
import munch.data.SearchResult;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 1/7/2017
 * Time: 11:08 PM
 * Project: munch-core
 */
@Singleton
public final class LocationCurator extends TabCurator {

    /**
     * @param resource cached StaticJson resource tool
     * @return Set of Tag that can be Tag curated, all in uppercase
     * @throws IOException IOException
     */
    private static Set<String> collectTabTags(CachedService.StaticJson resource) throws IOException {
        Set<String> tags = new HashSet<>();
        resource.getResource("tab-tags.json").fields().forEachRemaining(entry -> {
            entry.getValue().forEach(tag -> tags.add(tag.asText()));
        });
        return tags;
    }

    @Inject
    public LocationCurator(CachedService.StaticJson resource) throws IOException {
        super(5, 1, collectTabTags(resource));
    }

    @Override
    public boolean match(SearchQuery query) {
        if (StringUtils.isNotBlank(query.getLatLng())) return true;

        // Contains polygonal location data
        if (query.getLocation() != null) {
            // Must has 3 points
            if (query.getLocation().getPoints() == null) return false;
            if (query.getLocation().getPoints().size() >= 3) return true;
        }

        // Else fail
        return false;
    }

    @Override
    public List<SearchResult> query(SearchQuery query) {
        query = clone(query);

        // Create LocationPolygon if only latLng is given
        if (query.getLocation() == null) {
            AbstractService.LatLng latLng = new AbstractService.LatLng(query.getLatLng());

            Location location = new Location();
            location.setName("Current Location");
            location.setCenter(latLng.getString());
            location.setPoints(createPoints(latLng, 1500, 15));
            query.setLocation(location);
        }

        query.setFrom(0);
        query.setSize(60);
        return searchClient.search(query);
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
}
