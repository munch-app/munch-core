package munch.api.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import munch.data.location.Area;
import munch.data.tag.Tag;
import munch.restful.core.exception.ValidationException;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 8:34 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class SearchQuery {

    // TODO: Search Screen

    /*
    Collection { Name, ID }
    Screen = [Search, Collection, Location, Home, Occasion]
     */

    /**
     * Versions History:
     * 2018-09-25
     * - Changes in Filter.Location object
     * <p>
     * 2018-11-28
     * - Changes in Filter.tags uses munch.data.tag.Tag now
     * - Added strict validation for consistency
     */
    public static final String VERSION = "2018-11-28";

    // TODO: Collection Search
    private Filter filter = new Filter();
    private Sort sort = new Sort();

    /**
     * Optional
     *
     * @return NotNull Filter
     */
    @NotNull
    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    /**
     * Optional
     *
     * @return NotNull Sort
     */
    @NotNull
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
        private Hour hour;
        private Price price;
        private List<Tag> tags = new ArrayList<>();
        private Location location = new Location();

        @Valid
        @Nullable
        public Price getPrice() {
            return price;
        }

        public void setPrice(Price price) {
            this.price = price;
        }

        @Valid
        @Nullable
        public Hour getHour() {
            return hour;
        }

        public void setHour(Hour hour) {
            this.hour = hour;
        }

        @NotNull
        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
            this.tags = tags;
        }

        @Valid
        @NotNull
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

            @Nullable
            public Double getMin() {
                return min;
            }

            public void setMin(Double min) {
                this.min = min;
            }

            @Nullable
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

        /**
         * Hour Filter with explicit timings.
         * <p>
         * For Dinner, Lunch, Breakfast, Supper.
         * It should be queried with tagId in Filter.tags
         */
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Hour {
            private Type type;

            private String day;
            private String open;
            private String close;

            public enum Type {
                /**
                 * OpenNow, requires {day,open,close}
                 */
                OpenNow,

                /**
                 * OpenDay, requires {day,open,close}
                 */
                OpenDay,
            }

            /**
             * @return hour type, used more for ui purpose
             */
            @NotNull
            public Type getType() {
                return type;
            }

            public void setType(Type type) {
                this.type = type;
            }

            /**
             * @return day which it is open
             */
            @NotNull
            @Pattern(regexp = "mon|tue|wed|thu|fri|sat|sun")
            public String getDay() {
                return day;
            }

            public void setDay(String day) {
                this.day = day;
            }

            @NotNull
            @Pattern(regexp = "[0-9]{2}:[0-9]{2}")
            public String getOpen() {
                return open;
            }

            public void setOpen(String open) {
                this.open = open;
            }

            @NotNull
            @Pattern(regexp = "[0-9]{2}:[0-9]{2}")
            public String getClose() {
                return close;
            }

            public void setClose(String close) {
                this.close = close;
            }

            @Override
            public String toString() {
                return "Hour{" +
                        "type=" + type +
                        ", day='" + day + '\'' +
                        ", open='" + open + '\'' +
                        ", close='" + close + '\'' +
                        '}';
            }

            @JsonIgnore
            public String asText() {
                if (type == Type.OpenNow) return "Open Now";
                return StringUtils.capitalize(getDay()) + " " + getOpen() + " - " + getClose();
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Location {
            private Type type = Type.Anywhere;
            private List<Area> areas = new ArrayList<>();
            private List<Point> points = new ArrayList<>();

            public enum Type {
                /**
                 * Feature: EatBetween
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

            /**
             * @return if no location default to anywhere
             */
            @NotNull
            public List<Area> getAreas() {
                return areas;
            }

            public void setAreas(List<Area> areas) {
                this.areas = areas;
            }

            /**
             * @return Points used by EatBetween
             */
            @Valid
            @NotNull
            public List<Point> getPoints() {
                return points;
            }

            public void setPoints(List<Point> points) {
                this.points = points;
            }

            @NotNull
            public Type getType() {
                return type;
            }

            public void setType(Type type) {
                this.type = type;
            }

            @Override
            public String toString() {
                return "Location{" +
                        "areas=" + areas +
                        ", points=" + points +
                        ", type=" + type +
                        '}';
            }

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Point {
                private String name;
                private String latLng;

                @NotBlank
                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                @NotNull
                @Pattern(regexp = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$")
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
                    "hour=" + hour +
                    ", price=" + price +
                    ", tags=" + tags +
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

        private String type;

        /**
         * @return sort types
         * @see Sort#TYPE_MUNCH_RANK
         * @see Sort#TYPE_PRICE_LOWEST
         * @see Sort#TYPE_PRICE_HIGHEST
         * @see Sort#TYPE_DISTANCE_NEAREST
         */
        @Nullable
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

    /**
     * @param query to validate so that it respect the annotations
     * @throws ValidationException if other deep fields input by user
     */
    public static void validate(SearchQuery query) throws ValidationException {
        if (query.getFilter() == null) query.setFilter(new SearchQuery.Filter());
        if (query.getSort() == null) query.setSort(new SearchQuery.Sort());

        if (query.getFilter().getLocation() == null) {
            query.getFilter().setLocation(new SearchQuery.Filter.Location());
        }

        if (query.getFilter().getTags() == null) {
            query.getFilter().setTags(List.of());
        }

        ValidationException.validate(query);
    }
}
