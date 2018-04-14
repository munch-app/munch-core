package munch.api.services.discover;

import munch.data.structure.Place;
import munch.data.structure.SearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.time.Duration;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by: Fuxing
 * Date: 5/3/2018
 * Time: 11:25 PM
 * Project: munch-core
 */
public class FixedRandomSorter {
    private static final Logger logger = LoggerFactory.getLogger(FixedRandomSorter.class);
    private static final Pattern TIME_PATTERN = Pattern.compile(":");

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

    public void sort(List<Place> dataList, SearchQuery query) {
        if (dataList.isEmpty()) return;

        Random random = getRandom();

        for (Place place : dataList) {
            double next = 1.05 - (random.nextDouble() * 0.1);
            double ranking = place.getRanking() * next;
            place.setRanking(ranking);
        }

        if (hasHour(query)) {
            String day = query.getUserInfo().getDay();
            try {
                int time = timeAsInt(query.getUserInfo().getTime());

                for (Place place : dataList) {
                    double ranking = place.getRanking() * getHourBoost(place, day, time);
                    place.setRanking(ranking);
                }
            } catch (ParseException e) {
                logger.warn("Time parse Exception", e);
            }
        }


        dataList.sort((o1, o2) -> Double.compare(o2.getRanking(), o1.getRanking()));
    }

    private static boolean hasHour(SearchQuery query) {
        if (query.getUserInfo() == null) return false;
        if (query.getUserInfo().getDay() == null) return false;
        if (query.getUserInfo().getTime() == null) return false;
        return true;
    }

    private static double getHourBoost(Place place, String day, int time) {
        if (place.getHours() == null || place.getHours().isEmpty()) return 1.0;


        boolean isOpen = place.getHours().stream()
                .filter(hour -> hour.getDay().equalsIgnoreCase(day))
                .anyMatch(hour -> {
                    try {
                        int open = timeAsInt(hour.getOpen());
                        int close = timeAsInt(hour.getClose());
                        return open <= time && close >= time;
                    } catch (ParseException e) {
                        return false;
                    }
                });

        return isOpen ? 1.25 : 0.8;
    }

    private static int timeAsInt(String time) throws ParseException {
        try {
            String[] split = TIME_PATTERN.split(time);
            int hour = Integer.parseInt(split[0]);
            int min = Integer.parseInt(split[1]);
            return (hour * 60) + min;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | NullPointerException e) {
            throw new ParseException("time", 0);
        }
    }
}
