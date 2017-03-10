package com.munch.catalyst;

import com.munch.struct.module.ElasticModule;
import com.munch.struct.module.PostgresModule;
import org.junit.jupiter.api.Test;

/**
 * Created by: Fuxing
 * Date: 8/3/2017
 * Time: 1:59 AM
 * Project: munch-core
 */
class CatalystBridgeTest {

    @Test
    void run() throws Exception {
        CatalystBridge.start(new PostgresModule(), new ElasticModule());
    }

}