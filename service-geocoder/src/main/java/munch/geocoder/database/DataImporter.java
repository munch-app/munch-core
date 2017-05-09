package munch.geocoder.database;

import com.google.common.io.Resources;
import com.munch.hibernate.utils.TransactionProvider;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import org.apache.commons.lang3.StringUtils;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Import places into geo database for querying
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 3:12 PM
 * Project: munch-core
 */
@Singleton
public class DataImporter {

    private final TransactionProvider provider;

    @Inject
    public DataImporter(TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * Subzones Originally uses 3414 SVY21/ Singapore TM
     * But is changed to EPSG: 4326
     */
    public void importSubzone() throws IOException {
        // Load feature collection
        URL url = Resources.getResource("subzone.json");
        String json = Resources.toString(url, Charset.forName("UTF-8"));
        FeatureCollection collection = (FeatureCollection) GeoJSONFactory.create(json);
        GeoJSONReader reader = new GeoJSONReader();

        // Map all to regions
        List<Region> regions = new ArrayList<>();
        for (Feature feature : collection.getFeatures()) {
            Region region = new Region();
            region.setCode((String) feature.getProperties().get("SUBZONE_C"));
            region.setName((String) feature.getProperties().get("SUBZONE_N"));
            region.setGeometry(reader.read(feature.getGeometry()));
            regions.add(region);
        }

        // Persist to database
        provider.with(em -> {
            for (Region region : regions) {
                em.persist(region);
            }
        });
    }

    /**
     * EPSG: 4326
     * Distance is in degree
     * http://stackoverflow.com/questions/8464666/distance-between-2-points-in-postgis-in-srid-4326-in-metres
     */
    public void importMrt() throws IOException {
        URL url = Resources.getResource("mrt.csv");
        String csv = Resources.toString(url, Charset.forName("UTF-8"));

        provider.with(em -> {
            for (String line : csv.split("\n")) {
                String[] split = line.split(",");
                String name = split[0].trim();

                // Save region first
                Region region = new Region();
                region.setName(name);
                region.setCode(name);

                // Generate 10 point polygon with 1km wide
                GeometricShapeFactory shape = new GeometricShapeFactory();
                shape.setCentre(new Coordinate(Double.parseDouble(split[2]), Double.parseDouble(split[1])));
                shape.setSize(0.009);
                shape.setNumPoints(20);
                Polygon polygon = shape.createCircle();
                polygon.setSRID(4326);
                region.setGeometry(polygon);
                em.persist(region);

                // Then save place
                Neighborhood neighborhood = new Neighborhood();
                neighborhood.setName(name);
                neighborhood.setSort(1);
                neighborhood.setRegions(new HashSet<>(Collections.singleton(region)));
                em.persist(neighborhood);
            }
        });
    }

    public void importPlace() throws IOException {
        URL url = Resources.getResource("place.csv");
        String csv = Resources.toString(url, Charset.forName("UTF-8"));

        provider.with(em -> {
            for (String line : csv.split("\n")) {
                String[] split = line.split(",");
                List<String> codes = Arrays.stream(Arrays.copyOfRange(split, 1, split.length))
                        .map(StringUtils::trim).collect(Collectors.toList());

                // Saving place
                Neighborhood neighborhood = new Neighborhood();
                neighborhood.setName(split[0].trim());
                neighborhood.setSort(0);
                List<Region> list = em.createQuery("SELECT r FROM Region r WHERE r.code IN :codes", Region.class)
                        .setParameter("codes", codes).getResultList();
                neighborhood.setRegions(new HashSet<>(list));
                em.persist(neighborhood);
            }
        });
    }
}
