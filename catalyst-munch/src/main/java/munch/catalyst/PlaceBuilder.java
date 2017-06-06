package munch.catalyst;

import catalyst.data.CatalystLink;
import catalyst.data.CorpusData;
import munch.catalyst.clients.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Pattern;

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

    public void consume(CatalystLink link) {
        if (id == null) id = link.getCatalystId();
        if (!id.equals(link.getCatalystId()))
            throw new IllegalArgumentException("CatalystLink catalystId is different, unable to build Place.");

        // Find earliest date
        Date createdDate = link.getData().getCreatedDate();
        if (createdDate.compareTo(earliestDate) < 0) earliestDate = createdDate;

        for (CorpusData.Field field : link.getData().getFields()) {
            String key = field.getKey();
            String value = field.getValue();

            // Use respective adders
            if (PriceKey.equals(key)) {
                priceBuilder.addPrices(value);
            } else if (HourKeysPattern.matcher(key).matches()) {
                hourBuilder.addHours(link, key, value);
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
        // TODO location building
        place.setLocation(location);

        List<String> tags = new ArrayList<>();
        tags.addAll(valueBuilder.collect("Place.tag"));
        tags.addAll(valueBuilder.collect("Place.type"));
        place.setTags(tags);
        place.setHours(hourBuilder.collect());

        place.setCreatedDate(earliestDate);
        place.setUpdatedDate(updatedDate);

        // Return null if validation failed
        if (!validate(place)) return null;
        return place;
    }

    private boolean validate(Place place) {
        // TODO make sure has correct values
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
        private Map<String, List<Place.Hour>> map = new HashMap<>();

        /**
         * Place.Hour.mon-sun, ph, evePh, raw
         *
         * @param link  link that provided hour data
         * @param key   key is day of hour
         * @param value value is time range
         */
        private void addHours(CatalystLink link, String key, String value) {
            List<Place.Hour> hours = map.computeIfAbsent(link.getData().getCorpusKey(), s -> new ArrayList<>());
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

        private List<Place.Hour> collect() {
            // TODO build based on most grouped appearance
            return null;
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
    }
}
