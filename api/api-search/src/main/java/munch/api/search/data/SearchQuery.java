package munch.api.search.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.data.location.Area;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
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
    public static final String VERSION = "2018-09-25";

    private Filter filter = new Filter();
    private Sort sort = new Sort();

    /**
     * Optional
     *
     * @return Place additional filters
     */
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    /**
     * Add sort by distance
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Filter {
        private Price price;
        private Tag tag;
        private Hour hour;
        private Location location;

        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        public Tag getTag() {
            return tag;
        }

        public void setTag(Tag tag) {
            this.tag = tag;
        }

        public Hour getHour() {
            return hour;
        }

        public void setHour(Hour hour) {
            this.hour = hour;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Price {
            private String name;
            private Double min;
            private Double max;

            @Nullable
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Double getMin() {
                return min;
            }

            public void setMin(Double min) {
                this.min = min;
            }

            public Double getMax() {
                return max;
            }

            public void setMax(Double max) {
                this.max = max;
            }

            @Override
            public String toString() {
                return "Price{" +
                        "name='" + name + '\'' +
                        ", min=" + min +
                        ", max=" + max +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Tag {
            private Set<String> positives;
            private Set<String> negatives;

            @NotNull
            public Set<String> getPositives() {
                return positives;
            }

            public void setPositives(Set<String> positives) {
                this.positives = positives;
            }

            @NotNull
            public Set<String> getNegatives() {
                return negatives;
            }

            public void setNegatives(Set<String> negatives) {
                this.negatives = negatives;
            }

            @Override
            public String toString() {
                return "Tag{" +
                        "positives=" + positives +
                        ", negatives=" + negatives +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Hour {
            private String name;

            private String day;
            private String open;
            private String close;

            /**
             * @return name of hour filter
             */
            @Nullable
            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            /**
             * @return day which it is open
             */
            @NotNull
            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            @NotNull
            public String getOpen() {
                return open;
            }

            public void setOpen(String open) {
                this.open = open;
            }

            @NotNull
            public String getClose() {
                return close;
            }

            public void setClose(String close) {
                this.close = close;
            }

            @Override
            public String toString() {
                return "Hour{" +
                        "name='" + name + '\'' +
                        ", day='" + day + '\'' +
                        ", open='" + open + '\'' +
                        ", close='" + close + '\'' +
                        '}';
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Location {
            private List<Area> areas;
            private List<Point> points;
            private Type type;

            /**
             * @return if no location default to anywhere
             */
            public List<Area> getAreas() {
                return areas;
            }

            public void setAreas(List<Area> areas) {
                this.areas = areas;
            }

            /**
             * @return Points used by Eat Between
             */
            public List<Point> getPoints() {
                return points;
            }

            public void setPoints(List<Point> points) {
                this.points = points;
            }

            public Type getType() {
                return type;
            }

            public void setType(Type type) {
                this.type = type;
            }

            public enum Type {
                /**
                 * Feature: Eat Between
                 * Require: areas, 1 or more
                 */
                Between,

                /**
                 * Default: Any of these areas
                 * Require: areas, 1 or more
                 */
                Where,

                /**
                 * Default: if user location is turned on
                 * Require: Header['User-Lat-Lng']
                 */
                Nearby,

                /**
                 * Default: if user didn't select any location, assumed anywhere based on geo ip will be used
                 * Require: Ip Address or Header['User-Lat-Lng']
                 * Note: if any of the above failed to find required data, Anywhere will be assumed
                 */
                Anywhere
            }

            @Override
            public String toString() {
                return "Location{" +
                        "areas=" + areas +
                        ", points=" + points +
                        ", type=" + type +
                        '}';
            }

            public static class Point {
                private String name;
                private String latLng;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getLatLng() {
                    return latLng;
                }

                public void setLatLng(String latLng) {
                    this.latLng = latLng;
                }

                @Override
                public String toString() {
                    return "Point{" +
                            "name='" + name + '\'' +
                            ", latLng='" + latLng + '\'' +
                            '}';
                }
            }
        }

        @Override
        public String toString() {
            return "Filter{" +
                    "price=" + price +
                    ", tag=" + tag +
                    ", hour=" + hour +
                    ", location=" + location +
                    '}';
        }
    }

    /**
     * Ordinal sort
     * Only a single sort type can be used
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Sort {
        public static final String TYPE_MUNCH_RANK = "munch_rank";
        public static final String TYPE_PRICE_LOWEST = "price_lowest";
        public static final String TYPE_PRICE_HIGHEST = "price_highest";
        public static final String TYPE_DISTANCE_NEAREST = "distance_nearest";
        public static final String TYPE_RATING_HIGHEST = "rating_highest";

        private String type;

        /**
         * @return sort types
         * @see Sort#TYPE_MUNCH_RANK
         * @see Sort#TYPE_PRICE_LOWEST
         * @see Sort#TYPE_PRICE_HIGHEST
         * @see Sort#TYPE_DISTANCE_NEAREST
         * @see Sort#TYPE_RATING_HIGHEST
         */
        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "Sort{" +
                    "type='" + type + '\'' +
                    '}';
        }
    }

    /**
     * @return version of SearchQuery
     */
    public String getVersion() {
        return VERSION;
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "filter=" + filter +
                ", sort=" + sort +
                ", version=" + getVersion() +
                '}';
    }
}
