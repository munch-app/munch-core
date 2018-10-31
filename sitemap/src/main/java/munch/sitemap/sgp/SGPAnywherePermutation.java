package munch.sitemap.sgp;

import com.google.common.collect.Iterators;
import munch.api.search.assumption.AssumptionEngine;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.permutation.PermutationEngine;
import org.apache.commons.lang3.text.WordUtils;

import javax.inject.Singleton;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

            SearchQuery query = newSearchQuery();
            query.getFilter().getTag().setPositives(Set.of(tag.getName().toLowerCase()));
            query.getFilter().getLocation().setAreas(List.of(AssumptionEngine.SINGAPORE));
            return query;
        });
    }

    @Override
    public String getName(SearchQuery searchQuery) {
        return "sgp-" + joinTags(searchQuery, "-");
    }

    @Override
    public String getTitle(SearchQuery searchQuery) {
        String prefix = joinTags(searchQuery, ", ", WordUtils::capitalizeFully);
        return prefix + " places in Singapore · Munch Singapore";
    }

    @Override
    public String getKeywords(SearchQuery searchQuery) {
        return joinTags(searchQuery, ",") + ",Singapore,Food";
    }

    @Override
    public String getDescription(SearchQuery searchQuery) {
        String prefix = joinTags(searchQuery, ", ", WordUtils::capitalizeFully);
        return prefix + " places in Singapore. " +
                "View images and articles from popular food bloggers. " +
                "Find the best places by price, location, preferences on Munch!";
    }

    @Override
    protected boolean validate(NamedSearchQuery namedSearchQuery) {
        return namedSearchQuery.getCount() >= 10;
    }
}