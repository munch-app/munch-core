package munch.sitemap.permutation;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.data.location.Area;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:24 AM
 * Project: munch-core
 */
public abstract class PermutationEngine {

    protected final PermutationService service;

    @Inject
    protected PermutationEngine(PermutationService service) {
        this.service = service;
    }

    public Iterator<NamedSearchQuery> get() {
        Iterator<SearchQuery> iterator = iterator();
        Iterator<NamedSearchQuery> mapped = Iterators.transform(iterator, searchQuery -> {
            NamedSearchQuery query = new NamedSearchQuery();
            query.setSearchQuery(searchQuery);
            query.setName(getName(searchQuery));
            query.setTitle(getTitle(searchQuery));
            query.setKeywords(getKeywords(searchQuery));
            query.setDescription(getDescription(searchQuery));
            return query;
        });

        Iterator<NamedSearchQuery> filtered = Iterators.filter(mapped, input -> {
            Objects.requireNonNull(input);
            return validate(input, service.search(input.getSearchQuery()));
        });

        return Iterators.transform(filtered, input -> {
            service.put(input);
            return input;
        });
    }

    protected abstract Iterator<SearchQuery> iterator();

    protected abstract String getName(SearchQuery searchQuery);

    protected abstract String getTitle(SearchQuery searchQuery);

    protected abstract String getKeywords(SearchQuery searchQuery);

    protected abstract String getDescription(SearchQuery searchQuery);

    protected abstract boolean validate(NamedSearchQuery namedSearchQuery, List<JsonNode> data);

    public static String getLocationName(SearchQuery searchQuery) {
        SearchQuery.Filter.Location location = searchQuery.getFilter().getLocation();
        Area area = location.getAreas().get(0);
        return area.getName();
    }

    public static String joinTags(SearchQuery searchQuery, String joiner) {
        SearchQuery.Filter.Tag tag = searchQuery.getFilter().getTag();
        return Joiner.on(joiner)
                .join(tag.getPositives());
    }

    public static boolean hasMinPlace(List<JsonNode> nodes, int count) {
        for (JsonNode node : nodes) {
            if (node.path("place").isObject()) {
                if (count-- <= 0) return true;
            }
        }
        return false;
    }
}
