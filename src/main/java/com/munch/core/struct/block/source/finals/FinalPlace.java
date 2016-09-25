package com.munch.core.struct.block.source.finals;

import com.munch.core.struct.block.BlockVersion;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 25/9/2016
 * Time: 2:17 AM
 * Project: struct
 */
public class FinalPlace extends BlockVersion {

    public static final String BUCKET_NAME = "munch.spaghetti.final.place";

    private String id;

    private Confirmed confirmed;
    private Detail detail;
    private Image image;
    private Menu menu;
    private Tag tag;

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     *
     * @param version version of block
     */
    public FinalPlace() {
        super(VERSION_FIRST);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Confirmed getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(Confirmed confirmed) {
        this.confirmed = confirmed;
    }

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    /**
     * Already pre confirmed place data from other stages
     */
    public static class Confirmed {
        private String placeName;
        private String website;

        private String address;

        private String unitNumber;
        private String zipCode;

        private String street;
        private String town;
        private String blk;

        private String countryCode;

        private double lat, lng;

        // TODO linked platform


        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

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

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getTown() {
            return town;
        }

        public void setTown(String town) {
            this.town = town;
        }

        public String getBlk() {
            return blk;
        }

        public void setBlk(String blk) {
            this.blk = blk;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    /**
     * Detail stage data
     */
    public static class Detail {
        private List<FinalTextContext> descriptions = new ArrayList<>();
        private List<FinalTextContext> phones = new ArrayList<>();
        private List<FinalHourContext> hours = new ArrayList<>();

        public List<FinalTextContext> getDescriptions() {
            return descriptions;
        }

        public List<FinalTextContext> getPhones() {
            return phones;
        }

        public List<FinalHourContext> getHours() {
            return hours;
        }
    }

    public static class Image {
        private List<FinalUrlSource> imageUrls = new ArrayList<>();

        public List<FinalUrlSource> getImageUrls() {
            return imageUrls;
        }
    }

    public static class Menu {
        private List<FinalUrlSource> menuUrls = new ArrayList<>();

        public List<FinalUrlSource> getMenuUrls() {
            return menuUrls;
        }
    }

    public static class Tag {
        private List<FinalTextContext> cuisines = new ArrayList<>();
        private List<FinalTextContext> venues = new ArrayList<>();
        private List<FinalTextContext> meals = new ArrayList<>();
        private List<FinalTextContext> others = new ArrayList<>();

        private List<FinalTextContext> tags = new ArrayList<>();

        public List<FinalTextContext> getCuisines() {
            return cuisines;
        }

        public List<FinalTextContext> getVenues() {
            return venues;
        }

        public List<FinalTextContext> getMeals() {
            return meals;
        }

        public List<FinalTextContext> getOthers() {
            return others;
        }

        public List<FinalTextContext> getTags() {
            return tags;
        }
    }
}
