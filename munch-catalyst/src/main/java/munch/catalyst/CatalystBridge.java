package munch.catalyst;

import com.catalyst.client.CatalystClient;
import com.catalyst.client.CatalystConsumer;
import com.corpus.exception.Retriable;
import com.corpus.exception.SleepRetriable;
import com.corpus.object.GroupObject;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.catalyst.service.PlaceClient;
import munch.catalyst.service.PostgresPlace;
import munch.catalyst.service.ServicesModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:42 AM
 * Project: munch-core
 */
@Singleton
public class CatalystBridge {
    private static final Logger logger = LoggerFactory.getLogger(CatalystBridge.class);

    private final Duration sleepDuration;
    private final Retriable retriable;

    private final PlaceClient placeClient;
    private final MunchPersist persist;
    private final Config config;

    private CatalystConsumer consumer;

    /**
     * Munch catalyst consumer, prepared from application.conf
     *
     * @param placeClient data client from munch-service-data
     * @param persist    munch persist
     * @param config     catalyst config
     */
    @Inject
    public CatalystBridge(PlaceClient placeClient, MunchPersist persist, Config config) {
        this.placeClient = placeClient;
        this.persist = persist;
        this.config = config;

        // Max retry for 2 days interval of 15 minutes before timeout
        this.retriable = new SleepRetriable(2 * 24 * 4, Duration.ofMinutes(15));
        this.sleepDuration = this.config.getDuration("consumer.sleep");
    }

    /**
     * Start consumer by checking existing last place
     *
     * @throws IOException from consumer
     */
    public void initialize() throws IOException {
        PostgresPlace lastPlace = placeClient.latest();

        // Prepare consumer from config
        this.consumer = new CatalystConsumer(
                createClient(config.getConfig("consumer")),
                config.getInt("consumer.size"),
                lastPlace != null ? lastPlace.getUpdatedDate() : new Date(0),
                lastPlace != null ? lastPlace.getId() : null
        );

        if (lastPlace != null) {
            logger.info("Started munch catalyst consumer with existing data. last id: {}", lastPlace.getId());
        } else {
            logger.info("Started munch catalyst consumer fresh with no existing data.");
        }
    }

    /**
     * Start running
     * Main interface
     */
    public void connect() {
        Objects.requireNonNull(consumer, "Catalyst bridge needs to be initialized.");

        while (!Thread.currentThread().isInterrupted()) {
            // Retry loop catalyst consumer
            List<GroupObject> groups = retriable.loop(consumer::next);

            // Retry loop munch persist
            retriable.loop(() -> persist.persist(groups));

            // Check if has next, if no has next go sleep for awhile
            if (!consumer.hasNext()) {
                try {
                    logger.info("Sleeping for {} as there is no next", sleepDuration);
                    Thread.sleep(sleepDuration.toMillis());
                } catch (InterruptedException e) {
                    logger.warn("Munch catalyst interrupted");
                }
            }
        }
        logger.info("Munch catalyst stopped");
    }

    /**
     * @param config consumer config
     * @return Create Catalyst Client
     */
    private static CatalystClient createClient(Config config) {
        String catalystUrl = config.getString("url");
        if (config.hasPath("user")) {
            String user = config.getString("user");
            String password = config.getString("password");
            return new CatalystClient(catalystUrl, user, password);
        } else {
            return new CatalystClient(catalystUrl);
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Starting in 15 seconds.");
        Thread.sleep(15000);

        Injector injector = Guice.createInjector(new CatalystModule(), new ServicesModule());
        CatalystBridge bridge = injector.getInstance(CatalystBridge.class);
        bridge.initialize();
        bridge.connect();
    }
}
