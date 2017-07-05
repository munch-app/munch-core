package munch.catalyst.builder.place;

import catalyst.data.CorpusData;
import munch.catalyst.data.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 5/7/2017
 * Time: 4:24 PM
 * Project: munch-core
 */
public final class PriceBuilder implements TypeBuilder {
    private static final Logger logger = LoggerFactory.getLogger(PriceBuilder.class);
    private static final String PriceKey = "Place.price";

    private List<Double> prices = new ArrayList<>();

    @Override
    public void add(CorpusData data, CorpusData.Field field) {
        String value = field.getValue();
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
    public Place.Price collect() {
        if (prices.isEmpty()) return null;
        double low = prices.stream().mapToDouble(Double::doubleValue).min().getAsDouble();
        double high = prices.stream().mapToDouble(Double::doubleValue).max().getAsDouble();
        Place.Price price = new Place.Price();
        price.setLowest(low);
        price.setHighest(high);
        return price;
    }

    /**
     * Check if field match price
     *
     * @param field field to match
     * @return true if field key matched
     * @see PriceBuilder#PriceKey
     */
    public boolean match(CorpusData.Field field) {
        return PriceKey.matches(field.getKey());
    }
}
