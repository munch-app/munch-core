package munch.catalyst.builder.place;

import corpus.data.CorpusData;
import munch.catalyst.builder.DataBuilder;
import munch.catalyst.data.Place;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:33 AM
 * Project: munch-core
 */
public final class PlaceBuilder implements DataBuilder<Place> {
    private static final Logger logger = LoggerFactory.getLogger(PlaceBuilder.class);

    private final LocationBuilder locationBuilder = new LocationBuilder();
    private final PriceBuilder priceBuilder = new PriceBuilder();
    private final HourBuilder hourBuilder = new HourBuilder();
    private final ValueBuilder valueBuilder = new ValueBuilder();

    private final TypeBuilder[] builders = new TypeBuilder[]{
            locationBuilder, priceBuilder, hourBuilder, valueBuilder};

    private String id;
    private Date earliestDate = new Date();

    @Override
    public void consume(CorpusData data) {
        // Id Validation
        if (id == null) id = data.getCatalystId();
        if (!id.equals(data.getCatalystId()))
            throw new IllegalArgumentException("CatalystLink catalystId is different, unable to build Place.");

        // Find earliest date
        Date createdDate = data.getCreatedDate();
        if (createdDate.compareTo(earliestDate) < 0) earliestDate = createdDate;

        // Iterate through all fields and add them to respective builders
        for (CorpusData.Field field : data.getFields()) {
            for (TypeBuilder builder : builders) {
                if (builder.match(field)) {
                    builder.add(data, field);
                    // Break because can only logically match 1 for now
                    break;
                }
            }
        }
    }

    /**
     * @return created Place value with consumed links
     */
    @Override
    public List<Place> collect(Date updatedDate) {
        Place place = new Place();
        place.setId(id);

        // Transform Name to Upper Camel Case
        place.setName(WordUtils.capitalizeFully(valueBuilder.collectMax("Place.name")));
        place.setPhone(valueBuilder.collectMax("Place.phone"));
        place.setWebsite(valueBuilder.collectMax("Place.website"));
        place.setDescription(valueBuilder.collectMax("Place.description"));

        // With their own type builder
        place.setPrice(priceBuilder.collect());
        place.setLocation(locationBuilder.collect());
        place.setHours(hourBuilder.collect());

        // Multiple values
        place.setTags(valueBuilder.collect("Place.tag").stream()
                .map(String::toLowerCase).collect(Collectors.toList()));

        // Created & Updated Dates
        place.setCreatedDate(earliestDate);
        place.setUpdatedDate(updatedDate);

        // Return empty list if validation failed
        if (!validate(place)) return Collections.emptyList();
        return Collections.singletonList(place);
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
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean validate(Place place) {
        if (StringUtils.isBlank(place.getId())) return false;
        if (StringUtils.isBlank(place.getName())) return false;

        if (place.getLocation() == null) return false;
        if (StringUtils.isBlank(place.getLocation().getPostal())) return false;
        if (StringUtils.isBlank(place.getLocation().getCountry())) return false;
        if (StringUtils.isBlank(place.getLocation().getLatLng())) return false;

        if (place.getCreatedDate() == null) return false;
        if (place.getUpdatedDate() == null) return false;

        try {
            String[] split = place.getLocation().getLatLng().split(",");
            Double.parseDouble(split[0].trim());
            Double.parseDouble(split[1].trim());
        } catch (NumberFormatException nfe) {
            logger.error("LatLng Validation failed");
            return false;
        }
        return true;
    }
}
