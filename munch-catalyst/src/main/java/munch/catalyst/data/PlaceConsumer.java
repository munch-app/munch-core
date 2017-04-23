package munch.catalyst.data;

import com.corpus.object.GroupField;
import com.corpus.object.GroupObject;
import com.corpus.object.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import munch.catalyst.clients.PlaceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:42 PM
 * Project: munch-core
 */
@Singleton
public class PlaceConsumer extends DataConsumer {
    private static final Logger logger = LoggerFactory.getLogger(PlaceConsumer.class);

    private final PlaceClient placeClient;

    @Inject
    public PlaceConsumer(PlaceClient placeClient) {
        this.placeClient = placeClient;
    }

    @Override
    public void consume(List<GroupObject> list) {
        List<String> deletes = mapInActive(list, "place", GroupObject::getGroupKey);
        logger.info("Deleting {} group to service: places", deletes.size());
        placeClient.delete(deletes);

        List<PlaceConsumer.Place> puts = mapActive(list, "place", this::create);
        logger.info("Persisting {} group to service: places", puts.size());
        placeClient.put(puts);
    }

    /**
     * @param group group to create place with
     * @return newly created Place
     */
    public Place create(GroupObject group) {
        Place place = new Place();
        place.setId(group.getGroupKey());

        // Set basic info
        place.setName(getString(group, "name"));
        place.setPhone(getString(group, "phone"));
        place.setWebsite(getString(group, "website"));
        place.setDescription(getString(group, "description"));

        // Set other entity
        place.setPrice(createPrice(group));
        place.setLocation(createLocation(group));

        place.setTags(createTags(group));
        place.setHours(createHours(group));

        // Set tracking dates
        place.setCreatedDate(group.getCreatedDate());
        place.setUpdatedDate(group.getUpdatedDate());
        return place;
    }

    /**
     * @param group group object
     * @return Location created from group
     */
    public Place.Location createLocation(GroupObject group) {
        Place.Location location = new Place.Location();
        location.setAddress(getString(group, "Location.address"));
        location.setUnitNumber(getString(group, "Location.unitNumber"));

        location.setCity(getString(group, "Location.city"));
        location.setCountry(getString(group, "Location.country"));

        location.setPostal(getString(group, "Location.postal"));
        location.setLat(getDouble(group, "Location.lat"));
        location.setLng(getDouble(group, "Location.lng"));
        return location;
    }

    public Place.Price createPrice(GroupObject group) {
        // TODO price creation
        return null;
    }

    /**
     * Create opening hours from group
     *
     * @param group group object
     * @return set of opening hours created
     */
    public Set<Place.Hour> createHours(GroupObject group) {
        // TODO hour creation
        return Collections.emptySet();
    }

    /**
     * @param group group object
     * @return set of PlaceType labels of place
     */
    public Set<String> createTags(GroupObject group) {
        List<GroupField> fields = ObjectUtils.getAllField(group.getValues(), "PlaceType.others");
        return fields.stream().map(GroupField::getValue).collect(Collectors.toSet());
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class Place {

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
    }
}