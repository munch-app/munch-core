package munch.api.search.filter;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 29/11/18
 * Time: 11:13 AM
 * Project: munch-core
 */
@Singleton
public final class FilterPriceGrapher {
    private static final double INCREMENT = 5.0;

    public FilterResult.PriceGraph getGraph(Map<Double, Integer> frequency) {
        if (frequency.isEmpty()) return null;

        List<FilterResult.PriceGraph.Point> points = parsePoints(frequency);
        List<Double> prices = points.stream().map(FilterResult.PriceGraph.Point::getPrice).collect(Collectors.toList());
        double min = Math.floor(Collections.min(prices) / INCREMENT) * INCREMENT;
        double max = Math.ceil(Collections.max(prices) / INCREMENT) * INCREMENT;

        // Add min & max to points if don't exists
        // Might run into epsilon problem if double is not whole
        if (points.get(0).getPrice() != min) {
            FilterResult.PriceGraph.Point point = new FilterResult.PriceGraph.Point();
            point.setPrice(min);
            point.setCount(0);
            points.add(0, point);
        }

        if (points.get(points.size() - 1).getPrice() != max) {
            FilterResult.PriceGraph.Point point = new FilterResult.PriceGraph.Point();
            point.setPrice(max);
            point.setCount(0);
            points.add(point);
        }

        long total = points.stream().mapToInt(FilterResult.PriceGraph.Point::getCount).sum();
        double f30 = findWithinCount(points, total, 0.3);
        double f70 = findWithinCount(points, total, 0.7);
        Map<String, FilterResult.PriceGraph.Range> ranges = Map.of(
                "$", asRange(min, f30),
                "$$", asRange(f30, f70),
                "$$$", asRange(f70, max)
        );

        FilterResult.PriceGraph graph = new FilterResult.PriceGraph();
        graph.setMin(min);
        graph.setMax(max);
        graph.setPoints(points);
        graph.setRanges(ranges);
        return graph;
    }

    private static List<FilterResult.PriceGraph.Point> parsePoints(Map<Double, Integer> frequency) {
        return frequency.entrySet()
                .stream()
                .map(es -> {
                    FilterResult.PriceGraph.Point point = new FilterResult.PriceGraph.Point();
                    point.setPrice(es.getKey());
                    point.setCount(es.getValue());
                    return point;
                })
                .sorted(Comparator.comparingDouble(FilterResult.PriceGraph.Point::getPrice))
                .collect(Collectors.toList());
    }

    private static double findWithinCount(List<FilterResult.PriceGraph.Point> points, long total, double percent) {
        int current = (int) (((double) total) * percent);
        for (FilterResult.PriceGraph.Point point : points) {
            current -= point.getCount();
            if (current <= 0) return point.getPrice();
        }
        return 0;
    }

    private static FilterResult.PriceGraph.Range asRange(double min, double max) {
        FilterResult.PriceGraph.Range range = new FilterResult.PriceGraph.Range();
        range.setMin(min);
        range.setMax(max);
        return range;
    }

}
