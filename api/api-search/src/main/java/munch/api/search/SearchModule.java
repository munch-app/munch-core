package munch.api.search;

import com.google.inject.multibindings.Multibinder;
import munch.api.ApiServiceModule;
import munch.api.search.cards.SearchPlaceCard;
import munch.api.search.plugin.*;
import munch.api.search.plugin.collection.SearchCollectionHeaderPlugin;
import munch.api.search.plugin.collection.SearchCollectionItemPlugin;
import munch.api.search.plugin.home.SearchHomeAwardCollectionPlugin;
import munch.api.search.plugin.home.SearchHomePopularPlacePlugin;
import munch.api.search.plugin.home.SearchHomeRecentPlacePlugin;
import munch.api.search.plugin.home.SearchHomeTabPlugin;
import munch.api.search.plugin.location.SearchLocationAreaPlugin;
import munch.api.search.plugin.location.SearchLocationBannerPlugin;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 5:21 PM
 * Project: munch-core
 */
public final class SearchModule extends ApiServiceModule {

    @Override
    protected void configure() {
        Multibinder<SearchCardPlugin> loaderBinder = Multibinder.newSetBinder(binder(), SearchCardPlugin.class);
        loaderBinder.addBinding().to(SearchAreaClusterListLoader.class);
        loaderBinder.addBinding().to(SearchAreaClusterHeaderLoader.class);
        loaderBinder.addBinding().to(SearchNoResultPlugin.class);
        loaderBinder.addBinding().to(SearchNoLocationPlugin.class);
        loaderBinder.addBinding().to(SearchHeaderPlugin.class);
        loaderBinder.addBinding().to(SearchSuggestionTagLoader.class);
        loaderBinder.addBinding().to(SearchBetweenLoader.class);

        loaderBinder.addBinding().to(SearchHomeTabPlugin.class);
        loaderBinder.addBinding().to(SearchHomeRecentPlacePlugin.class);
        loaderBinder.addBinding().to(SearchHomePopularPlacePlugin.class);
        loaderBinder.addBinding().to(SearchHomeAwardCollectionPlugin.class);

        loaderBinder.addBinding().to(SearchLocationBannerPlugin.class);
        loaderBinder.addBinding().to(SearchLocationAreaPlugin.class);

        loaderBinder.addBinding().to(SearchCollectionHeaderPlugin.class);
        loaderBinder.addBinding().to(SearchCollectionItemPlugin.class);

        addService(SearchService.class);
        addService(SuggestService.class);
        addService(SearchFilterService.class);

        addCleaner(SearchPlaceCard.Cleaner.class);
    }
}
