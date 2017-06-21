package munch.location.reader;

import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.simplify.TopologyPreservingSimplifier;
import munch.location.Location;
import org.apache.commons.lang3.StringUtils;
import org.wololo.geojson.Feature;
import org.wololo.geojson.FeatureCollection;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.jts2geojson.GeoJSONReader;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 15/6/2017
 * Time: 6:33 AM
 * Project: munch-core
 */
public final class SubzoneReader {
    private static final double margin = DegreeMetres.kmToDegree(0.005);

    private final GeoJSONReader reader = new GeoJSONReader();
    private final GeometryFactory factory = new GeometryFactory();

    public List<Location> read() throws IOException {
        Map<String, Geometry> regions = readRegions();
        List<Place> places = readPlaces();

        return places.stream().map(place -> {
            // Map all to polygons
            List<Geometry> polygons = place.codes.stream()
                    .map(regions::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());

            // Validate that all is polygon and has been mapped
            if (polygons.size() != place.codes.size()) {
                // List<Geometry> collect = place.codes.stream().map(regions::get).collect(Collectors.toList());
                throw new RuntimeException("Region code not mapped or not polygon.");
            }

            Geometry joined;

            if (polygons.size() > 1) {
                // Map all to multi polygon
                GeometryCollection collection = (GeometryCollection) factory.buildGeometry(polygons);

                // Increase polygon area
                Geometry buffered = collection.buffer(margin);

                // Union all polygon together into 1
                joined = buffered.union();
            } else {
                // Single polygon don't need join
                joined = polygons.get(0);
            }

            // Simplify total points
            Geometry result = TopologyPreservingSimplifier.simplify(joined, margin);

            // Create location set and return
            Location location = new Location();
            location.setName(place.name);
            if (result.getNumGeometries() > 1) {
                System.out.println(place.name);
                return null;
            }

            location.setGeometry((Polygon) result);
            Point centroid = result.getCentroid();
            location.setCenter(centroid.getY() + "," + centroid.getX());
            return location;
        }).collect(Collectors.toList());
    }

    private Map<String, Geometry> readRegions() throws IOException {
        // Load feature collection
        URL url = Resources.getResource("reader/subzone.json");
        String json = Resources.toString(url, Charset.forName("UTF-8"));
        FeatureCollection collection = (FeatureCollection) GeoJSONFactory.create(json);

        // Map all to String: Geometry
        Map<String, Geometry> map = new HashMap<>();
        for (Feature feature : collection.getFeatures()) {
            String code = (String) feature.getProperties().get("SUBZONE_C");
            map.put(code, reader.read(feature.getGeometry()));
        }
        return map;
    }

    private List<Place> readPlaces() throws IOException {
        URL url = Resources.getResource("reader/place.csv");
        String csv = Resources.toString(url, Charset.forName("UTF-8"));

        return Arrays.stream(csv.split("\n"))
                .map(line -> line.split(","))
                .map(lines -> {
                    List<String> codes = Arrays.stream(Arrays.copyOfRange(lines, 1, lines.length))
                            .map(StringUtils::trim).collect(Collectors.toList());

                    return new Place(lines[0].trim(), codes);
                }).collect(Collectors.toList());
    }

    private class Place {
        private final String name;
        private final List<String> codes;

        public Place(String name, List<String> codes) {
            this.name = name;
            this.codes = codes;
        }
    }
}
