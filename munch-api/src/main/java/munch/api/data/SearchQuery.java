package munch.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a structure for place query
 * If distance and polygon is both present, distance will be used
 * <p>
 * <p>
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SearchQuery {
    private int from;
    private int size;

    private String query;
    private Location location; // Optional
    private Filters filters; // Optional

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * location.name = for display
     * location.center = currently provide no functions
     * location.points = polygon points
     *
     * @return Location for searchQuery
     * @see Location
     */
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Optional
     *
     * @return Place additional filters
     */
    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    /**
     * TODO: Not yet implemented
     * Add sort by distance
     */
    public static final class Filters {
        private PriceRange priceRange;
        private Set<Tag> tags;
        private Double ratingsAbove;
        private Set<Hour> hours;

        public PriceRange getPriceRange() {
            return priceRange;
        }

        public void setPriceRange(PriceRange priceRange) {
            this.priceRange = priceRange;
        }

        public Set<Tag> getTags() {
            return tags;
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }

        public Double getRatingsAbove() {
            return ratingsAbove;
        }

        public void setRatingsAbove(Double ratingsAbove) {
            this.ratingsAbove = ratingsAbove;
        }

        public Set<Hour> getHours() {
            return hours;
        }

        public void setHours(Set<Hour> hours) {
            this.hours = hours;
        }

        @JsonIgnoreProperties(ignoreUnknown = false)
        public static class PriceRange {
            private double highest;
            private double lowest;

            public double getHighest() {
                return highest;
            }

            public void setHighest(double highest) {
                this.highest = highest;
            }

            public double getLowest() {
                return lowest;
            }

            public void setLowest(double lowest) {
                this.lowest = lowest;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = false)
        public static class Tag {
            private String text;
            private boolean positive;

            public Tag() {
            }

            public Tag(String text, boolean positive) {
                this.text = text;
                this.positive = positive;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public boolean isPositive() {
                return positive;
            }

            public void setPositive(boolean positive) {
                this.positive = positive;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = false)
        public static class Hour {
            private int day;
            private String open;
            private String close;

            public int getDay() {
                return day;
            }

            public void setDay(int day) {
                this.day = day;
            }

            public String getOpen() {
                return open;
            }

            public void setOpen(String open) {
                this.open = open;
            }

            public String getClose() {
                return close;
            }

            public void setClose(String close) {
                this.close = close;
            }
        }
    }

    /**
     * Geo query
     */
    public static final class Distance {
        private String distance;
        private String latLng;

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getLatLng() {
            return latLng;
        }

        public void setLatLng(String latLng) {
            this.latLng = latLng;
        }
    }

    public static final class Polygon {
        private List<String> points;

        /**
         * @return as ["lat, lng", "lat, lng"]
         */
        public List<String> getPoints() {
            return points;
        }

        public void setPoints(List<String> points) {
            this.points = points;
        }
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "from=" + from +
                ", size=" + size +
                ", query='" + query + '\'' +
                ", location=" + location +
                ", filters=" + filters +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private final SearchQuery query;

        private Builder() {
            this.query = new SearchQuery();
        }

        public Builder with(SearchQuery query) {
            this.query.setQuery(query.query);
            this.query.setLocation(query.location);
            this.query.setFilters(query.filters);
            return this;
        }

        public Builder query(String text) {
            query.setQuery(text);
            return this;
        }

        public Builder polygon(Location location) {
            query.setLocation(location);
            return this;
        }

        public Builder tag(String tag) {
            if (query.getFilters() == null) query.setFilters(new Filters());
            if (query.getFilters().getTags() == null) query.getFilters().setTags(new HashSet<>());
            query.getFilters().getTags().add(new Filters.Tag(tag, true));
            return this;
        }

        public SearchQuery build() {
            return query;
        }
    }
}
