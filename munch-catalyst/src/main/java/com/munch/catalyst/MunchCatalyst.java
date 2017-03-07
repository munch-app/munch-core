package com.munch.catalyst;

import com.catalyst.client.CatalystConsumer;
import com.corpus.exception.Retriable;
import com.corpus.exception.SleepRetriable;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.hibernate.utils.TransactionProvider;
import com.munch.struct.Place;
import com.munch.struct.module.DatabaseModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:42 AM
 * Project: munch-core
 */
@Singleton
public class MunchCatalyst {
    private static final Logger logger = LoggerFactory.getLogger(MunchCatalyst.class);

    private final Duration sleepDuration;

    private final TransactionProvider provider;
    private final CatalystConsumer consumer;
    private final Retriable retriable;

    /**
     * Munch catalyst consumer, prepared from application.conf
     *
     * @param provider core munch database transaction provider
     */
    @Inject
    public MunchCatalyst(@Named("struct") TransactionProvider provider, MunchGroupPersist groupPersist) {
        this.provider = provider;
        Config config = ConfigFactory.load().getConfig("munch.catalyst");

        // Max retry for 3 days interval of 15 minutes before timeout
        this.retriable = new SleepRetriable(3 * 24 * 4, TimeUnit.MINUTES, 15);

        // Prepare consumer from config
        this.sleepDuration = config.getDuration("consumer.sleep");
        String clientUrl = config.getString("consumer.url");
        int size = config.getInt("consumer.size");
        this.consumer = new CatalystConsumer(clientUrl, size, groupPersist);
    }

    /**
     * Start consumer by checking existing last place
     *
     * @throws IOException from consumer
     */
    private boolean start() throws IOException {
        Place lastPlace = provider.optional(em -> em.createQuery("SELECT p FROM Place p " +
                "ORDER BY p.id DESC , p.updatedDate DESC", Place.class)
                .setMaxResults(1)
                .getSingleResult()).orElse(null);

        // Start new
        if (lastPlace == null) {
            logger.info("Started munch catalyst consumer fresh with no existing data.");
            return consumer.start(new Date(0), null);
        }

        // Start from existing last place
        logger.info("Started munch catalyst consumer with existing data. last id: {}", lastPlace.getId());
        return consumer.start(lastPlace.getUpdatedDate(), lastPlace.getId());
    }

    /**
     * Start running
     * Main interface
     */
    public void run() {
        try {
            start();
        } catch (IOException e) {
            logger.error("Unable to start.");
            throw new RuntimeException(e);
        }

        while (!Thread.currentThread().isInterrupted()) {
            boolean hasNext = retriable.loop(consumer::next);

            // Check if has next, if no has next go sleep for awhile
            if (!hasNext) {
                logger.info("Sleeping for {} as there is no next", sleepDuration);
                try {
                    Thread.sleep(sleepDuration.toMillis());
                } catch (InterruptedException e) {
                    logger.warn("Munch catalyst interrupted");
                }
            }
        }
        logger.info("Munch catalyst stopped");
    }

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(
                new DatabaseModule()
        );

        MunchCatalyst catalyst = injector.getInstance(MunchCatalyst.class);
        catalyst.run();
    }
}
