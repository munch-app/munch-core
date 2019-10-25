package app.munch.elastic;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:49 PM
 * Project: munch-core
 */
public final class ElasticModule extends AbstractModule {

    @Inject
    void shutdownHook(RestHighLevelClient client) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                client.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Provides
    @Singleton
    SqsClient provideSqsClient() {
        return SqsClient.builder()
                .build();
    }

    @Provides
    @Singleton
    RestHighLevelClient provideRestHighLevelClient() {
        Config config = ConfigFactory.load().getConfig("services.elastic");

        return new RestHighLevelClient(RestClient.builder(
                new HttpHost(config.getString("hostname"), config.getInt("port"), config.getString("scheme"))
        ));
    }
}
