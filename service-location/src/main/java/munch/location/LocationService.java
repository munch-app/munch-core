package munch.location;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.munch.hibernate.utils.TransactionProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import munch.location.database.Location;
import munch.location.database.Region;
import munch.location.reader.FeatureReader;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.wololo.geojson.Feature;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONWriter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 2:56 PM
 * Project: munch-core
 */
@Singleton
public class LocationService implements JsonService {
    private final TransactionProvider provider;
    private final GeometryFactory factory = new GeometryFactory();

    private Map<String, JsonNode> cache = new HashMap<>();

    /**
     * @param featureReader data to import to geocoding services
     * @param provider     run database queries
     * @throws IOException import failure
     */
    @Inject
    public LocationService(TransactionProvider provider, FeatureReader featureReader) throws IOException {
        this.provider = provider;
        featureReader.importSubzone();
        featureReader.importMrt();
        featureReader.importPlace();
    }

    @Override
    public void route() {
        GET("/geocode", this::geocode);
        GET("/search", this::search);
        GET("/reverse", this::reverse);
    }

    /**
     * query string must contain both lat & lng
     * if any is missing code: 400
     * <p>
     * geocode reverse geocoding, converting latLng to place
     *
     * @param call json call
     * @return code 200: Place if exist
     * code 404: if none found
     */
    private JsonNode reverse(JsonCall call) {
        double lat = call.queryDouble("lat");
        double lng = call.queryDouble("lng");
        Point point = factory.createPoint(new Coordinate(lng, lat));

        return provider.reduce(em -> em.createQuery("SELECT r FROM Region r WHERE within(:point, r.geometry) = true", Region.class)
                .setParameter("point", point)
                .getResultList())
                .stream()
                .map(Region::getLocations)
                .flatMap(List::stream)
                .max(Comparator.comparingLong(Location::getSort))
                .map(this::convert)
                .orElse(null);
    }

    /**
     * query string must contain text
     * if missing code: 400
     * <p>
     * geocode search resolver, convert text to latLng
     *
     * @param call json call
     * @return code 200: Neighborhood if exist
     * code 404: if none found
     */
    private JsonNode geocode(JsonCall call) {
        String text = call.queryString("text").toLowerCase();
        return provider.optional(em -> em.createQuery("SELECT p FROM Location p " +
                "WHERE LOWER(p.name) = :name", Location.class)
                .setParameter("name", text)
                .getSingleResult())
                .map(this::convert)
                .orElse(null);
    }

    /**
     * query string must contain text
     * if missing code: 400
     * <p>
     * geocode auto complete service, convert text to latLng by suggesting
     * text must be size of 3 or above or will be rejected
     *
     * @param call json call
     * @return code 200: List of Locations
     * if none found empty data list
     */
    private List<String> search(JsonCall call) {
        String text = call.queryString("text").toLowerCase();
        if (text.length() < 3) return Collections.emptyList();

        List<Location> locations = provider.reduce(em -> {
            FullTextEntityManager fullText = Search.getFullTextEntityManager(em);

            QueryBuilder titleQB = fullText.getSearchFactory().buildQueryBuilder().forEntity(Location.class).get();
            Query query = titleQB.phrase().withSlop(2).onField("edgeNGramName")
                    .andField("nGramName").boostedTo(5)
                    .sentence(text).createQuery();

            FullTextQuery fullTextQuery = fullText.createFullTextQuery(query, Location.class);
            fullTextQuery.setMaxResults(20);

            //noinspection unchecked
            return fullTextQuery.getResultList();
        });
        if (locations.isEmpty()) return Collections.emptyList();

        return locations.stream().map(Location::getName).collect(Collectors.toList());
    }

    /**
     * Convert Location to json node
     *
     * @param location Location
     * @return converted json node
     */
    private JsonNode convert(Location location) {
        if (!cache.containsKey(location.getName())) {
            ObjectNode node = newNode();
            // Put name
            node.put("name", location.getName());

            // Put geo json
            GeoJSONWriter writer = new GeoJSONWriter();
            List<Feature> features = location.getRegions().stream()
                    .map(Region::getGeometry)
                    .map(geo -> new Feature(writer.write(geo), Collections.emptyMap()))
                    .collect(Collectors.toList());
            JsonNode geometryCenter = objectMapper.valueToTree(writer.write(features));
            node.set("geometry", geometryCenter);

            // If center exist, put a node for it
            if (location.getCenter() != null) {
                Geometry centerJson = writer.write(location.getCenter());
                JsonNode center = objectMapper.valueToTree(centerJson);
                node.set("center", center);
            }
            cache.put(location.getName(), node);
        }
        return cache.getOrDefault(location.getName(), null);
    }
}
