package munch.data.places;

/**
 * contains 2 fields: id, data
 * <pre>
 * {
 *      "id": "type_Name_version",
 *      "fields": ...
 * }
 * </pre>
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
     *
     * @return id of the card
     */
    public abstract String getId();
}
