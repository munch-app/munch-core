package munch.catalyst.builder.place;

import catalyst.data.CorpusData;
import com.google.common.collect.ImmutableSet;
import munch.catalyst.data.Place;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Created By: Fuxing Loh
 * Date: 5/7/2017
 * Time: 4:49 PM
 * Project: munch-core
 */
public final class LocationBuilder implements TypeBuilder {
    private static final Logger logger = LoggerFactory.getLogger(LocationBuilder.class);
    private static final Pattern LocationPattern = Pattern.compile("Place\\.Location\\.\\w+");
    private static final Set<String> priorityCorpus = ImmutableSet.of("Sg.Nea.TrackRecord");

    private ValueBuilder priorityBuilder = new ValueBuilder();
    private ValueBuilder valueBuilder = new ValueBuilder();

    @Override
    public void add(CorpusData data, CorpusData.Field field) {
        // These corpus has Priority as their data are more accurate
        if (priorityCorpus.contains(data.getCorpusName())) {
            priorityBuilder.add(data, field);
        }
        valueBuilder.add(data, field);
    }

    /**
     * @return Location build
     */
    public Place.Location collect() {
        Place.Location location = new Place.Location();
        // Street name of place, is considered part of address
        location.setAddress(WordUtils.capitalizeFully(collectMax("Place.Location.address")));
        location.setPostal(collectMax("Place.Location.postal"));

        location.setStreet(valueBuilder.collectMax("Place.Location.street"));
        location.setUnitNumber(valueBuilder.collectMax("Place.Location.unitNumber"));
        // This is mainly to be used in conjunction with container corpus data
        location.setBuilding(valueBuilder.collectMax("Place.Location.building"));

        location.setCity(valueBuilder.collectMax("Place.Location.city"));
        location.setCountry(valueBuilder.collectMax("Place.Location.country"));
        location.setLatLng(valueBuilder.collectMax("Place.Location.latLng"));
        return location;
    }

    /**
     * Collect from priorityBuilder first then value builder
     *
     * @param name name of key
     * @return String
     */
    @Nullable
    private String collectMax(String name) {
        String value = priorityBuilder.collectMax(name);
        if (value != null) {
            return value;
        }
        return valueBuilder.collectMax(name);
    }

    /**
     * Check if field is Place.Location.*
     *
     * @param field field to match
     * @return true if field key matched
     * @see LocationBuilder#LocationPattern
     */
    public boolean match(CorpusData.Field field) {
        return LocationPattern.matcher(field.getKey()).matches();
    }
}