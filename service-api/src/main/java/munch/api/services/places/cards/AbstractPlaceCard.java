package munch.api.services.places.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by: Fuxing
 * Date: 18/10/2017
 * Time: 12:06 AM
 * Project: munch-core
 */
public abstract class AbstractPlaceCard {

    /**
     * Id format:
     * type_Name_version(yyyymmdd)
     * E.g. basic_Banner_20170609
     * E.g. vendor_FacebookReview_20171205
     * E.g. vendor_InstagramMedia_20160101
     * <p>
     * Version of the card is usually a result of data structure update
     * Rarely it can be based on incremental design changes as well
     *
     * @return id of the card
     */
    @JsonIgnore
    public abstract String getCardId();
}
