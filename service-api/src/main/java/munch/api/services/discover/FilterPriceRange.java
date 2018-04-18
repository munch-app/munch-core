package munch.api.services.discover;

import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 4/4/18
 * Time: 12:38 AM
 * Project: munch-core
 */
public class FilterPriceRange {
    private Map<Double, Integer> frequency;

    private Segment all;

    private Segment cheap;
    private Segment average;
    private Segment expensive;

    /**
     * Double is parsed as String for JSON
     */
    public Map<Double, Integer> getFrequency() {
        return frequency;
    }

    public void setFrequency(Map<Double, Integer> frequency) {
        this.frequency = frequency;
    }

    public Segment getAll() {
        return all;
    }

    public void setAll(Segment all) {
        this.all = all;
    }

    public Segment getCheap() {
        return cheap;
    }

    public void setCheap(Segment cheap) {
        this.cheap = cheap;
    }

    public Segment getAverage() {
        return average;
    }

    public void setAverage(Segment average) {
        this.average = average;
    }

    public Segment getExpensive() {
        return expensive;
    }

    public void setExpensive(Segment expensive) {
        this.expensive = expensive;
    }

    public static class Segment {

        private double min;
        private double max;

        public Segment(double min, double max) {
            this.min = min;
            this.max = max;
        }

        public double getMin() {
            return min;
        }

        public void setMin(double min) {
            this.min = min;
        }

        public double getMax() {
            return max;
        }

        public void setMax(double max) {
            this.max = max;
        }
    }
}
