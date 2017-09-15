package munch.api;

import org.junit.jupiter.api.Test;

/**
 * Created by: Fuxing
 * Date: 10/9/2017
 * Time: 3:00 PM
 * Project: munch-core
 */
class ValidateVersionTest {

    @Test
    void versions() throws Exception {
        String versions = "1.0.0, 0.0";

        String[] split = versions.split(" *, *");
        for (String s : split) {
            System.out.println("(" + s + ")");
        }
    }
}