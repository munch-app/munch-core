package munch.api.search.cards;

import munch.api.search.filter.FilterTag;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 11/5/18
 * Time: 8:57 PM
 * Project: munch-core
 */
public final class SearchSuggestedTagCard implements SearchCard {

    private final String locationName;
    private final List<FilterTag> tags;

    public SearchSuggestedTagCard(String locationName, List<FilterTag> tags) {
        this.locationName = locationName;
        this.tags = tags;
    }

    public String getLocationName() {
        return locationName;
    }

    public List<FilterTag> getTags() {
        return tags;
    }

    @Override
    public String getCardId() {
        return "SuggestedTag_2018-05-11";
    }
}
