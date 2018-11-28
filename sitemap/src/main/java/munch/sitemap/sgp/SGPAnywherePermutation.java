package munch.sitemap.sgp;

import com.google.common.collect.Iterators;
import munch.api.search.SearchQuery;
import munch.api.search.assumption.AssumptionEngine;
import munch.data.named.NamedQuery;
import munch.permutation.PermutationEngine;

import javax.inject.Singleton;
import java.util.Iterator;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:31 AM
 * Project: munch-core
 */
@Singleton
public final class SGPAnywherePermutation extends PermutationEngine {

    @Override
    @SuppressWarnings("ConstantConditions")
    protected Iterator<SearchQuery> iterator() {
        return Iterators.transform(tagClient.iterator(), tag -> {
            if (tag.getCounts().getTotal() < 100) return null;

            SearchQuery query = new SearchQuery();
            query.getFilter().getTags().add(tag);

            query.getFilter().getLocation().setType(SearchQuery.Filter.Location.Type.Where);
            query.getFilter().getLocation().setAreas(List.of(AssumptionEngine.SINGAPORE));
            return query;
        });
    }

    @Override
    public String getSlug(SearchQuery searchQuery) {
        String tag = joinTags(searchQuery, "-");
        return "sgp-" + clean(tag);
    }

    @Override
    public String getTitle(SearchQuery searchQuery) {
        String prefix = joinTags(searchQuery, ", ");
        return prefix + " places in Singapore · Munch Singapore";
    }

    @Override
    public String getDescription(SearchQuery searchQuery) {
        String prefix = joinTags(searchQuery, ", ");

        return prefix + " places in Singapore. " +
                "View images and articles from popular food bloggers. " +
                "Find the best places by price, location, preferences on Munch!";
    }

    @Override
    protected boolean isValid(NamedQuery namedQuery) {
        return namedQuery.getCount() >= 10;
    }
}