package munch.api.services.search;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

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
        List<String> list = new ArrayList<>(List.of(
                "1", "2", "3", "4", "5", "6"
        ));
        sorter.sort(list);
        System.out.println(list);
        // [2, 4, 3, 5, 1, 6] 11:35
    }
}