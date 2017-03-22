package munch.search;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:49 PM
 * Project: munch-core
 */
public class ElasticModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SearchStore.class).to(ElasticStore.class);
    }

    @Provides
    @Singleton
    RestClient provideClient() {
        Config config = ConfigFactory.load().getConfig("munch.struct.search");

        return RestClient.builder(new HttpHost(
                config.getString("hostname"),
                config.getInt("port"),
                config.getString("scheme"))
        ).build();
    }
}
