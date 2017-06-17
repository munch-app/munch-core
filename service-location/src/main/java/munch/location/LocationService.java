package munch.location;

import com.munch.hibernate.utils.TransactionProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
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

    /**
     * @param featureReader data to import to geocoding services
     * @param provider      run database queries
     * @throws IOException import failure
     */
    @Inject
    public LocationService(TransactionProvider provider) throws IOException {
        this.provider = provider;
    }

    @Override
    public void route() {
        GET("/reverse", this::reverse);
        GET("/geocode", this::geocode);
        GET("/search", this::search);
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
    private Location reverse(JsonCall call) {
        double lat = call.queryDouble("lat");
        double lng = call.queryDouble("lng");
        Point point = factory.createPoint(new Coordinate(lng, lat));

        return provider.optional(em -> em.createQuery("FROM Location " +
                "WHERE within(:point, geoPolygon) = true ORDER BY sort DESC", Location.class)
                .setParameter("point", point)
                .setMaxResults(1)
                .getSingleResult())
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
    private Location geocode(JsonCall call) {
        String text = call.queryString("text").toLowerCase();

        return provider.optional(em -> em.createQuery("FROM Location " +
                "WHERE LOWER(name) = :name", Location.class)
                .setParameter("name", text)
                .getSingleResult())
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

            QueryBuilder titleQB = fullText.getSearchFactory()
                    .buildQueryBuilder()
                    .forEntity(Location.class).get();
            Query query = titleQB.phrase().withSlop(2)
                    .onField("edgeNGramName")
                    .andField("nGramName").boostedTo(5)
                    .sentence(text).createQuery();

            FullTextQuery fullTextQuery = fullText.createFullTextQuery(query, Location.class);
            fullTextQuery.setMaxResults(20);

            //noinspection unchecked
            return fullTextQuery.getResultList();
        });
        if (locations.isEmpty()) return Collections.emptyList();

        // Map to name
        return locations.stream().map(Location::getName).collect(Collectors.toList());
    }
}
