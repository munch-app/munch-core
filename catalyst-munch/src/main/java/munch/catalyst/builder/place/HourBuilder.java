package munch.catalyst.builder.place;

import corpus.data.CorpusData;
import munch.data.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 5/7/2017
 * Time: 4:25 PM
 * Project: munch-core
 */
public final class HourBuilder implements TypeBuilder {
    private static final Logger logger = LoggerFactory.getLogger(HourBuilder.class);
    private static final Pattern HourKeysPattern = Pattern.compile("Place\\.Hour\\.\\w+");

    private Map<String, Set<Place.Hour>> map = new HashMap<>();

    /**
     * Place.Hour.mon-sun, ph, evePh, raw
     *
     * @param data  link that provided hour data
     * @param field hour field
     */
    @Override
    public void add(CorpusData data, CorpusData.Field field) {
        Set<Place.Hour> hours = map.computeIfAbsent(data.getCorpusKey(), s -> new HashSet<>());
        String day = parseDay(field.getKey());
        if (day != null) {
            String[] range = field.getValue().split("-");
            Place.Hour hour = new Place.Hour();
            hour.setOpen(range[0]);
            hour.setClose(range[1]);
            hour.setDay(day);
            hours.add(hour);
        }
    }

    /**
     * Future: remove evePh and ph for better comparision
     *
     * @return get most appearance hour list
     */
    public List<Place.Hour> collect() {
        if (map.isEmpty()) return Collections.emptyList();
        Map<Set<Place.Hour>, Integer> values = new HashMap<>();
        for (Set<Place.Hour> hours : map.values()) {
            values.compute(hours, (h, i) -> i == null ? 1 : i + 1);
        }

        return values.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .map(ArrayList::new)
                .orElseGet(ArrayList::new);
    }

    private boolean equals(Set<Place.Hour> left, Set<Place.Hour> right) {
        left = left.stream().filter(hour -> isMonToSun(hour.getDay()))
                .collect(Collectors.toSet());

        right = right.stream().filter(hour -> isMonToSun(hour.getDay()))
                .collect(Collectors.toSet());

        return left.equals(right);
    }

    @Nullable
    public String parseDay(String key) {
        switch (key) {
            case "Place.Hour.mon":
                return "mon";
            case "Place.Hour.tue":
                return "tue";
            case "Place.Hour.wed":
                return "wed";
            case "Place.Hour.thu":
                return "thu";
            case "Place.Hour.fri":
                return "fri";
            case "Place.Hour.sat":
                return "sat";
            case "Place.Hour.sun":
                return "sun";
            case "Place.Hour.ph":
                return "ph";
            case "Place.Hour.evePh":
                return "evePh";
            default:
                logger.warn("Unable to parse day: {}", key);
            case "Place.Hour.raw":
                return null;
        }
    }

    /**
     * Check if day is mon - fri
     *
     * @param day day to check
     * @return true if is
     */
    private boolean isMonToSun(String day) {
        switch (day) {
            case "mon":
            case "tue":
            case "wed":
            case "thu":
            case "fri":
            case "sat":
            case "sun":
                return true;
            default:
                return false;
        }
    }

    /**
     * Check if field is hour keys
     *
     * @param field field to match
     * @return true if field key matched
     * @see HourBuilder#HourKeysPattern
     */
    public boolean match(CorpusData.Field field) {
        return HourKeysPattern.matcher(field.getKey()).matches();
    }
}
