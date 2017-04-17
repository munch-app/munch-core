package munch.catalyst;

import munch.data.PostgresModule;

import java.io.IOException;

/**
 * Created By: Fuxing Loh
 * Date: 15/3/2017
 * Time: 6:07 PM
 * Project: munch-core
 */
public class PostgresOnlyTest {

    public static void main(String[] args) throws IOException {
        CatalystBridge.start(new PostgresModule(), new NoSearchModule());
    }

}
