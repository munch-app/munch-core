package munch.catalyst.clients;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mashape.unirest.http.Unirest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.WaitFor;

import javax.inject.Inject;
import java.time.Duration;

/**
 * In theses clients: catalystId is also know also placeId
 * <p>
 * Created by: Fuxing
 * Date: 15/4/2017
 * Time: 3:44 AM
 * Project: munch-core
 */
public class ClientModule extends AbstractModule {

    @Override
    protected void configure() {
        requestInjection(this);
        Unirest.setTimeouts(60000, 600000);

        Config services = ConfigFactory.load().getConfig("services");

        // Bind all service url String from config
        bind(String.class).annotatedWith(Names.named("services.places.url"))
                .toInstance(services.getString("places.url"));
        bind(String.class).annotatedWith(Names.named("services.articles.url"))
                .toInstance(services.getString("articles.url"));
        bind(String.class).annotatedWith(Names.named("services.instagram.url"))
                .toInstance(services.getString("instagram.url"));
        bind(String.class).annotatedWith(Names.named("services.search.url"))
                .toInstance(services.getString("search.url"));
    }

    @Inject
    void waitFor(Config config) {
        WaitFor.host(config.getString("services.instagram.url"), Duration.ofSeconds(60));
        WaitFor.host(config.getString("services.articles.url"), Duration.ofSeconds(60));
        WaitFor.host(config.getString("services.places.url"), Duration.ofSeconds(80));
        WaitFor.host(config.getString("services.search.url"), Duration.ofSeconds(180));
    }
}
