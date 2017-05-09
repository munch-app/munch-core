package munch.catalyst;

import com.catalyst.client.CatalystClient;
import com.catalyst.client.CatalystConsumer;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 10:02 PM
 * Project: munch-core
 */
public class CatalystModule extends AbstractModule {
    private static final Logger logger = LoggerFactory.getLogger(CatalystModule.class);

    @Override
    protected void configure() {

    }

    @Provides
    CatalystClient provideCatalystClient(Config config) {
        String catalystUrl = config.getString("catalyst.url");
        if (!config.hasPath("catalyst.user")) return new CatalystClient(catalystUrl);

        // Password protected
        String user = config.getString("catalyst.user");
        String password = config.getString("catalyst.password");
        return new CatalystClient(catalystUrl, user, password);
    }

    @Provides
    CatalystConsumer provideCatalystConsumer(CatalystClient client, Config config) {
        /*
        if (lastPlace != null) {
            logger.info("Started munch catalyst consumer with existing data. last id: {}", lastPlace.getId());
        } else {
            logger.info("Started munch catalyst consumer fresh with no existing data.");
        }
         */
        // TODO date and after key
        return new CatalystConsumer(client, config.getInt("catalyst.size"),
                new Date(0), null);
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }
}
