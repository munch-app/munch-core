package munch.catalyst;

import com.catalyst.client.CatalystConsumer;
import com.corpus.exception.Retriable;
import com.corpus.exception.SleepRetriable;
import com.corpus.object.GroupObject;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.catalyst.clients.ServiceModule;
import munch.catalyst.data.DataModule;
import munch.catalyst.data.MultiConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:42 AM
 * Project: munch-core
 */
@Singleton
public class CatalystBridge {
    private static final Logger logger = LoggerFactory.getLogger(CatalystBridge.class);

    // Max retry for 2 days interval of 15 minutes before timeout
    private final Retriable retriable = new SleepRetriable(2 * 24 * 4, Duration.ofMinutes(15));

    private final CatalystConsumer catalyst;
    private final MultiConsumer dataConsumer;

    private final Duration sleepDuration;

    /**
     * Munch catalyst consumer, prepared from application.conf
     *
     * @param placeClient data client from munch-service-data
     * @param persist     munch persist
     * @param consumer
     * @param config      catalyst config
     * @param catalyst
     */
    @Inject
    public CatalystBridge(CatalystConsumer catalyst, MultiConsumer consumer, Config config) {
        this.catalyst = catalyst;
        this.dataConsumer = consumer;

        this.sleepDuration = config.getDuration("catalyst.sleep");
    }


    /**
     * Start running
     * Main interface
     */
    public void connect() {
        while (!Thread.currentThread().isInterrupted()) {
            // Retry loop catalyst consumer
            List<GroupObject> groups = retriable.loop(catalyst::next);

            // Retry loop data consumer; persist
            retriable.loop(() -> dataConsumer.consume(groups));

            // Check if has next, if no has next go sleep for awhile
            if (!catalyst.hasNext()) {
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

    public static void main(String[] args) throws IOException, InterruptedException {
        logger.info("Starting in 15 seconds.");
        Thread.sleep(15000);

        Injector injector = Guice.createInjector(
                new DataModule(),
                new CatalystModule(),
                new ServiceModule()
        );

        // Connect and start!
        injector.getInstance(CatalystBridge.class).connect();
    }
}
