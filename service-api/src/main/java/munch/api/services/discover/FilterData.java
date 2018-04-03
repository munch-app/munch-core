package munch.api.services.discover;

import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 3/4/2018
 * Time: 4:15 AM
 * Project: munch-core
 */
public final class FilterData {
    private long count;
    private Tag tag;
    private PriceRange priceRange;

    /**
     * @return result count
     */
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public PriceRange getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(PriceRange priceRange) {
        this.priceRange = priceRange;
    }

    public static class Tag {
        private Map<String, Integer> counts;

        public Map<String, Integer> getCounts() {
            return counts;
        }

        public void setCounts(Map<String, Integer> counts) {
            this.counts = counts;
        }
    }

    public static class PriceRange {
        private Map<Double, Integer> frequency;

        private Segment all;

        private Segment cheap;
        private Segment average;
        private Segment expensive;

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
}
