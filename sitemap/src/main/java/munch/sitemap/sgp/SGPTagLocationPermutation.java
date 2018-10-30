package munch.sitemap.sgp;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.data.location.Area;
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
        Set<Set<String>> tags = Lists.newArrayList(tagClient.iterator())
                .stream()
                .filter(tag -> tag.getCounts().getTotal() >= 100)
                .map(tag -> Set.of(tag.getName().toLowerCase()))
                .collect(Collectors.toSet());

        Iterator<Iterator<SearchQuery>> areas = Iterators.transform(areaClient.iterator(), area -> {
            return Iterators.transform(tags.iterator(), positives -> {
                SearchQuery query = newSearchQuery();
                query.getFilter().getTag().setPositives(positives);
                query.getFilter().getLocation().setAreas(List.of(area));
                return query;
            });
        });

        Iterator<Iterator<SearchQuery>> landmarks = Iterators.transform(landmarkClient.iterator(), landmark -> {
            return Iterators.transform(tags.iterator(), positives -> {
                SearchQuery query = newSearchQuery();
                query.getFilter().getTag().setPositives(positives);

                Area area = LandmarkUtils.asArea(landmark, 1);
                query.getFilter().getLocation().setAreas(List.of(area));
                return query;
            });
        });
        return Iterators.concat(Iterators.concat(areas), Iterators.concat(landmarks));
    }

    @Override
    public String getName(SearchQuery searchQuery) {
        return "sgp-" + getLocationName(searchQuery) + "-" + joinTags(searchQuery, "-");
    }

    @Override
    public String getTitle(SearchQuery searchQuery) {
        return "The best " + joinTags(searchQuery, " ") + " places in and around " + getLocationName(searchQuery) + " · Munch Singapore";
    }

    @Override
    public String getKeywords(SearchQuery searchQuery) {
        return joinTags(searchQuery, ",") + "," + getLocationName(searchQuery) + ",Singapore,Food";
    }

    @Override
    public String getDescription(SearchQuery searchQuery) {
        return "Looking for " + joinTags(searchQuery, " ") + " places in and around " + getLocationName(searchQuery) + "? " +
                "View images and articles from food bloggers. " +
                "Find the best places by price, location, preferences on Munch! ";
    }

    @Override
    protected boolean validate(NamedSearchQuery namedSearchQuery) {
        return namedSearchQuery.getCount() >= 10;
    }
}
