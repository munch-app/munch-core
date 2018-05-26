package munch.api.services.search.inject;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * Created by: Fuxing
 * Date: 12/5/18
 * Time: 1:08 AM
 * Project: munch-core
 */
public class SearchCardModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<SearchCardInjector.Loader> loaderBinder = Multibinder.newSetBinder(binder(), SearchCardInjector.Loader.class);
        loaderBinder.addBinding().to(SearchContainerLoader.class);
        loaderBinder.addBinding().to(SearchInstagramLoader.class);
        loaderBinder.addBinding().to(SearchNoResultLoader.class);
        loaderBinder.addBinding().to(SearchNoLocationLoader.class);
        loaderBinder.addBinding().to(SearchHeaderLoader.class);
        loaderBinder.addBinding().to(SearchContainerHeaderLoader.class);
        loaderBinder.addBinding().to(SearchSuggestionTagLoader.class);
    }
}
