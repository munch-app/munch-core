package com.munch.core.struct.block.source;

import com.munch.core.struct.block.BlockVersion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 5:22 PM
 * Project: struct
 */
public class SeedPlace extends BlockVersion {

    public static final String BUCKET_NAME = "munch.spaghetti.seed.place";

    // Basic
    private String id; // Only hash key used
    private String name;
    private String description;

    // Informational
    private String phoneNumber;
    private String websiteUrl;
    private List<String> types = new ArrayList<>();
    private List<String> tags = new ArrayList<>();

    // Details
    private double priceStart;
    private double priceEnd;
    private List<SourceHour> hours = new ArrayList<>();

    // Location
    private double lat;
    private double lng;
    private String address;
    private String addressExt;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    // Tracking
    private int source;
    private String sourceUrl;
    private Date updatedDate;
    private String origin; // The person that seeded this data
    private String notes;

    // Source of others
    private List<SourcePlace> sourcePlaces = new ArrayList<>();
    private List<String> sourceWebsites = new ArrayList<>(); // Id of source websites
    private SourceLocation sourceLocation;

    /**
     * All data should be named with the version that is introduced
     * For data not named, it is there since VERSION_FIRST
     */
    public SeedPlace() {
        super(VERSION_FIRST);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public double getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(double priceStart) {
        this.priceStart = priceStart;
    }

    public double getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(double priceEnd) {
        this.priceEnd = priceEnd;
    }

    public List<SourceHour> getHours() {
        return hours;
    }

    public void setHours(List<SourceHour> hours) {
        this.hours = hours;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressExt() {
        return addressExt;
    }

    public void setAddressExt(String addressExt) {
        this.addressExt = addressExt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<SourcePlace> getSourcePlaces() {
        return sourcePlaces;
    }

    public void setSourcePlaces(List<SourcePlace> sourcePlaces) {
        this.sourcePlaces = sourcePlaces;
    }

    public List<String> getSourceWebsites() {
        return sourceWebsites;
    }

    public void setSourceWebsites(List<String> sourceWebsites) {
        this.sourceWebsites = sourceWebsites;
    }

    public SourceLocation getSourceLocation() {
        return sourceLocation;
    }

    public void setSourceLocation(SourceLocation sourceLocation) {
        this.sourceLocation = sourceLocation;
    }
}
