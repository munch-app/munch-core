package munch.api.services.search.cards;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:15 PM
 * Project: munch-core
 */
public abstract class SearchCard {

    private String uniqueId;

    /**
     * Id format:
     * type_Name_version(ddmmyyyy)
     * E.g. basic_Place_13092017
     * <p>
     * Version of the card is usually a result of data structure update
     * Rarely it can be based on incremental design changes as well
     *
     * @return id of the card
     */
    public abstract String getCardId();

    /**
     * @return unique id of card to uniquely separate card content and remove duplicate on iOS side
     */
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
