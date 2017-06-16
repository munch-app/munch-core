package munch.places.elastic;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.WaitFor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:49 PM
 * Project: munch-core
 */
public class ElasticModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(ElasticModule.class);

    @Override
    protected void configure() {
        requestInjection(this);
    }

    @Inject
    void configureMapping(ElasticMapping mapping) throws IOException {
        mapping.tryCreate();
    }

    /**
     * Wait for elastic to be started
     *
     * @param config config
     * @return elastic RestClient
     */
    @Provides
    @Singleton
    RestClient provideClient(Config config) throws InterruptedException {
        String scheme = config.getString("elastic.scheme");
        String host = config.getString("elastic.hostname");
        int port = config.getInt("elastic.port");

        // Wait for host to be alive
        WaitFor.host(host, port, Duration.ofSeconds(160));
        // Wait another 15 seconds elastic search to be ready
        Thread.sleep(Duration.ofSeconds(15).toMillis());
        return RestClient.builder(new HttpHost(host, port, scheme)).build();
    }
}
