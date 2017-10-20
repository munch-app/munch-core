package munch.api.services.places.cards;

import com.fasterxml.jackson.databind.JsonNode;
import munch.data.structure.PlaceCard;

/**
 * Created by: Fuxing
 * Date: 20/10/2017
 * Time: 2:38 PM
 * Project: munch-core
 */
public class PlaceHeaderReviewCard implements PlaceCard<JsonNode> {
    @Override
    public String getCardId() {
        return "header_Review_20171020";
    }

    @Override
    public JsonNode getData() {
        return null;
    }
}
