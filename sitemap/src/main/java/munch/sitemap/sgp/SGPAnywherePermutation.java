package munch.sitemap.sgp;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Iterators;
import munch.api.search.assumption.AssumptionEngine;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.sitemap.permutation.PermutationEngine;
import munch.sitemap.permutation.PermutationService;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:31 AM
 * Project: munch-core
 */
public final class SGPAnywherePermutation extends PermutationEngine {

    @Inject
    protected SGPAnywherePermutation(PermutationService service) {
        super(service);
    }

    @Override
    protected Iterator<SearchQuery> iterator() {
        return Iterators.transform(service.tagIterator(), input -> {
            SearchQuery query = new SearchQuery();
            query.setFilter(new SearchQuery.Filter());
            query.getFilter().setTag(new SearchQuery.Filter.Tag());
            query.getFilter().setLocation(new SearchQuery.Filter.Location());

            query.getFilter().getTag().setPositives(Set.of(input.getName().toLowerCase()));
            query.getFilter().getLocation().setAreas(List.of(AssumptionEngine.SINGAPORE));
            return query;
        });
    }

    @Override
    protected String getName(SearchQuery searchQuery) {
        return "sgp-" + joinTags(searchQuery, "-");
    }

    @Override
    protected String getTitle(SearchQuery searchQuery) {
        return "The best " + joinTags(searchQuery, " ") + " places in Singapore · Munch Singapore";
    }

    @Override
    protected String getKeywords(SearchQuery searchQuery) {
        return joinTags(searchQuery, ",") + ",Singapore,Food";
    }

    @Override
    protected String getDescription(SearchQuery searchQuery) {
        return "Looking for " + joinTags(searchQuery, ", ") + " places in Singapore? " +
                "View images and articles from popular food bloggers. " +
                "Find the best places by price, location, preferences on Munch!";
    }

    @Override
    protected boolean validate(NamedSearchQuery namedSearchQuery, List<JsonNode> data) {
        return hasMinPlace(data, 10);
    }
}