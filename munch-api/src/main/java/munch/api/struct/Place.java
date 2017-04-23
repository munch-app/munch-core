package munch.api.struct;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:54 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {

    private String id;

    // Basic
    private String name;
    private String phone;
    private String website;
    private String description;

    // One
    private Price price;
    private Location location;

    // Many
    private Set<String> tags;
    private Set<Hour> hours;

    // Data linked to place
    private List<LinkedImage> linkedImages;

    // External resolved data
    private List<Image> images;

    // Dates
    private Date createdDate;
    private Date updatedDate;

    /**
     * @return place id, provided by catalyst groupId
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name of the place, trim if over
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Preferably with country code intact
     *
     * @return phone number of the place
     */
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Website url must not exceed 2000 character.
     * Trim if necessary
     * http://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers
     *
     * @return website url of place
     */
    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * Description of place must not exceed 1000 character.
     * Trim if over 1000 characters
     *
     * @return description of place
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    /**
     * @return opening hours of place
     */
    public Set<Hour> getHours() {
        return hours;
    }

    public void setHours(Set<Hour> hours) {
        this.hours = hours;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    /**
     * @return location of place
     */
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * For egress, set this to null before sending it out
     *
     * @return images linked to the place
     */
    public List<LinkedImage> getLinkedImages() {
        return linkedImages;
    }

    public void setLinkedImages(List<LinkedImage> linkedImages) {
        this.linkedImages = linkedImages;
    }

    /**
     * @return external resources, to be resolved
     */
    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    /**
     * @return created date from catalyst group
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * @return updated date from catalyst group
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * Location data of the place
     */
    public static final class Location {

        private String address;
        private String unitNumber;

        private String city;
        private String country;

        private String postal;
        private Double lat;
        private Double lng;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getUnitNumber() {
            return unitNumber;
        }

        public void setUnitNumber(String unitNumber) {
            this.unitNumber = unitNumber;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getPostal() {
            return postal;
        }

        public void setPostal(String postal) {
            this.postal = postal;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }
    }

    /**
     * Price data of the place
     */
    public static final class Price {

        private Double lowest;
        private Double highest;

        /**
         * Part of price range
         *
         * @return lowest in price range
         */
        public Double getLowest() {
            return lowest;
        }

        public void setLowest(Double lowest) {
            this.lowest = lowest;
        }

        /**
         * Part of price range
         *
         * @return highest in price range
         */
        public Double getHighest() {
            return highest;
        }

        public void setHighest(Double highest) {
            this.highest = highest;
        }
    }

    /**
     * Opening hour of the place
     */
    public static final class Hour {

        public enum Day {
            @JsonProperty("mon")Mon,
            @JsonProperty("tue")Tue,
            @JsonProperty("wed")Wed,
            @JsonProperty("thu")Thu,
            @JsonProperty("fri")Fri,
            @JsonProperty("sat")Sat,
            @JsonProperty("sun")Sun,
            @JsonProperty("ph")Ph
        }

        private Day day;

        /**
         * HH:mm
         * 00:00 - 23:59
         * Midnight - 1 Min before midnight Max
         * <p>
         * 12:00 - 22:00
         * Noon - 10pm
         * <p>
         * 08:00 - 19:00
         * 8am - 7pm
         * <p>
         * Not Allowed:
         * 20:00 - 04:00
         * 20:00 - 24:00
         * Not allowed to put 24:00 Highest is 23:59
         * Not allowed to cross to another day
         */
        private String open;
        private String close;

        /**
         * @return day in enum will be string in json
         * @see Day
         */
        public Day getDay() {
            return day;
        }

        public void setDay(Day day) {
            this.day = day;
        }

        /**
         * @return opening hours
         */
        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        /**
         * @return closing hours
         */
        public String getClose() {
            return close;
        }

        public void setClose(String close) {
            this.close = close;
        }
    }

    /**
     * Image linked to the place
     */
    public static final class LinkedImage {
        private String imageKey;
        private Date createdDate;

        private String sourceName;
        private String sourceId;

        /**
         * @return imageKey stored in munch-images service
         */
        public String getImageKey() {
            return imageKey;
        }

        public void setImageKey(String imageKey) {
            this.imageKey = imageKey;
        }

        /**
         * @return date for which linked image is created
         */
        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        /**
         * @return source provider name for the image
         */
        public String getSourceName() {
            return sourceName;
        }

        public void setSourceName(String sourceName) {
            this.sourceName = sourceName;
        }

        /**
         * @return source provider id for the image
         */
        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
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