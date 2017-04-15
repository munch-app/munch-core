package munch.catalyst;

import munch.document.PostgresModule;
import munch.search.elastic.ElasticModule;

import java.io.IOException;

/**
 * Created by: Fuxing
 * Date: 8/3/2017
 * Time: 1:59 AM
 * Project: munch-core
 */
class CatalystBridgeTest {
    public static void main(String[] args) throws IOException {
        CatalystBridge.start(new PostgresModule(), new ElasticModule());
    }
}