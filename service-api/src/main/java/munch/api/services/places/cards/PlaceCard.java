package munch.api.services.places.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * contains 2 fields: id, data
 * <pre>
 * {
 *      "id": "type_Name_version",
 *      "fields": ...
 * }
 * </pre>
 * <p>
 * Data in the card should be at its most primitive form.
 * String, Double, Integer, Set no POJO
 * <p>
 * Created by: Fuxing
 * Date: 6/9/2017
 * Time: 12:35 AM
 * Project: munch-core
 */
public abstract class PlaceCard {

    /**
     * Id format:
     * type_Name_version(ddmmyyyy)
     * E.g. basic_Banner_06092017
     * E.g. vendor_FacebookReview_01052017
     * E.g. vendor_InstagramMedia_01012016
     * <p>
     * Version of the card is usually a result of data structure update
     * Rarely it can be based on incremental design changes as well
     *
     * @return id of the card
     */
    @JsonProperty("_cardId")
    public abstract String getCardId();
}