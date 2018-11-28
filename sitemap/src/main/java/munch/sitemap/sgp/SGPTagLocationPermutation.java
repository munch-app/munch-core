package munch.sitemap.sgp;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import munch.api.search.SearchQuery;
import munch.data.location.Area;
import munch.data.named.NamedQuery;
import munch.data.tag.Tag;
import munch.permutation.LandmarkUtils;
import munch.permutation.PermutationEngine;

import javax.inject.Singleton;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 10:29 AM
 * Project: munch-core
 */
@Singleton
public final class SGPTagLocationPermutation extends PermutationEngine {

    @SuppressWarnings("ConstantConditions")
    @Override
    protected Iterator<SearchQuery> iterator() {
        Set<Tag> tags = Lists.newArrayList(tagClient.iterator())
                .stream()
                .filter(tag -> tag.getCounts().getTotal() >= 100)
                .collect(Collectors.toSet());

        Iterator<Iterator<SearchQuery>> areas = Iterators.transform(areaClient.iterator(), area -> {
            return Iterators.transform(tags.iterator(), tag -> {
                SearchQuery query = new SearchQuery();
                query.getFilter().getTags().add(tag);

                query.getFilter().getLocation().setType(SearchQuery.Filter.Location.Type.Where);
                query.getFilter().getLocation().setAreas(List.of(area));
                return query;
            });
        });

        Iterator<Iterator<SearchQuery>> landmarks = Iterators.transform(landmarkClient.iterator(), landmark -> {
            return Iterators.transform(tags.iterator(), tag -> {
                SearchQuery query = new SearchQuery();
                query.getFilter().getTags().add(tag);

                query.getFilter().getLocation().setType(SearchQuery.Filter.Location.Type.Where);
                Area area = LandmarkUtils.asArea(landmark, 1);
                query.getFilter().getLocation().setAreas(List.of(area));
                return query;
            });
        });
        return Iterators.concat(Iterators.concat(areas), Iterators.concat(landmarks));
    }

    @Override
    protected boolean isValid(NamedQuery namedQuery) {
        return namedQuery.getCount() >= 10;
    }

    @Override
    public String getSlug(SearchQuery searchQuery) {
        String location = getLocationName(searchQuery);
        String tag = joinTags(searchQuery, "-");
        return "sgp-" + clean(location + "-" + tag);
    }

    @Override
    public String getTitle(SearchQuery searchQuery) {
        String tag = joinTags(searchQuery, ", ");
        return getLocationName(searchQuery) + ": " + tag + " Places · Munch Singapore";
    }

    @Override
    public String getDescription(SearchQuery searchQuery) {
        String prefix = joinTags(searchQuery, ", ");
        return prefix + " places in and around " + getLocationName(searchQuery) + ". " +
                "View images and articles from food bloggers. " +
                "Find the best places by price, location, preferences on Munch!";
    }
}
