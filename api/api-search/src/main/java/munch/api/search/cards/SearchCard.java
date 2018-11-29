package munch.api.search.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:15 PM
 * Project: munch-core
 */
public interface SearchCard {

    /**
     * Version of the card is usually a result of data structure update
     * Rarely it can be based on incremental design changes as well
     *
     * @return id of the card
     */
    @JsonProperty("_cardId")
    @Pattern(regexp = "[a-z-0]{1,64}_[0-9]{4}-[0-9]{2}-[0-9]{2}", flags = Pattern.Flag.CASE_INSENSITIVE)
    String getCardId();

    /**
     * Default to CardId if not implemented, override if Card is non unique
     * non unique cards appears more than once in Search
     * <p>
     * The purpose of this field is used to remove duplicated card
     *
     * @return unique id of card to uniquely separate card content
     */
    @JsonProperty("_uniqueId")
    default String getUniqueId() {
        return getCardId();
    }
}
