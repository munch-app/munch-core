package munch.api.services.discover.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:15 PM
 * Project: munch-core
 */
public interface SearchCard {

    /**
     * Id format:
     * type_Name_version(yyyymmdd)
     * E.g. basic_Place_20170913
     * <p>
     * Version of the card is usually a result of data structure update
     * Rarely it can be based on incremental design changes as well
     *
     * @return id of the card
     */
    @JsonProperty("_cardId")
    String getCardId();

    /**
     * @return unique id of card to uniquely separate card content and remove duplicate on iOS side
     */
    @JsonProperty("_uniqueId")
    String getUniqueId();
}
