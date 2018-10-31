package munch.permutation;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.data.location.Area;

import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 31/10/18
 * Time: 1:31 AM
 * Project: munch-core
 */
public interface NamedQueryMapper {

    /**
     * @param searchQuery to map from
     * @return mapped name, note that name will still be cleaned after generating
     */
    String getName(SearchQuery searchQuery);

    String getTitle(SearchQuery searchQuery);

    String getKeywords(SearchQuery searchQuery);

    String getDescription(SearchQuery searchQuery);

    /**
     * @return SearchQuery with all tag & location filled
     */
    default SearchQuery newSearchQuery() {
        SearchQuery query = new SearchQuery();
        query.setFilter(new SearchQuery.Filter());
        query.setSort(new SearchQuery.Sort());

        query.getFilter().setLocation(new SearchQuery.Filter.Location());
        query.getFilter().getLocation().setType(SearchQuery.Filter.Location.Type.Where);

        query.getFilter().setTag(new SearchQuery.Filter.Tag());
        return query;
    }

    default NamedSearchQuery map(SearchQuery searchQuery) {
        NamedSearchQuery query = new NamedSearchQuery();
        query.setSearchQuery(searchQuery);
        query.setName(cleanName(getName(searchQuery)));
        query.setTitle(getTitle(searchQuery));
        query.setKeywords(getKeywords(searchQuery));
        query.setDescription(getDescription(searchQuery));
        return query;
    }

    default String getLocationName(SearchQuery searchQuery) {
        SearchQuery.Filter.Location location = searchQuery.getFilter().getLocation();
        Area area = location.getAreas().get(0);
        return area.getName();
    }

    default String joinTags(SearchQuery searchQuery, String joiner) {
        SearchQuery.Filter.Tag tag = searchQuery.getFilter().getTag();
        return Joiner.on(joiner).join(tag.getPositives());
    }

    default String joinTags(SearchQuery searchQuery, String joiner, Function<String, String> mapper) {
        SearchQuery.Filter.Tag tag = searchQuery.getFilter().getTag();
        return tag.getPositives().stream()
                .map(mapper::apply)
                .collect(Collectors.joining(joiner));
    }

    private String cleanName(String name) {
        name = name.toLowerCase();
        name = name.replace(" ", "-");
        name = name.replaceAll("[^a-z0-9-]", "");
        return name;
    }
}
