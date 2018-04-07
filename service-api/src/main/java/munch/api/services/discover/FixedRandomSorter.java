package munch.api.services.discover;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by: Fuxing
 * Date: 5/3/2018
 * Time: 11:25 PM
 * Project: munch-core
 */
public class FixedRandomSorter {

    private final long interval;

    public FixedRandomSorter(Duration interval) {
        this.interval = interval.toMillis();
    }

    private long getSeed() {
        return System.currentTimeMillis() / interval;
    }

    private Random getRandom() {
        return new Random(getSeed());
    }

    public <T> void sort(List<T> dataList) {
        if (dataList.isEmpty()) return;

        Random random = getRandom();
        Collections.shuffle(dataList, random);
    }
}