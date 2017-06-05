package munch.catalyst;

import catalyst.data.CatalystLink;
import catalyst.data.CorpusData;
import com.google.common.collect.ImmutableSet;
import munch.catalyst.clients.Place;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:33 AM
 * Project: munch-core
 */
public final class PlaceBuilder {
    private static final Pattern HourKeysPattern = Pattern.compile("Place\\.Hour\\.\\w+");
    private static final String PriceKey = "Place.price";
    private static final Set<String> MultiKeys = ImmutableSet.of("Place.tag", "Place.type");

    private String id;
    private Map<String, Map<String, Integer>> keyValueCount = new HashMap<>();

    public void consume(CatalystLink link) {
        if (id == null) id = link.getCatalystId();
        if (!id.equals(link.getCatalystId()))
            throw new IllegalArgumentException("CatalystLink catalystId is different, unable to build Place.");

        for (CorpusData.Field field : link.getData().getFields()) {
            String key = field.getKey();
            String value = field.getValue();

            // Use respective adders
            if (PriceKey.equals(key)) {
                addPrices(value);
            } else if (HourKeysPattern.matcher(key).matches()) {
                addHours(key, value);
            } else if (MultiKeys.contains(key)) {
                // TODO multi value adder
            } else {
                addValues(key, value);
            }
        }
    }

    private void addPrices(String value) {

    }

    private void addHours(String key, String value) {

    }

    private void addValues(String key, String value) {
        // TODO values adder
//        keyValueCount.computeIfAbsent(key, s -> {
//            new HashMap<>();
//        })
//        keyValueCount.compute(key, (v, count) -> {
//        });
    }

    public Place build() {
        Place place = new Place();
        // TODO build place with: keyValueCount
        return place;
    }
}
