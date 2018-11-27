package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.ApiService;
import munch.api.search.data.SearchQuery;
import munch.api.search.elastic.ElasticQueryUtils;
import munch.data.ElasticObject;
import munch.data.client.ElasticClient;
import munch.data.location.Area;
import munch.data.location.Landmark;
import munch.location.Country;
import munch.location.GeocodeClient;
import munch.location.LatitudeLongitude;
import munch.restful.core.JsonUtils;
import munch.restful.server.JsonCall;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 24/9/18
 * Time: 6:51 PM
 * Project: munch-core
 */
@Singleton
public final class SearchFilterBetweenService extends ApiService {

    private final GeocodeClient geocodeClient;
    private final ElasticClient elasticClient;

    @Inject
    public SearchFilterBetweenService(GeocodeClient geocodeClient, ElasticClient elasticClient) {
        this.geocodeClient = geocodeClient;
        this.elasticClient = elasticClient;
    }

    @Override
    public void route() {
        PATH("/search/filter/between", () -> {
            POST("/search", this::search);
        });
    }

    public List<SearchQuery.Filter.Location.Point> search(JsonCall call) {
        JsonNode json = call.bodyAsJson();
        String text = json.path("text").asText();

        // Try Geocode if is Numeric and 6 digits
        if (StringUtils.isNumeric(text) && StringUtils.length(text) == 6) {
            LatitudeLongitude latLng = geocodeClient.withPostcode(Country.sgp, text);
            if (latLng != null) return List.of(asPoint(text, latLng.asString()));
        }

        // Try Munch Database
        List<SearchQuery.Filter.Location.Point> results = search(text);
        if (!results.isEmpty()) return results;

        // Try universal Geocoder database
        return geocodeClient.search(Country.sgp, text).stream()
                .map(nll -> asPoint(nll.getName(), nll.asString()))
                .collect(Collectors.toList());
    }

    private List<SearchQuery.Filter.Location.Point> search(String text) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 10);
        ObjectNode queryNode = root.putObject("query");
        ObjectNode boolNode = queryNode.putObject("bool");

        // must: {?}
        boolNode.set("must", ElasticQueryUtils.multiMatch(text, "name", "names"));

        boolNode.putArray("filter")
                .addObject()
                .putObject("terms")
                .putArray("dataType")
                .add("Area")
                .add("Landmark");

        List<ElasticObject> objects = elasticClient.searchHitsHits(root);
        List<SearchQuery.Filter.Location.Point> list = new ArrayList<>();
        for (ElasticObject object : objects) {
            if (object instanceof Area) {
                Area area = (Area) object;
                list.add(asPoint(area.getName(), area.getLocation().getLatLng()));
            } else if (object instanceof Landmark) {
                Landmark landmark = (Landmark) object;
                list.add(asPoint(landmark.getName(), landmark.getLocation().getLatLng()));
            }
        }
        return list;
    }

    private SearchQuery.Filter.Location.Point asPoint(String name, String latLng) {
        SearchQuery.Filter.Location.Point point = new SearchQuery.Filter.Location.Point();
        point.setName(name);
        point.setLatLng(latLng);
        return point;
    }
}
