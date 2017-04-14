package munch.geocoder;

import com.munch.hibernate.utils.TransactionProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import munch.geocoder.database.DataImporter;
import munch.geocoder.database.Place;
import munch.geocoder.database.Region;
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
import java.util.Comparator;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 2:56 PM
 * Project: munch-core
 */
@Singleton
public class GeocoderService implements JsonService {
    private final TransactionProvider provider;
    private final GeometryFactory factory = new GeometryFactory();

    /**
     * @param dataImporter data to import to geocoding services
     * @param provider     run database queries
     * @throws IOException import failure
     */
    @Inject
    public GeocoderService(TransactionProvider provider, DataImporter dataImporter) throws IOException {
        this.provider = provider;
        dataImporter.importSubzone();
        dataImporter.importMrt();
        dataImporter.importPlace();
    }

    @Override
    public void route() {
        get("/geocode", this::search);
        get("/geocode/search", this::complete);
        get("/geocode/reverse", this::reverse);
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
    private Place reverse(JsonCall call) {
        double lat = call.queryDouble("lat");
        double lng = call.queryDouble("lng");
        Point point = factory.createPoint(new Coordinate(lng, lat));

        return provider.reduce(em -> em.createQuery("SELECT r FROM Region r WHERE within(:point, r.geometry) = true", Region.class)
                .setParameter("point", point)
                .getResultList())
                .stream()
                .map(Region::getPlaces)
                .flatMap(List::stream)
                .max(Comparator.comparingLong(Place::getSort))
                .orElse(null);
    }

    /**
     * query string must contain text
     * if missing code: 400
     * <p>
     * geocode search resolver, convert text to latLng
     *
     * @param call json call
     * @return code 200: Place if exist
     * code 404: if none found
     */
    private Place search(JsonCall call) {
        String text = call.queryString("text").toLowerCase();
        return provider.optional(em -> em.createQuery("SELECT p FROM Place p " +
                "WHERE LOWER(p.name) = :name", Place.class)
                .setParameter("name", text)
                .getSingleResult()).orElse(null);
    }

    /**
     * query string must contain text
     * if missing code: 400
     * <p>
     * geocode auto complete service, convert text to latLng by suggesting
     * text must be size of 3 or above or will be rejected
     *
     * @param call json call
     * @return code 200: List of places
     * if none found empty data list
     */
    private List<Place> complete(JsonCall call) {
        String text = call.queryString("text").toLowerCase();
        if (text.length() < 3) return Collections.emptyList();

        return provider.reduce(em -> {
            FullTextEntityManager fullText = Search.getFullTextEntityManager(em);

            QueryBuilder titleQB = fullText.getSearchFactory().buildQueryBuilder().forEntity(Place.class).get();
            Query query = titleQB.phrase().withSlop(2).onField("edgeNGramName")
                    .andField("nGramName").boostedTo(5)
                    .sentence(text).createQuery();

            FullTextQuery fullTextQuery = fullText.createFullTextQuery(query, Place.class);
            fullTextQuery.setMaxResults(20);

            //noinspection unchecked
            return fullTextQuery.getResultList();
        });
    }
}
