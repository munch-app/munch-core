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
import munch.restful.server.auth0.Auth0AuthenticationModule;
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

        Config config = ConfigFactory.load();
        final String issuer = config.getString("services.auth0.issuer");

        install(new Auth0AuthenticationModule("https://api.partner.munchapp.co/", issuer));
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
        Injector injector = Guice.createInjector(new ApiModule());
        injector.getInstance(ApiServer.class).start();
    }
}