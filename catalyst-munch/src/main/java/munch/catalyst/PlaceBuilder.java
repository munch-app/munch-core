package munch.catalyst;

import catalyst.data.CorpusData;
import munch.catalyst.data.Place;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:33 AM
 * Project: munch-core
 */
public final class PlaceBuilder {
    private static final Logger logger = LoggerFactory.getLogger(PlaceBuilder.class);

    private static final Pattern HourKeysPattern = Pattern.compile("Place\\.Hour\\.\\w+");
    private static final String PriceKey = "Place.price";

    private final PriceBuilder priceBuilder;
    private final HourBuilder hourBuilder;
    private final ValueBuilder valueBuilder;

    private String id;
    private Date earliestDate = new Date();

    public PlaceBuilder() {
        this.priceBuilder = new PriceBuilder();
        this.hourBuilder = new HourBuilder();
        this.valueBuilder = new ValueBuilder();
    }

    public void consume(CorpusData data) {
        if (id == null) id = data.getCatalystId();
        if (!id.equals(data.getCatalystId()))
            throw new IllegalArgumentException("CatalystLink catalystId is different, unable to build Place.");

        // Find earliest date
        Date createdDate = data.getCreatedDate();
        if (createdDate.compareTo(earliestDate) < 0) earliestDate = createdDate;

        for (CorpusData.Field field : data.getFields()) {
            String key = field.getKey();
            String value = field.getValue();

            // Use respective adders
            if (PriceKey.equals(key)) {
                priceBuilder.addPrices(value);
            } else if (HourKeysPattern.matcher(key).matches()) {
                hourBuilder.addHours(data, key, value);
            } else {
                valueBuilder.addValue(key, value);
            }
        }
    }

    /**
     * @return created Place value with consumed links
     */
    @Nullable
    public Place collect(Date updatedDate) {
        Place place = new Place();
        place.setId(id);

        place.setName(valueBuilder.collectMax("Place.name"));
        place.setPhone(valueBuilder.collectMax("Place.phone"));
        place.setWebsite(valueBuilder.collectMax("Place.website"));
        place.setDescription(valueBuilder.collectMax("Place.description"));

        place.setPrice(priceBuilder.collect());
        Place.Location location = new Place.Location();
        location.setAddress(valueBuilder.collectMax("Place.Location.address"));
        location.setUnitNumber(valueBuilder.collectMax("Place.Location.unitNumber"));

        location.setCity(valueBuilder.collectMax("Place.Location.city"));
        location.setCountry(valueBuilder.collectMax("Place.Location.country"));

        location.setPostal(valueBuilder.collectMax("Place.Location.postal"));
        String latLng = valueBuilder.collectMax("Place.Location.latLng");
        if (latLng != null) {
            String[] split = latLng.split(",");
            location.setLat(Double.parseDouble(split[0].trim()));
            location.setLng(Double.parseDouble(split[1].trim()));
        }
        place.setLocation(location);

        List<String> tags = new ArrayList<>();
        tags.addAll(valueBuilder.collect("Place.tag"));
        tags.addAll(valueBuilder.collect("Place.type"));
        place.setTags(tags);
        place.setHours(new ArrayList<>(hourBuilder.collect()));

        place.setCreatedDate(earliestDate);
        place.setUpdatedDate(updatedDate);

        // Return null if validation failed
        if (!validate(place)) return null;
        return place;
    }

    /**
     * Theses fields must not be blank:
     * <p>
     * place.id
     * place.name
     * <p>
     * place.location.postal
     * place.location.country
     * place.location.lat
     * place.location.lng
     * <p>
     * place.location.createdDate
     * place.location.updatedDate
     *
     * @param place place to validate
     * @return true = success
     */
    private boolean validate(Place place) {
        if (StringUtils.isBlank(place.getId())) return false;
        if (StringUtils.isBlank(place.getName())) return false;

        if (place.getLocation() == null) return false;
        if (StringUtils.isBlank(place.getLocation().getPostal())) return false;
        if (StringUtils.isBlank(place.getLocation().getCountry())) return false;
        if (place.getLocation().getLat() == null) return false;
        if (place.getLocation().getLng() == null) return false;

        if (place.getCreatedDate() == null) return false;
        if (place.getUpdatedDate() == null) return false;
        return true;
    }

