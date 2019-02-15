package munch.api.location;

import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.data.ElasticObject;
import munch.data.client.ElasticClient;
import munch.data.elastic.ElasticUtils;
import munch.data.location.Area;
import munch.data.location.Landmark;
import munch.location.Country;
import munch.location.GeocodeClient;
import munch.location.LatitudeLongitude;
import munch.restful.core.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 29/11/18
 * Time: 8:21 AM
 * Project: munch-core
 */
@Singleton
public final class LocationDatabase {

    private final GeocodeClient geocodeClient;
    private final ElasticClient elasticClient;

    @Inject
    public LocationDatabase(GeocodeClient geocodeClient, ElasticClient elasticClient) {
        this.geocodeClient = geocodeClient;
        this.elasticClient = elasticClient;
    }

    public List<NamedLocation> search(String text, String userLatLng) {
        // Try Geocode if is Numeric and 6 digits
        if (StringUtils.isNumeric(text) && StringUtils.length(text) == 6) {
            LatitudeLongitude latLng = geocodeClient.withPostcode(Country.sgp, text);
            if (latLng != null) return List.of(asLocation(text, latLng.asString()));
        }

        // Try Munch Database
        List<NamedLocation> results = search(text);
        if (!results.isEmpty()) return results;

        // Try universal Geocoder database
        return geocodeClient.search(Country.sgp, text).stream()
                .map(nll -> asLocation(nll.getName(), nll.asString()))
                .collect(Collectors.toList());
    }

    public NamedLocation reverse(String latLng) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 1);

        ObjectNode bool = JsonUtils.createObjectNode();
        bool.set("must", ElasticUtils.mustMatchAll());
        bool.set("filter", JsonUtils.createArrayNode()
                .add(ElasticUtils.filterTerm("dataType", "Area"))
                .add(ElasticUtils.filterTerms("type", Set.of("Region", "Cluster")))
                .add(ElasticUtils.filterIntersectsPoint("location.polygon", latLng))
        );
        root.putObject("query").set("bool", bool);

        List<Area> areas = elasticClient.searchHitsHits(root);
        if (areas.isEmpty()) return null;

        Area area = areas.get(0);
        return asLocation(area.getName(), area.getLocation().getLatLng());
    }

    private List<NamedLocation> search(String text) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 10);
        ObjectNode queryNode = root.putObject("query");
        ObjectNode boolNode = queryNode.putObject("bool");

        // must: {?}
        boolNode.set("must", ElasticUtils.multiMatch(text, "name", "names"));

        boolNode.putArray("filter")
                .addObject()
                .putObject("terms")
                .putArray("dataType")
                .add("Area")
                .add("Landmark");

        List<ElasticObject> objects = elasticClient.searchHitsHits(root);
        List<NamedLocation> list = new ArrayList<>();
        for (ElasticObject object : objects) {
            if (object instanceof Area) {
                Area area = (Area) object;
                list.add(asLocation(area.getName(), area.getLocation().getLatLng()));
            } else if (object instanceof Landmark) {
                Landmark landmark = (Landmark) object;
                list.add(asLocation(landmark.getName(), landmark.getLocation().getLatLng()));
            }
        }
        return list;
    }

    private static NamedLocation asLocation(String name, String latLng) {
        NamedLocation point = new NamedLocation();
        point.setName(name);
        point.setLatLng(latLng);
        return point;
    }
}
