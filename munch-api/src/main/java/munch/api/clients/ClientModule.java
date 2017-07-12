package munch.api.clients;

import catalyst.utils.exception.Retriable;
import catalyst.utils.exception.SleepRetriable;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.WaitFor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger logger = LoggerFactory.getLogger(ClientModule.class);

    @Override
    protected void configure() {
        requestInjection(this);
    }

    @Inject
    void waitFor(@Named("services") Config services) {
        WaitFor.host(services.getString("articles.url"), Duration.ofSeconds(60));
        WaitFor.host(services.getString("instagram.url"), Duration.ofSeconds(60));
        WaitFor.host(services.getString("images.url"), Duration.ofSeconds(60));
        WaitFor.host(services.getString("places.url"), Duration.ofSeconds(100));
        WaitFor.host(services.getString("search.url"), Duration.ofSeconds(180));
    }

    /**
     * Wait for 200 seconds for nominatim to be ready
     *
     * @param services services config
     */
    @Inject
    void waitForNomination(@Named("services") Config services) throws UnirestException {
        String url = services.getString("nominatim.url");

        WaitFor.host(url, Duration.ofSeconds(200));
        Retriable retriable = new SleepRetriable(15, Duration.ofSeconds(20), (throwable, integer) -> {
            logger.info("Waiting for {} to be ready.", url);
        });
        retriable.loop(() -> new GetRequest(HttpMethod.GET,
                url + "/reverse?format=json").asJson().getBody());
    }

    @Provides
    @Named("services")
    Config provideConfig() {
        return ConfigFactory.load().getConfig("services");
    }
}
