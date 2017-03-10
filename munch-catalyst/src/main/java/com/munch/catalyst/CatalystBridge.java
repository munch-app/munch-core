package com.munch.catalyst;

import com.catalyst.client.CatalystConsumer;
import com.corpus.exception.Retriable;
import com.corpus.exception.SleepRetriable;
import com.corpus.object.GroupObject;
import com.google.inject.*;
import com.google.inject.name.Named;
import com.munch.hibernate.utils.TransactionProvider;
import com.munch.struct.EntityPlace;
import com.munch.struct.module.ElasticModule;
import com.munch.struct.module.PostgresModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

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

    private final TransactionProvider provider;
    private final MunchPersist persist;
    private final Config config;

    private CatalystConsumer consumer;

    /**
     * Munch catalyst consumer, prepared from application.conf
     *
     * @param provider core munch database transaction provider
     * @param persist munch persist
     */
    @Inject
    public CatalystBridge(@Named("struct") TransactionProvider provider, MunchPersist persist) {
        this.config = ConfigFactory.load().getConfig("munch.catalyst");
        this.provider = provider;
        this.persist = persist;

        // Max retry for 3 days interval of 15 minutes before timeout
        this.retriable = new SleepRetriable(3 * 24 * 4, TimeUnit.MINUTES, 15);
        this.sleepDuration = config.getDuration("consumer.sleep");
    }

    /**
     * Start consumer by checking existing last place
     *
     * @throws IOException from consumer
     */
    public void initialize() throws IOException {
        EntityPlace lastPlace = provider.optional(em -> em.createQuery("SELECT p FROM EntityPlace p " +
                "ORDER BY p.id DESC , p.updatedDate DESC", EntityPlace.class)
                .setMaxResults(1).getSingleResult()).orElse(null);

        // Prepare consumer from config
        this.consumer = new CatalystConsumer(
                config.getString("consumer.url"),
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
     * @param modules guice modules for bridge system
     */
    public static void start(Module... modules) throws IOException {
        Injector injector = Guice.createInjector(modules);
        CatalystBridge bridge = injector.getInstance(CatalystBridge.class);
        bridge.initialize();
        bridge.connect();
    }

    public static void main(String[] args) throws IOException {
        start(new PostgresModule(), new ElasticModule());
    }
}
