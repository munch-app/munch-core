package munch.api.search;

import app.munch.api.ApiServiceModule;
import com.google.inject.multibindings.Multibinder;
import munch.api.search.cards.SearchPlaceCard;
import munch.api.search.plugin.*;
import munch.api.search.plugin.collection.SearchCollectionHeaderPlugin;
import munch.api.search.plugin.collection.SearchCollectionItemPlugin;
import munch.api.search.plugin.home.SearchHomeNearbyPlugin;
import munch.api.search.plugin.home.SearchHomePopularPlacePlugin;
import munch.api.search.plugin.home.SearchHomeRecentPlacePlugin;
import munch.api.search.plugin.location.SearchBetweenPlugin;
import munch.api.search.plugin.location.SearchLocationAreaPlugin;
import munch.api.search.plugin.location.SearchLocationBannerPlugin;
import munch.api.search.plugin.series.SearchHomeSeriesPlugin;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 5:21 PM
 * Project: munch-core
 */
@Deprecated
public final class SearchModule extends ApiServiceModule {

    @Override
    protected void configure() {
        Multibinder<SearchCardPlugin> loaderBinder = Multibinder.newSetBinder(binder(), SearchCardPlugin.class);
        loaderBinder.addBinding().to(SearchAreaClusterListPlugin.class);
        loaderBinder.addBinding().to(SearchAreaClusterHeaderPlugin.class);
        loaderBinder.addBinding().to(SearchNoResultPlugin.class);
        loaderBinder.addBinding().to(SearchNoLocationPlugin.class);
        loaderBinder.addBinding().to(SearchHeaderPlugin.class);
        loaderBinder.addBinding().to(SearchSuggestionTagPlugin.class);
        loaderBinder.addBinding().to(SearchBetweenPlugin.class);

        loaderBinder.addBinding().to(SearchHomeNearbyPlugin.class);
        loaderBinder.addBinding().to(SearchHomeRecentPlacePlugin.class);
        loaderBinder.addBinding().to(SearchHomePopularPlacePlugin.class);
        loaderBinder.addBinding().to(SearchHomeSeriesPlugin.class);

        loaderBinder.addBinding().to(SearchLocationBannerPlugin.class);
        loaderBinder.addBinding().to(SearchLocationAreaPlugin.class);

        loaderBinder.addBinding().to(SearchCollectionHeaderPlugin.class);
        loaderBinder.addBinding().to(SearchCollectionItemPlugin.class);

        addDeprecatedService(SearchService.class);
        addDeprecatedService(SuggestService.class);
        addDeprecatedService(SearchFilterService.class);

        addCleaner(SearchPlaceCard.Cleaner.class);
    }
}
