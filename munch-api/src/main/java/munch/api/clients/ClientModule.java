package munch.api.clients;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.client.WaitFor;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;

/**
 * Created by: Fuxing
 * Date: 15/4/2017
 * Time: 3:44 AM
 * Project: munch-core
 */
public class ClientModule extends AbstractModule {

    @Override
    protected void configure() {
        requestInjection(this);
    }

    @Inject
    void waitFor(@Named("services") Config services) {
        WaitFor.host(services.getString("articles.url"), Duration.ofSeconds(60));
        WaitFor.host(services.getString("gallery.url"), Duration.ofSeconds(60));
        WaitFor.host(services.getString("images.url"), Duration.ofSeconds(60));
        WaitFor.host(services.getString("geocoder.url"), Duration.ofSeconds(60));
        WaitFor.host(services.getString("places.url"), Duration.ofSeconds(180));
    }

    @Provides
    @Named("services")
    Config provideConfig() {
        return ConfigFactory.load().getConfig("services");
    }
}
