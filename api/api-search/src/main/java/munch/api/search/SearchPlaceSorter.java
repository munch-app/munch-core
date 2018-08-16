package munch.api.search;

import munch.api.search.elastic.ElasticQueryUtils;
import munch.data.Hour;
import munch.data.client.TagClient;
import munch.data.place.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 5/3/2018
 * Time: 11:25 PM
 * Project: munch-core
 */
@Singleton
public final class SearchPlaceSorter {
    private static final Logger logger = LoggerFactory.getLogger(SearchPlaceSorter.class);
    private final long interval = Duration.ofHours(24 + 18).toMillis();

    private Set<String> tagLevel2 = new HashSet<>();
    private Set<String> tagLevel3 = new HashSet<>();

    @Inject
    public SearchPlaceSorter(TagClient tagClient) {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            Set<String> level2 = new HashSet<>();
            Set<String> level3 = new HashSet<>();

            tagClient.iterator().forEachRemaining(tag -> {
                if (tag.getPlace().getLevel() == 2) {
                    tagLevel2.add(tag.getTagId());
                } else if (tag.getPlace().getLevel() == 3) {
                    tagLevel3.add(tag.getTagId());
                }
            });

            tagLevel2 = level2;
            tagLevel3 = level3;
        }, 0, 24, TimeUnit.HOURS);
    }

    private Random getRandom() {
        return new Random(System.currentTimeMillis() / interval);
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
        if (localTime != null) sort(dataList, localTime);

        dataList.sort((o1, o2) -> Double.compare(o2.getRanking(), o1.getRanking()));
        return dataList;
    }

    public void sort(List<Place> dataList, LocalDateTime localTime) {
        DayOfWeek dayOfWeek = localTime.getDayOfWeek();
        int time = localTime.getHour() + (localTime.getMinute() * 60);

        for (Place place : dataList) {
            double ranking = place.getRanking() * getHourBoost(place, dayOfWeek, time);
            ranking = ranking * getTagBoost(place, time);
            place.setRanking(ranking);
        }
    }

    private double getHourBoost(Place place, DayOfWeek dayOfWeek, int time) {
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
     * PM-133
     * Level 2:
     * 1. Places that are only tagged with 'Snacks' or 'Bakery' will be deprioritized during 12.00pm - 1.30pm (lunch hours) and 6.00pm - 7.30pm (dinner hours).
     * - If a place has more than one tag within the same category (e.g. Bakery and Cafe), it will not be deprioritized.
     * <p>
     * Level 3:
     * 2. Places that are only tagged with 'Alcohol' will be deprioritized until after 8.30pm.
     */
    private double getTagBoost(Place place, int time) {
        // 12:00-13:00, 18:00-19:30
        if ((time >= 720 && time <= 780) || (time >= 1080 && time <= 1170)) {
            if (containsTag(place, tagLevel2, "950870d5-416f-42df-96f5-cf207bb02aea", "c0507377-11a2-4ab3-a933-d2d41392c6d9")) {
                return 0.8;
            }
        }

        // 01:00 - 20:30
        if (time >= 60 && time <= 1230) {
            if (containsTag(place, tagLevel3, "8e8ac48d-0052-4732-9f69-969cf9c5d109")) {
                return 0.8;
            }
        }

        return 1.0;
    }

    private static boolean containsTag(Place place, Set<String> levels, String... tagIds) {
        Set<String> filtered = place.getTags().stream()
                .filter(tag -> levels.contains(tag.getTagId()))
                .map(Place.Tag::getTagId)
                .collect(Collectors.toSet());

        // No Filtered = completely empty
        if (filtered.isEmpty()) return false;

        if (contains(filtered, tagIds)) {
            filtered.removeIf(s -> {
                for (String tagId : tagIds) {
                    if (s.equals(tagId)) return true;
                }
                return false;
            });
            return filtered.isEmpty();
        }

        return false;
    }

    private static boolean contains(Set<String> filtered, String... tagIds) {
        for (String tagId : tagIds) {
            if (filtered.contains(tagId)) return true;
        }
        return false;
    }
}
