package app.munch.elastic;

import app.munch.elastic.serializer.*;
import app.munch.model.ElasticDocumentType;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.MapBinder;
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

    @Override
    protected void configure() {
        MapBinder<ElasticDocumentType, Serializer> serializerBinder = MapBinder.newMapBinder(
                binder(), ElasticDocumentType.class, Serializer.class
        );

        serializerBinder.addBinding(ElasticDocumentType.ARTICLE).to(ArticleSerializer.class);
        serializerBinder.addBinding(ElasticDocumentType.LOCATION).to(LocationSerializer.class);
        serializerBinder.addBinding(ElasticDocumentType.PLACE).to(PlaceSerializer.class);
        serializerBinder.addBinding(ElasticDocumentType.PROFILE).to(ProfileSerializer.class);
        serializerBinder.addBinding(ElasticDocumentType.PUBLICATION).to(PublicationSerializer.class);
        serializerBinder.addBinding(ElasticDocumentType.TAG).to(TagSerializer.class);
    }

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
