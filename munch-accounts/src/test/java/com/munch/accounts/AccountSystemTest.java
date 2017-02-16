package com.munch.accounts;

import com.munch.utils.spark.SparkServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created By: Fuxing Loh
 * Date: 7/2/2017
 * Time: 4:06 PM
 * Project: munch-core
 */
class AccountSystemTest {

    static SparkServer server;

    @BeforeEach
    void setUp() throws Exception {
        // TODO MA-30 Protected Testing
//        server = new SparkServer(AccountSystem.controllers());
        server.start(1);
    }

    @Test
    void authProtected() throws Exception {

    }

    @Test
    void tokenProtected() throws Exception {

    }

    @Test
    void publicAccess() throws Exception {

    }
}