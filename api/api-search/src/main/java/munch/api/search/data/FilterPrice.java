package munch.api.search.data;

import java.util.List;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 4/4/18
 * Time: 12:38 AM
 * Project: munch-core
 */
@Deprecated
public final class FilterPrice {
    private Map<Double, Integer> frequency;
    private List<Percentile> percentiles;

    /**
     * Double is parsed as String for JSON
     */
    public Map<Double, Integer> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<Double, Integer> frequency) {
        this.frequency = frequency;
    }

    public List<Percentile> getPercentiles() {
        return percentiles;
    }

    public void setPercentiles(List<Percentile> percentiles) {
        this.percentiles = percentiles;
    }

    public static class Percentile {
        private double percent;
        private double price;

        public double getPercent() {
            return percent;
        }

        public void setPercent(double percent) {
            this.percent = percent;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        @Override
        public String toString() {
            return "Percentile{" +
                    "percent=" + percent +
                    ", price=" + price +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FilterPrice{" +
                "frequency=" + frequency +
                ", percentiles=" + percentiles +
                '}';
    }
}
