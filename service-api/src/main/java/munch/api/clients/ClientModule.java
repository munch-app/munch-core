package munch.api.clients;

import catalyst.utils.exception.Retriable;
import catalyst.utils.exception.SleepRetriable;
import com.google.inject.AbstractModule;
import com.mashape.unirest.http.HttpMethod;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.typesafe.config.Config;
import munch.restful.WaitFor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
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

    /**
     * Wait for 200 seconds for nominatim to be ready
     *
     * @param config config
     */
    @Inject
    void waitForNomination(Config config) throws UnirestException {
        String url = config.getString("services.nominatim.url");

        WaitFor.host(url, Duration.ofSeconds(200));
        Retriable retriable = new SleepRetriable(30, Duration.ofSeconds(30), (throwable, integer) -> {
            logger.info("Waiting for {} to be ready.", url);
        });
        retriable.loop(() -> new GetRequest(HttpMethod.GET, url + "/reverse?format=json").asJson().getBody());
    }
}
