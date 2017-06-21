package munch.location.reader;

import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import munch.location.Location;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 15/6/2017
 * Time: 6:33 AM
 * Project: munch-core
 */
public final class MrtReader {
    private static final double radius = DegreeMetres.kmToDegree(1.0);

    /**
     * Read and return
     *
     * @return List of Location
     * @throws IOException exception
     */
    public List<Location> read() throws IOException {
        URL url = Resources.getResource("reader/mrt.csv");
        String csv = Resources.toString(url, Charset.forName("UTF-8"));

        return Arrays.stream(csv.split("\n"))
                .map(s -> s.split(","))
                .map(lines -> {
                    double lat = Double.parseDouble(lines[1]);
                    double lng = Double.parseDouble(lines[2]);
                    Polygon polygon = createCircle(lat, lng, radius, 10);

                    Location location = new Location();
                    location.setName(lines[0].trim());
                    location.setSort(1);
                    location.setGeometry(polygon);
                    location.setCenter(lat + "," + lng);
                    String[] points = Arrays.stream(polygon.getCoordinates())
                            .map(cord -> cord.y + "," + cord.x)
                            .toArray(String[]::new);
                    location.setPoints(points);
                    return location;
                }).collect(Collectors.toList());
    }

    /**
     * @param lat    latitude
     * @param lng    longitude
     * @param size   size in decimals
     * @param points amounts of point in polygon
     * @return Polygon
     */
    private static Polygon createCircle(double lat, double lng, double size, int points) {
        // Create circle shape
        GeometricShapeFactory shape = new GeometricShapeFactory();
        shape.setCentre(new Coordinate(lng, lat));
        shape.setSize(size);
        shape.setNumPoints(points);

        // Create polygon
        Polygon polygon = shape.createCircle();
        polygon.setSRID(4326);
        return polygon;
    }
}
