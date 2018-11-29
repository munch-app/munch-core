package munch.api.search.filter;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 10:53 PM
 * Project: munch-core
 */
public final class FilterResult {
    private long count;

    private TagGraph tagGraph;
    private PriceGraph priceGraph;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public TagGraph getTagGraph() {
        return tagGraph;
    }

    public void setTagGraph(TagGraph tagGraph) {
        this.tagGraph = tagGraph;
    }

    public PriceGraph getPriceGraph() {
        return priceGraph;
    }

    public void setPriceGraph(PriceGraph priceGraph) {
        this.priceGraph = priceGraph;
    }

    /**
     * Created by: Fuxing
     * Date: 12/9/18
     * Time: 3:53 PM
     * Project: munch-core
     */
    public static final class PriceGraph {
        private double min;
        private double max;

        private List<Point> points;
        private Map<String, Range> ranges;

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

        public List<Point> getPoints() {
            return points;
        }

        public void setPoints(List<Point> points) {
            this.points = points;
        }

        public Map<String, Range> getRanges() {
            return ranges;
        }

        public void setRanges(Map<String, Range> ranges) {
            this.ranges = ranges;
        }

        public static class Point {
            private double price;
            private int count;

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public int getCount() {
                return count;
            }

            public void setCount(int count) {
                this.count = count;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Point point = (Point) o;
                return Double.compare(point.price, price) == 0 &&
                        count == point.count;
            }

            @Override
            public int hashCode() {
                return Objects.hash(price, count);
            }

            @Override
            public String toString() {
                return "Point{" +
                        "price=" + price +
                        ", count=" + count +
                        '}';
            }
        }

        public static class Range {
            private double min;
            private double max;

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

            @Override
            public String toString() {
                return "Range{" +
                        "min=" + min +
                        ", max=" + max +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Range range = (Range) o;
                return Double.compare(range.min, min) == 0 &&
                        Double.compare(range.max, max) == 0;
            }

            @Override
            public int hashCode() {
                return Objects.hash(min, max);
            }
        }

        @Override
        public String toString() {
            return "PriceGraph{" +
                    "min=" + min +
                    ", max=" + max +
                    ", points=" + points +
                    ", ranges=" + ranges +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PriceGraph that = (PriceGraph) o;
            return Double.compare(that.min, min) == 0 &&
                    Double.compare(that.max, max) == 0 &&
                    Objects.equals(points, that.points) &&
                    Objects.equals(ranges, that.ranges);
        }

        @Override
        public int hashCode() {
            return Objects.hash(min, max, points, ranges);
        }
    }

    /**
     * Created by: Fuxing
     * Date: 28/11/18
     * Time: 4:15 AM
     * Project: munch-core
     */
    public static final class TagGraph {

        private List<FilterTag> tags;

        public List<FilterTag> getTags() {
            return tags;
        }

        public void setTags(List<FilterTag> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "TagGraph{" +
                    "tags=" + tags +
                    '}';
        }

    }
}
