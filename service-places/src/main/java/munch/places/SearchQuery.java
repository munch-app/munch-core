package munch.places;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * This is a structure for place query
 * Naturally everything is optional except for from & size
 * <p>
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SearchQuery {
    private int from;
    private int size;

    private String query;
    private Location location;
    private Filters filters;

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
     * Optional
     * If distance and polygon is both present, distance will be used
     *
     * @return polygon geo query
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
}
