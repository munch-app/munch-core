package munch.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.server.JsonUtils;
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
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        return JsonUtils.objectMapper;
    }

    @Provides
    @Singleton
    RestClient provideClient(Config config) {
        Config elastic = config.getConfig("elastic");

        return RestClient.builder(new HttpHost(
                elastic.getString("hostname"),
                elastic.getInt("port"),
                elastic.getString("scheme"))
        ).build();
    }
}
