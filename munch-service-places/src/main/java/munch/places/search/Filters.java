package munch.places.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Filters {

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
        private String tag;
        private boolean positive;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
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
