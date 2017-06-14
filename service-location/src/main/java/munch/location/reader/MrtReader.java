package munch.location.reader;

import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.util.GeometricShapeFactory;
import munch.location.database.LocationV2;

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
public class MrtReader {

    public List<LocationV2> read() throws IOException {
        URL url = Resources.getResource("reader/mrt.csv");
        String csv = Resources.toString(url, Charset.forName("UTF-8"));

        return Arrays.stream(csv.split("\n"))
                .map(s -> s.split(","))
                .map(lines -> {
                    LocationV2 location = new LocationV2();
                    location.setName(lines[0].trim());
                    location.setSort(1);

                    // Create polygon circle
                    Coordinate coordinate = new Coordinate(Double.parseDouble(lines[2]), Double.parseDouble(lines[1]));
                    location.setPolygon(createCircle(coordinate, 0.009, 10));

                    return location;
                }).collect(Collectors.toList());
    }

    /**
     * @param coordinate coordinates
     * @param size       size in decimals
     * @param points     amounts of point in polygon
     * @return Polygon
     */
    private static Polygon createCircle(Coordinate coordinate, double size, int points) {
        // Create circle shape
        GeometricShapeFactory shape = new GeometricShapeFactory();
        shape.setCentre(coordinate);
        shape.setSize(size);
        shape.setNumPoints(points);

        // Create polygon
        Polygon polygon = shape.createCircle();
        polygon.setSRID(4326);
        return polygon;
    }
}
