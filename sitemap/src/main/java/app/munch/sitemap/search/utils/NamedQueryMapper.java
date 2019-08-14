package app.munch.sitemap.search.utils;

import munch.api.search.SearchQuery;
import munch.data.location.Area;
import munch.data.tag.Tag;

import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 31/10/18
 * Time: 1:31 AM
 * Project: munch-core
 */
public interface NamedQueryMapper {

    /**
     * @return version of query mapper
     */
    default String getVersion() {
        return "2018-11-28";
    }

    String getSlug(SearchQuery searchQuery);

    String getTitle(SearchQuery searchQuery);

    String getDescription(SearchQuery searchQuery);

    default String getLocationName(SearchQuery searchQuery) {
        SearchQuery.Filter.Location location = searchQuery.getFilter().getLocation();
        Area area = location.getAreas().get(0);
        return area.getName();
    }

    default String joinTags(SearchQuery searchQuery, String joiner) {
        return searchQuery.getFilter().getTags().stream()
                .map(Tag::getName)
                .collect(Collectors.joining(joiner));
    }

    default String clean(String name) {
        name = name.toLowerCase();
        name = name.replace(" ", "-");
        name = name.replaceAll("[^a-z0-9-]", "");
        return name;
    }
}
