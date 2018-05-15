package munch.api.services.search;

import org.junit.jupiter.api.Test;

import java.time.Duration;

/**
 * Created by: Fuxing
 * Date: 5/3/2018
 * Time: 11:33 PM
 * Project: munch-core
 */
class FixedRandomSorterTest {

    @Test
    void sort() throws Exception {
        FixedRandomSorter sorter = new FixedRandomSorter(Duration.ofMinutes(1));
    }
}