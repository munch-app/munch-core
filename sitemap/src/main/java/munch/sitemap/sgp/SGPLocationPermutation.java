package munch.sitemap.sgp;

import com.google.common.collect.Iterators;
import munch.api.search.data.NamedSearchQuery;
import munch.api.search.data.SearchQuery;
import munch.data.location.Area;
import munch.permutation.LandmarkUtils;
import munch.permutation.PermutationEngine;

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

    @Override
    @SuppressWarnings("ConstantConditions")
    protected Iterator<SearchQuery> iterator() {
        Iterator<SearchQuery> areas = Iterators.transform(areaClient.iterator(), area -> {
            SearchQuery query = newSearchQuery();
            query.getFilter().getLocation().setAreas(List.of(area));
            return query;
        });

        Iterator<SearchQuery> landmarks = Iterators.transform(landmarkClient.iterator(), landmark -> {
            SearchQuery query = newSearchQuery();
            Area area = LandmarkUtils.asArea(landmark, 1.0);
            query.getFilter().getLocation().setAreas(List.of(area));
            return query;
        });

        return Iterators.concat(areas, landmarks);
    }

    @Override
    public String getName(SearchQuery searchQuery) {
        return "sgp-" + getLocationName(searchQuery);
    }

    @Override
    public String getTitle(SearchQuery searchQuery) {
        return "Best places in and around " + getLocationName(searchQuery) + " · Munch Singapore";
    }

    @Override
    public String getKeywords(SearchQuery searchQuery) {
        return getLocationName(searchQuery) + ",Singapore,Food";
    }

    @Override
    public String getDescription(SearchQuery searchQuery) {
        return "Places in and around " + getLocationName(searchQuery) + ". " +
                "View images and articles from food bloggers. " +
                "Find the best places by price, location, preferences on Munch!";
    }

    @Override
    protected boolean validate(NamedSearchQuery namedSearchQuery) {
        return namedSearchQuery.getCount() >= 10;
    }
}
