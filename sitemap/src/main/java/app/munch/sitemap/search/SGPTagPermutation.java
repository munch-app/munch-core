package app.munch.sitemap.search;

import app.munch.sitemap.search.utils.PermutationEngine;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import munch.api.search.SearchQuery;
import munch.api.search.assumption.AssumptionEngine;
import munch.data.named.NamedQuery;
import munch.data.tag.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:31 AM
 * Project: munch-core
 */
public final class SGPTagPermutation extends PermutationEngine {
    private static final Logger logger = LoggerFactory.getLogger(SGPTagPermutation.class);

    private Set<Set<Tag>> getTags() {
        List<Tag> list = Lists.newArrayList(tagClient.iterator());
        Set<Set<Tag>> tags = new HashSet<>();
        for (Tag tag1 : list) {
            if (tag1.getCounts().getTotal() < 100) continue;

            for (Tag tag2 : list) {
                if (tag2.getCounts().getTotal() < 100) continue;
                tags.add(ImmutableSet.of(tag1, tag2));
            }
        }

        logger.info("Tag Permutation found before query: {}", tags.size());
        return tags;
    }

    @Override
    @SuppressWarnings("ConstantConditions")
    protected Iterator<SearchQuery> iterator() {
        return Iterators.transform(getTags().iterator(), tags -> {
            SearchQuery query = new SearchQuery();
            query.getFilter().getTags().addAll(tags);

            query.getFilter().getLocation().setType(SearchQuery.Filter.Location.Type.Where);
            query.getFilter().getLocation().setAreas(List.of(AssumptionEngine.SINGAPORE));
            return query;
        });
    }

    @Override
    public String getSlug(SearchQuery searchQuery) {
        String tags = joinTags(searchQuery, "-");
        return "sgp-" + clean(tags);
    }

    @Override
    public String getTitle(SearchQuery searchQuery) {
        String prefix = joinTags(searchQuery, ", ");
        return prefix + " places in Singapore · Munch Singapore";
    }

    @Override
    public String getDescription(SearchQuery searchQuery) {
        String prefix = joinTags(searchQuery, ", ");
        return prefix + " places in Singapore." +
                "View images and articles from popular food bloggers. " +
                "Find the best places by price, location, preferences on Munch!";
    }

    @Override
    protected boolean isValid(NamedQuery namedQuery) {
        return namedQuery.getCount() >= 10;
    }
}
