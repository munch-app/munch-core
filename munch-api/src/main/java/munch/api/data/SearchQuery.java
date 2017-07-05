package munch.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashSet;
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
    private Integer from;
    private Integer size;

    // Optional fields
    private String query;
    private Location location;

    private Filter filter;
    private Sort sort;

    public Integer getFrom() {
        return from;
    }

    public void setFrom(Integer from) {
        this.from = from;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
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
    public static final class Filter {
        private Price price;
        private Tag tag;
        private Rating rating;
        private Hour hour;
        private Distance distance;

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

        public Rating getRating() {
            return rating;
        }

        public void setRating(Rating rating) {
            this.rating = rating;
        }

        public Hour getHour() {
            return hour;
        }

        public void setHour(Hour hour) {
            this.hour = hour;
        }

        public Distance getDistance() {
            return distance;
        }

        public void setDistance(Distance distance) {
            this.distance = distance;
        }

        public static class Price {
            private Double min;
            private Double max;

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
        }

        public static class Tag {
            private Set<String> positives;
            private Set<String> negatives;

            public Set<String> getPositives() {
                return positives;
            }

            public void setPositives(Set<String> positives) {
                this.positives = positives;
            }

            public Set<String> getNegatives() {
                return negatives;
            }

            public void setNegatives(Set<String> negatives) {
                this.negatives = negatives;
            }
        }

        public static class Rating {
            private Double min;

            public Double getMin() {
                return min;
            }

            public void setMin(Double min) {
                this.min = min;
            }
        }

        public static class Hour {
        }

        /**
         * latLng: "lat, lng"
         * min: in metres
         * max: in metres
         */
        public static class Distance {
            private String latLng;
            private Integer min;
            private Integer max;

            public String getLatLng() {
                return latLng;
            }

            public void setLatLng(String latLng) {
                this.latLng = latLng;
            }

            public Integer getMin() {
                return min;
            }

            public void setMin(Integer min) {
                this.min = min;
            }

            public Integer getMax() {
                return max;
            }

            public void setMax(Integer max) {
                this.max = max;
            }
        }
    }

    public static final class Sort {
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "from=" + from +
                ", size=" + size +
                ", query='" + query + '\'' +
                ", location=" + location +
                ", filter=" + filter +
                ", sort=" + sort +
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
            this.query.setFilter(query.filter);
            this.query.setSort(query.sort);
            return this;
        }

        public Builder query(String text) {
            query.setQuery(text);
            return this;
        }

        public Builder location(Location location) {
            query.setLocation(location);
            return this;
        }

        public Builder tag(String tag, boolean positive) {
            if (query.getFilter() == null) query.setFilter(new Filter());
            if (query.getFilter().getTag() == null) query.getFilter().setTag(new Filter.Tag());

            if (positive) {
                if (query.getFilter().getTag().getPositives() == null)
                    query.getFilter().getTag().setPositives(new HashSet<>());
                query.getFilter().getTag().getPositives().add(tag);
            } else {
                if (query.getFilter().getTag().getNegatives() == null)
                    query.getFilter().getTag().setNegatives(new HashSet<>());
                query.getFilter().getTag().getNegatives().add(tag);
            }
            return this;
        }

        public Builder tag(String tag) {
            return tag(tag, true);
        }

        public SearchQuery build() {
            return query;
        }
    }
}
