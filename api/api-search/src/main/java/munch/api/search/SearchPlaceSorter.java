package munch.api.search;

import munch.api.search.elastic.ElasticQueryUtils;
import munch.data.Hour;
import munch.data.place.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by: Fuxing
 * Date: 5/3/2018
 * Time: 11:25 PM
 * Project: munch-core
 */
@Singleton
public final class SearchPlaceSorter {
    private static final Logger logger = LoggerFactory.getLogger(SearchPlaceSorter.class);
    private static final Pattern TIME_PATTERN = Pattern.compile(":");

    private final long interval;

    @Inject
    public SearchPlaceSorter() {
        this(Duration.ofHours(24 + 18));
    }

    public SearchPlaceSorter(Duration interval) {
        this.interval = interval.toMillis();
    }

    private long getSeed() {
        return System.currentTimeMillis() / interval;
    }

    private Random getRandom() {
        return new Random(getSeed());
    }

    public List<Place> sort(List<Place> dataList, SearchRequest request) {
        if (dataList.isEmpty()) return List.of();
        dataList = new ArrayList<>(dataList);

        Random random = getRandom();

        for (Place place : dataList) {
            double next = 1.05 - (random.nextDouble() * 0.1);
            double ranking = place.getRanking() * next;
            place.setRanking(ranking);
        }

        LocalDateTime localTime = request.getLocalTime();
        if (localTime != null) {
            DayOfWeek dayOfWeek = localTime.getDayOfWeek();
            int time = localTime.getHour() + (localTime.getMinute() * 60);

            for (Place place : dataList) {
                double ranking = place.getRanking() * getHourBoost(place, dayOfWeek, time);
                ranking = ranking * getTagBoost(place, time);
                place.setRanking(ranking);
            }
        }

        dataList.sort((o1, o2) -> Double.compare(o2.getRanking(), o1.getRanking()));
        return dataList;
    }

    private static double getHourBoost(Place place, DayOfWeek dayOfWeek, int time) {
        if (place.getHours() == null || place.getHours().isEmpty()) return 1.0;

        boolean isOpen = place.getHours().stream()
                .filter(hour -> hour.getDay() == Hour.Day.parse(dayOfWeek))
                .anyMatch(hour -> {
                    int open = ElasticQueryUtils.timeAsInt(hour.getOpen());
                    int close = ElasticQueryUtils.timeAsInt(hour.getClose());
                    return open <= time && close >= time;
                });

        return isOpen ? 1.10 : 0.925;
    }

    /**
     * PM-128
     * De-prioritise establishments that are only tagged with 'Desserts' during 12.00pm - 13.30pm and 18.00pm - 19.30pm daily.
     */
    private static double getTagBoost(Place place, int time) {
        if (time < 720) return 1.0;
        if (time < 1080 && time > 810) return 1.0;
        if (time > 1170) return 1.0;

        for (Place.Tag tag : place.getTags()) {
            if ("Dessert".equalsIgnoreCase(tag.getName())) return 0.9;
        }
        return 1.0;
    }
}
