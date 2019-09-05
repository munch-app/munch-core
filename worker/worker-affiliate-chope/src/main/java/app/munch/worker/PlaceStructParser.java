package app.munch.worker;

import app.munch.model.PlaceStruct;
import com.fasterxml.jackson.databind.JsonNode;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 3/9/19
 * Time: 8:07 pm
 */
@Singleton
public final class PlaceStructParser {

    private final PlaceStruct.BuilderFactory builderFactory;

    @Inject
    PlaceStructParser(PlaceStruct.BuilderFactory builderFactory) {
        this.builderFactory = builderFactory;
    }

    public PlaceStruct parse(JsonNode node) {
        PlaceStruct.Builder builder = builderFactory.newBuilder();

        builder.name(node.path("name").asText());
        builder.address(node.path("address").path("streetAddress").asText());
        builder.postcode(node.path("address").path("postalCode").asText());

        JsonNode geo = node.path("geo");
        if (geo.has("latitude") && geo.has("longitude")) {
            builder.latLng(geo.path("latitude").asText() + "," + geo.path("longitude").asText());
        }

        builder.phone(node.path("telephone").asText());
        return builder.build();
    }
}
