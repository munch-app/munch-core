package munch.api.place.card;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Created by: Fuxing
 * Date: 12/11/2017
 * Time: 4:51 PM
 * Project: munch-core
 */
public final class PlaceHeaderCard implements PlaceCard<JsonNode> {
    private final String cardId;

    public PlaceHeaderCard(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public String getCardId() {
        return cardId;
    }

    @Override
    public JsonNode getData() {
        return null;
    }
}