    private class ValueBuilder {
        private Map<String, Map<String, Integer>> keyValueCount = new HashMap<>();

        /**
         * Put key value in multiKeyValue map
         *
         * @param key   key
         * @param value value
         */
        private void addValue(String key, String value) {
            Map<String, Integer> values = keyValueCount.computeIfAbsent(key, v -> new HashMap<>());
            values.compute(value, (v, c) -> c == null ? 1 : c + 1);
        }

        private Set<String> collect(String key) {
            return keyValueCount.getOrDefault(key, Collections.emptyMap()).keySet();
        }

        /**
         * @param key key to find
         * @return null if no values found for key
         */
        @Nullable
        private String collectMax(String key) {
            Map<String, Integer> values = keyValueCount.getOrDefault(key, Collections.emptyMap());
            return values.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }
    }

    private class PriceBuilder {
        private List<Double> prices = new ArrayList<>();

        /**
         * @param value price value in String
         */
        private void addPrices(String value) {
            String cleaned = value.replace("$", "")
                    .replace(" ", "");
            try {
                prices.add(Double.parseDouble(cleaned));
            } catch (NumberFormatException e) {
                logger.warn("Unable to parse price {}", value, e);
            }
        }

        @Nullable
        @SuppressWarnings("ConstantConditions")
        private Place.Price collect() {
            if (prices.isEmpty()) return null;
            double low = prices.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
            double high = prices.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
            Place.Price price = new Place.Price();
            price.setLowest(low);
            price.setHighest(high);
            return price;
        }
    }

    private class HourBuilder {
        private Map<String, Set<Place.Hour>> map = new HashMap<>();

        /**
         * Place.Hour.mon-sun, ph, evePh, raw
         *
         * @param data  link that provided hour data
         * @param key   key is day of hour
         * @param value value is time range
         */
        private void addHours(CorpusData data, String key, String value) {
            Set<Place.Hour> hours = map.computeIfAbsent(data.getCorpusKey(), s -> new HashSet<>());
            Place.Hour.Day day = parseDay(key);
            if (day != null) {
                String[] range = value.split("-");
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
        private Collection<Place.Hour> collect() {
            if (map.isEmpty()) return Collections.emptyList();
            Map<Set<Place.Hour>, Integer> values = new HashMap<>();
            for (Set<Place.Hour> hours : map.values()) {
                values.compute(hours, (h, i) -> i == null ? 1 : i + 1);
            }
            return values.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(Map.Entry::getKey)
                    .orElse(null);
        }

        private boolean equals(Set<Place.Hour> left, Set<Place.Hour> right) {
            left = left.stream().filter(hour -> isMonToSun(hour.getDay()))
                    .collect(Collectors.toSet());

            right = right.stream().filter(hour -> isMonToSun(hour.getDay()))
                    .collect(Collectors.toSet());

            return left.equals(right);
        }

        @Nullable
        private Place.Hour.Day parseDay(String key) {
            switch (key) {
                case "Place.Hour.mon":
                    return Place.Hour.Day.Mon;
                case "Place.Hour.tue":
                    return Place.Hour.Day.Tue;
                case "Place.Hour.wed":
                    return Place.Hour.Day.Wed;
                case "Place.Hour.thu":
                    return Place.Hour.Day.Thu;
                case "Place.Hour.fri":
                    return Place.Hour.Day.Fri;
                case "Place.Hour.sat":
                    return Place.Hour.Day.Sat;
                case "Place.Hour.sun":
                    return Place.Hour.Day.Sun;
                case "Place.Hour.ph":
                    return Place.Hour.Day.Ph;
                case "Place.Hour.evePh":
                    return Place.Hour.Day.EvePh;
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
        private boolean isMonToSun(Place.Hour.Day day) {
            switch (day) {
                case Mon:
                case Tue:
                case Wed:
                case Thu:
                case Fri:
                case Sat:
                case Sun:
                    return true;
                default:
                    return false;
            }
        }
    }
}
