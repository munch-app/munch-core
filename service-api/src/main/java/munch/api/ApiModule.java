package munch.api;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.api.services.ServiceModule;
import munch.data.dynamodb.DynamoModule;
import munch.data.elastic.ElasticModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by: Fuxing
 * Date: 25/3/2017
 * Time: 7:45 PM
 * Project: munch-core
 */
public class ApiModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(ApiModule.class);

    @Override
    protected void configure() {
        install(new ServiceModule());
        install(new DynamoModule());
        install(new ElasticModule());
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    /**
     * Start api server with predefined modules
     *
     * @param args not required
     */
    public static void main(String[] args) {
        try {
            Injector injector = Guice.createInjector(new ApiModule());
            injector.getInstance(ApiServer.class).start();
        } catch (Exception e) {
            logger.error("Error Starting ApiModule", e);
            throw e;
        }
    }
}