package com.munch.catalyst;

import com.catalyst.CatalystConsumer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 15/2/2017
 * Time: 2:42 AM
 * Project: munch-core
 */
public class MunchCatalyst {

    private final CatalystConsumer consumer;

    public MunchCatalyst(final String url) {
        Config config = ConfigFactory.load().getConfig("munch.catalyst");
        String clientUrl = config.getString("consumer.url");
        int size = config.getInt("consumer.size");
        this.consumer = new CatalystConsumer(clientUrl, size, new MunchPersistStore());
    }

    public void run() throws IOException {
        // TODO run consumer with
        // 1. Saving of date and group key for next
        // 2. Ability to handle exceptions
        // 3. Saving to core database
        // 4. Saving to elastic search
        // 5. Saving to elastic cache?
        // 6. Ability to do sleep within cycles
    }
}
