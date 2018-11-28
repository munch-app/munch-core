package munch.api.search.filter;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 12/9/18
 * Time: 3:53 PM
 * Project: munch-core
 */
public final class PriceGraph {
    private static final double INCREMENT = 5.0;

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

    public static PriceGraph fromFrequency(Map<Double, Integer> frequency) {
        if (frequency.isEmpty()) return null;

        List<Point> points = parsePoints(frequency);
        List<Double> prices = points.stream().map(Point::getPrice).collect(Collectors.toList());
        double min = Math.floor(Collections.min(prices) / INCREMENT) * INCREMENT;
        double max = Math.ceil(Collections.max(prices) / INCREMENT) * INCREMENT;

        // Add min & max to points if don't exists
        // Might run into epsilon problem if double is not whole
        if (points.get(0).getPrice() != min) {
            Point point = new Point();
            point.setPrice(min);
            point.setCount(0);
            points.add(0, point);
        }

        if (points.get(points.size() - 1).getPrice() != max) {
            Point point = new Point();
            point.setPrice(max);
            point.setCount(0);
            points.add(point);
        }

        long total = points.stream().mapToInt(Point::getCount).sum();
        double f30 = findWithinCount(points, total, 0.3);
        double f70 = findWithinCount(points, total, 0.7);
        Map<String, Range> ranges = Map.of(
                "$", asRange(min, f30),
                "$$", asRange(f30, f70),
                "$$$", asRange(f70, max)
        );

        PriceGraph graph = new PriceGraph();
        graph.setMin(min);
        graph.setMax(max);
        graph.setPoints(points);
        graph.setRanges(ranges);
        return graph;
    }

    private static List<Point> parsePoints(Map<Double, Integer> frequency) {
        return frequency.entrySet()
                .stream()
                .map(es -> {
                    Point point = new Point();
                    point.setPrice(es.getKey());
                    point.setCount(es.getValue());
                    return point;
                })
                .sorted(Comparator.comparingDouble(Point::getPrice))
                .collect(Collectors.toList());
    }

    private static double findWithinCount(List<Point> points, long total, double percent) {
        int current = (int) (((double) total) * percent);
        for (Point point : points) {
            current -= point.getCount();
            if (current <= 0) return point.getPrice();
        }
        return 0;
    }

    private static Range asRange(double min, double max) {
        Range range = new Range();
        range.setMin(min);
        range.setMax(max);
        return range;
    }
}
