package munch.api.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import munch.api.clients.NominatimClient;
import munch.api.clients.SearchClient;
import munch.api.services.discovery.CuratorDelegator;
import munch.data.Location;
import munch.data.SearchCollection;
import munch.data.SearchQuery;
import munch.restful.server.JsonCall;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 12/7/2017
 * Time: 12:54 PM
 * Project: munch-core
 */
@Singleton
public class DiscoveryService extends AbstractService {

    private final SearchClient searchClient;
    private final CuratorDelegator curatorDelegator;
    private final NominatimClient nominatimClient;

    @Inject
    public DiscoveryService(SearchClient searchClient, CuratorDelegator curatorDelegator,
                            NominatimClient nominatimClient) {
        this.searchClient = searchClient;
        this.curatorDelegator = curatorDelegator;
        this.nominatimClient = nominatimClient;
    }

    @Override
    public void route() {
        PATH("/discovery", () -> {
            GET("/locations/reverse", this::locationReverse);
            POST("/suggest", this::suggest);

            POST("/search", this::search);
            POST("/search/next", this::searchNext);
        });
    }

    /**
     * @param call json call
     * @return generated current Location of user
     */
    private Location locationReverse(JsonCall call) {
        LatLng latLng = parseLatLng(call.queryString("latLng"));
        String street = nominatimClient.getStreet(latLng);
        street = WordUtils.capitalizeFully(street);
        if (StringUtils.isBlank(street)) street = "Current Location";

        Location location = new Location();
        location.setName(street);
        location.setCenter(latLng.getString());
        location.setPoints(createPoints(latLng, 1500, 15));
        return location;
    }

    /**
     * <pre>
     *  {
     *      size: 10,
     *      text: "",
     *      latLng: "" // Optional
     *  }
     * </pre>
     *
     * @param call json call
     * @return list of Place result
     */
    private JsonNode suggest(JsonCall call, JsonNode request) {
        int size = request.get("size").asInt();
        String text = request.get("text").asText();
        return searchClient.suggestRaw(size, text, null);
    }

    /**
     * @param call json call
     * @return list of Place result
     */
    private List<SearchCollection> search(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        return curatorDelegator.delegate(query);
    }

    /**
     * @param call json call
     * @return list of Place
     * @see SearchQuery
     */
    private JsonNode searchNext(JsonCall call) {
        SearchQuery query = call.bodyAsObject(SearchQuery.class);
        JsonNode data = searchClient.searchRaw(query);
        return nodes(200, data);
    }

    /**
     * @param latLng    center of the place
     * @param radius    radius of circle in meters
     * @param numPoints amounts of point in polygon
     * @return List of Points for the polygon
     */
    private static List<String> createPoints(LatLng latLng, double radius, int numPoints) {
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
