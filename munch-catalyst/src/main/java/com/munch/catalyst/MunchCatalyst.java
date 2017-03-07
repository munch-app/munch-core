package com.munch.catalyst;

import com.catalyst.client.CatalystConsumer;
import com.munch.hibernate.utils.TransactionProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:42 AM
 * Project: munch-core
 */
public class MunchCatalyst {

    private final TransactionProvider provider;
    private final CatalystConsumer consumer;

    /**
     * Munch catalyst consumer, prepared from application.conf
     *
     * @param provider core munch database transaction provider
     */
    public MunchCatalyst(TransactionProvider provider) {
        this.provider = provider;
        Config config = ConfigFactory.load().getConfig("munch.catalyst");

        // Prepare consumer from config
        String clientUrl = config.getString("consumer.url");
        int size = config.getInt("consumer.size");
        this.consumer = new CatalystConsumer(clientUrl, size, new MunchGroupPersist(provider));
    }

    private void start() {
        provider.reduce(em -> {
            return null;
        });
    }

    public void run() {
        // TODO run consumer with
        // 1. Saving of date and group key for next
        // 2. Ability to handle exceptions
        // 3. Saving to core database
        // 4. Saving to elastic search
        // 5. Saving to elastic cache?
        // 6. Ability to do sleep within cycles
    }
}
