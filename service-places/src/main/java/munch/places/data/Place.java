package munch.places.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import munch.places.data.hibernate.PlaceUserType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:54 PM
 * Project: munch-core
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@TypeDefs(value = {
        @TypeDef(name = "price", typeClass = PlaceUserType.Price.class),
        @TypeDef(name = "location", typeClass = PlaceUserType.Location.class),
        @TypeDef(name = "tags", typeClass = PlaceUserType.Tags.class),
        @TypeDef(name = "hours", typeClass = PlaceUserType.Hours.class),
        @TypeDef(name = "images", typeClass = PlaceUserType.Images.class),
})
@Table(indexes = {
        @Index(name = "index_munch_place_update_date", columnList = "updatedDate")
})
public final class Place {
    private String id;

    // Basic (All Indexed)
    private String name;
    private String phone;
    private String website;
    private String description;

    // One (All Indexed)
    private Price price;
    private Location location;

    // Many
    private String[] tags; // Index
    private Hour[] hours; // Index
    private Image[] images;

    // Dates
    private Date createdDate;
    private Date updatedDate;

    /**
     * @return place id, provided by catalyst groupId
     */
    @Id
    @Column(columnDefinition = "CHAR(36)", nullable = false, updatable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name of the place, trim if over
     */
    @Column(nullable = false, length = 255)
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
    @Column(nullable = true, length = 255)
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
    @Column(nullable = true, length = 500)
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
    @Column(nullable = true, length = 2000)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Type(type = "price")
    @Column(nullable = true)
    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    @Type(type = "location")
    @Column(nullable = false)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Type(type = "tags")
    @Column(nullable = false)
    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    @Type(type = "hours")
    @Column(nullable = false)
    public Hour[] getHours() {
        return hours;
    }

    public void setHours(Hour[] hours) {
        this.hours = hours;
    }

    /**
     * @return some images of place from other source
     */
    @Type(type = "images")
    @Column(nullable = false)
    public Image[] getImages() {
        return images;
    }

    public void setImages(Image[] images) {
        this.images = images;
    }

    @Column(nullable = false)
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @Column(nullable = false)
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
        private String latLng;

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

        public String getLatLng() {
            return latLng;
        }

        public void setLatLng(String latLng) {
            this.latLng = latLng;
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
            @JsonProperty("ph")Ph,
            @JsonProperty("evePh")EvePh,
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
     * Technically this is a smaller subclass of ImageMeta in munch-images
     * with lesser fields
     */
    public static final class Image {
        private String from;
        private String key;
        private Map<String, Type> images;

        /**
         * @return where is the image from
         */
        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        /**
         * @return unique key of the image
         */
        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @return images type with url
         */
        public Map<String, Type> getImages() {
            return images;
        }

        public void setImages(Map<String, Type> images) {
            this.images = images;
        }

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonIgnoreProperties(ignoreUnknown = true)
        public final static class Type {
            private String url;

            /**
             * @return public url of image content
             */
            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public String toString() {
                return "Type{" +
                        "url='" + url + '\'' +
                        '}';
            }
        }
    }
}
