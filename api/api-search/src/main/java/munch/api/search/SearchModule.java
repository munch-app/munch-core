package munch.api.search;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.google.inject.Provides;
import com.google.inject.multibindings.Multibinder;
import munch.api.ApiServiceModule;
import munch.api.search.inject.*;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 5:21 PM
 * Project: munch-core
 */
public final class SearchModule extends ApiServiceModule {

    @Override
    protected void configure() {
        Multibinder<SearchCardInjector.Loader> loaderBinder = Multibinder.newSetBinder(binder(), SearchCardInjector.Loader.class);
        loaderBinder.addBinding().to(SearchAreaClusterListLoader.class);
        loaderBinder.addBinding().to(SearchAreaClusterHeaderLoader.class);
        loaderBinder.addBinding().to(SearchNoResultLoader.class);
        loaderBinder.addBinding().to(SearchNoLocationLoader.class);
        loaderBinder.addBinding().to(SearchHeaderLoader.class);
        loaderBinder.addBinding().to(SearchSuggestionTagLoader.class);
        loaderBinder.addBinding().to(SearchBetweenLoader.class);

        addService(SearchService.class);
        addService(SearchFilterService.class);
        addService(SearchFilterAreaService.class);
        addService(SearchFilterBetweenService.class);
    }

    @Provides
    @Singleton
    DynamoDB provideDynamoDB() {
        return new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
    }
}
