package munch.restful.server;

import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 11:18 PM
 * Project: munch-core
 */
class WaitForTest {

    @Test
    void test() throws Exception {
        WaitFor.host("jdbc:postgresql://localhost:5444/postgres".substring(5), Duration.ofSeconds(10));
    }
}