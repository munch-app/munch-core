package com.munch.core.struct.nosql.source;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.munch.core.struct.nosql.website.SourceWebsite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 5:22 PM
 * Project: struct
 */
@DynamoDBTable(tableName = SeedPlace.Table.TableName)
public class SeedPlace {
    public static class Table {
        public static final String TableName = "SeedPlace";
        public static final String Id = "id";

        /**
         * Create an expression with just food venue id key
         *
         * @param seedUrl foodVenueId Key
         * @return expression with key
         */
        public static DynamoDBQueryExpression<SourceWebsite> expressKey(String seedUrl) {
            SourceWebsite data = new SourceWebsite();
            data.setSeedUrl(seedUrl);
            return new DynamoDBQueryExpression<SourceWebsite>()
                    .withHashKeyValues(data).withConsistentRead(false);
        }
    }

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

    @DynamoDBHashKey(attributeName = Table.Id)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @DynamoDBAttribute(attributeName = "websiteUrl")
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @DynamoDBAttribute(attributeName = "types")
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @DynamoDBAttribute(attributeName = "tags")
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @DynamoDBAttribute(attributeName = "priceStart")
    public double getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(double priceStart) {
        this.priceStart = priceStart;
    }

    @DynamoDBAttribute(attributeName = "priceEnd")
    public double getPriceEnd() {
        return priceEnd;
    }

    public void setPriceEnd(double priceEnd) {
        this.priceEnd = priceEnd;
    }

    @DynamoDBAttribute(attributeName = "hours")
    public List<SourceHour> getHours() {
        return hours;
    }

    public void setHours(List<SourceHour> hours) {
        this.hours = hours;
    }

    @DynamoDBAttribute(attributeName = "lat")
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    @DynamoDBAttribute(attributeName = "lng")
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @DynamoDBAttribute(attributeName = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @DynamoDBAttribute(attributeName = "addressExt")
    public String getAddressExt() {
        return addressExt;
    }

    public void setAddressExt(String addressExt) {
        this.addressExt = addressExt;
    }

    @DynamoDBAttribute(attributeName = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @DynamoDBAttribute(attributeName = "state")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @DynamoDBAttribute(attributeName = "zipCode")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @DynamoDBAttribute(attributeName = "country")
    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @DynamoDBAttribute(attributeName = "source")
    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    @DynamoDBAttribute(attributeName = "sourceUrl")
    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    @DynamoDBAttribute(attributeName = "updatedDate")
    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    @DynamoDBAttribute(attributeName = "origin")
    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @DynamoDBAttribute(attributeName = "notes")
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @DynamoDBAttribute(attributeName = "sourcePlaces")
    public List<SourcePlace> getSourcePlaces() {
        return sourcePlaces;
    }

    public void setSourcePlaces(List<SourcePlace> sourcePlaces) {
        this.sourcePlaces = sourcePlaces;
    }

    @DynamoDBAttribute(attributeName = "sourceWebsites")
    public List<String> getSourceWebsites() {
        return sourceWebsites;
    }

    public void setSourceWebsites(List<String> sourceWebsites) {
        this.sourceWebsites = sourceWebsites;
    }
}
