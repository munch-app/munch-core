package com.munch.catalyst;

import org.junit.jupiter.api.Test;

/**
 * Created By: Fuxing Loh
 * Date: 15/3/2017
 * Time: 6:07 PM
 * Project: munch-core
 */
public class PostgresOnlyTest {

    @Test
    void run() throws Exception {
        CatalystBridge.start(new AutoCreatePostgresModule(), new EmptySearchModule());
    }

}
