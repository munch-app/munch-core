package munch.catalyst.builder;

import catalyst.data.CorpusData;
import catalyst.utils.FieldUtils;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import munch.catalyst.data.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 9/7/2017
 * Time: 4:36 AM
 * Project: munch-core
 */
public class LocationBuilder implements DataBuilder<Location> {
    private static final Logger logger = LoggerFactory.getLogger(LocationBuilder.class);
    private static final Supplier<NullPointerException> NullSupplier = () -> new NullPointerException("LocationPolygon");
    private static final String CorpusName = "Sg.Munch.LocationPolygon";
    private static final WKTReader reader = new WKTReader();

    private List<Location> locations = new ArrayList<>();

    @Override
    public void consume(CorpusData data) {
        if (!CorpusName.equals(data.getCorpusName())) return;
        String type = FieldUtils.getValue(data, "Place.type");
        if (type == null || !type.equals("location")) return;

        Location location = new Location();
        location.setId(data.getCatalystId());
        location.setName(FieldUtils.getValue(data, "Place.name"));

        location.setCountry(FieldUtils.getValue(data, "Place.Location.country"));
        location.setCity(FieldUtils.getValue(data, "Place.Location.city"));
        location.setCenter(FieldUtils.getValue(data, "Place.Location.latLng"));

        String wkt = FieldUtils.getValue(data, "Place.Location.polygon");
        try {
            //noinspection ConstantConditions
            Polygon polygon = (Polygon) reader.read(wkt);
            List<String> points = Arrays.stream(polygon.getCoordinates())
                    .map(c -> c.y + "," + c.x)
                    .collect(Collectors.toList());
            location.setPoints(points);
        } catch (ParseException e) {
            logger.error("Unable to parse Place.Location.polygon WKT: {}", wkt, e);
            throw new RuntimeException(e);
        }

        locations.add(location);
    }

    @Override
    public List<Location> collect(Date updatedDate) {
        locations.forEach(location -> location.setUpdatedDate(updatedDate));
        return locations;
    }
}
