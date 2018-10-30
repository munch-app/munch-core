package munch.sitemap.sgp;

import com.fasterxml.jackson.databind.JsonNode;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.sitemap.permutation.PermutationEngine;
import munch.sitemap.permutation.PermutationService;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Iterator;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 10:29 AM
 * Project: munch-core
 */
@Singleton
public final class SGPLocationPermutation extends PermutationEngine {

    @Inject
    protected SGPLocationPermutation(PermutationService service) {
        super(service);
    }

    @Override
    protected Iterator<SearchQuery> iterator() {
        // TODO Combination
        return null;
    }

    @Override
    protected String getName(SearchQuery searchQuery) {
        return "sgp-" + getLocationName(searchQuery) + "-" + joinTags(searchQuery, "-");
    }

    @Override
    protected String getTitle(SearchQuery searchQuery) {
        return "The best " + joinTags(searchQuery, " ") + " places in and around " + getLocationName(searchQuery) + " · Munch Singapore";
    }

    @Override
    protected String getKeywords(SearchQuery searchQuery) {
        return joinTags(searchQuery, ",") + "," + getLocationName(searchQuery) + ",Singapore,Food";
    }

    @Override
    protected String getDescription(SearchQuery searchQuery) {
        return "Looking for " + joinTags(searchQuery, " ") + " places in and around " + getLocationName(searchQuery) + "? " +
                "View images and articles from food bloggers. " +
                "Find the best places by price, location, preferences on Munch! ";
    }

    @Override
    protected boolean validate(NamedSearchQuery namedSearchQuery, List<JsonNode> data) {
        if (namedSearchQuery.getSearchQuery().getFilter().getTag().getPositives().size() == 3) {
            return hasMinPlace(data, 5);
        }
        return hasMinPlace(data, 10);
    }
}
