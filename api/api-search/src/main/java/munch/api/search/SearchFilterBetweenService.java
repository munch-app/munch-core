package munch.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import munch.api.ApiService;
import munch.api.search.data.BetweenLocation;
import munch.api.search.suggest.SuggestDelegator;
import munch.data.ElasticObject;
import munch.data.client.AreaClient;
import munch.data.client.ElasticClient;
import munch.data.location.Area;
import munch.data.location.Landmark;
import munch.data.location.Location;
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
    private final AreaClient areaClient;
    private final ElasticClient elasticClient;

    @Inject
    public SearchFilterBetweenService(GeocodeClient geocodeClient, AreaClient areaClient, ElasticClient elasticClient) {
        this.geocodeClient = geocodeClient;
        this.areaClient = areaClient;
        this.elasticClient = elasticClient;
    }

    @Override
    public void route() {
        PATH("/search/filter/between", () -> {
            POST("/search", this::search);
            POST("/generate", this::generate);
        });
    }

    public List<BetweenLocation> search(JsonCall call) {
        JsonNode json = call.bodyAsJson();
        String text = json.path("text").asText();

        // Try Geocode if is Numeric and 6 digits
        if (StringUtils.isNumeric(text) && StringUtils.length(text) == 6) {
            LatitudeLongitude latLng = geocodeClient.withPostcode(Country.sgp, text);
            if (latLng != null) return List.of(asLocation(text, latLng.asString()));
        }

        // Try Munch Database
        List<BetweenLocation> results = search(text);
        if (!results.isEmpty()) return results;

        // Try universal Geocoder database
        return geocodeClient.search(Country.sgp, text).stream()
                .map(nll -> asLocation(nll.getName(), nll.asString()))
                .collect(Collectors.toList());
    }

    private List<BetweenLocation> search(String text) {
        ObjectNode root = JsonUtils.createObjectNode();
        root.put("from", 0);
        root.put("size", 40);
        ObjectNode queryNode = root.putObject("query");
        ObjectNode boolNode = queryNode.putObject("bool");

        // must: {?}
        boolNode.set("must", SuggestDelegator.multiMatchNameNames(text));

        // filter: [{"term": {"dataType": "Place"}}]
        boolNode.putArray("filter")
                .addObject()
                .putObject("terms")
                .putArray("dataType")
                .add("Area")
                .add("Landmark");

        List<ElasticObject> objects = elasticClient.searchHitsHits(root);
        List<BetweenLocation> list = new ArrayList<>();
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

    private BetweenLocation asLocation(String name, String latLng) {
        BetweenLocation betweenLocation = new BetweenLocation();
        betweenLocation.setName(name);

        Location location = new Location();
        location.setLatLng(latLng);
        betweenLocation.setLocation(location);
        return betweenLocation;
    }

    public List<Area> generate(JsonCall call) {
        List<BetweenLocation> locations = call.bodyAsList(BetweenLocation.class);
        // TODO: Generate a list of Area

        return areaClient.list(null, 10);
    }
}
