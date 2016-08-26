package com.munch.core.struct.nosql.source;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 5:27 PM
 * Project: struct
 */
@DynamoDBDocument
public class SourcePlace {

    // Basic
    private String name;
    private long fanCount;
    private long checkInCount;
    private String description;
    private String websiteUrl;

    // Details Data
    private List<String> tags = new ArrayList<>();
    private List<String> types = new ArrayList<>();
    private List<String> rawContent = new ArrayList<>();
    private List<String> menuUrls = new ArrayList<>();
    private List<String> prices = new ArrayList<>();
    private double priceStart;
    private double priceEnd;

    // Contact Data
    private List<String> phoneNumbers = new ArrayList<>();
    private List<String> emails = new ArrayList<>();
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

    // Ratings
    private double ratingPercent;
    private int reviewCount;

    // Other Struct
    private List<SourcePost> posts = new ArrayList<>();
    private List<SourceComment> comments = new ArrayList<>();

    // Tracking
    private String trackingId;
    private int source;
    private String sourceUrl;
    private Date updatedDate;

    @DynamoDBAttribute(attributeName = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "fanCount")
    public long getFanCount() {
        return fanCount;
    }

    public void setFanCount(long fanCount) {
        this.fanCount = fanCount;
    }

    @DynamoDBAttribute(attributeName = "checkInCount")
    public long getCheckInCount() {
        return checkInCount;
    }

    public void setCheckInCount(long checkInCount) {
        this.checkInCount = checkInCount;
    }

    @DynamoDBAttribute(attributeName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "websiteUrl")
    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    @DynamoDBAttribute(attributeName = "tags")
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @DynamoDBAttribute(attributeName = "types")
    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @DynamoDBAttribute(attributeName = "rawContent")
    public List<String> getRawContent() {
        return rawContent;
    }

    public void setRawContent(List<String> rawContent) {
        this.rawContent = rawContent;
    }

    @DynamoDBAttribute(attributeName = "menuUrls")
    public List<String> getMenuUrls() {
        return menuUrls;
    }

    public void setMenuUrls(List<String> menuUrls) {
        this.menuUrls = menuUrls;
    }

    @DynamoDBAttribute(attributeName = "prices")
    public List<String> getPrices() {
        return prices;
    }

    public void setPrices(List<String> prices) {
        this.prices = prices;
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

    @DynamoDBAttribute(attributeName = "phoneNumbers")
    public List<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    @DynamoDBAttribute(attributeName = "emails")
    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
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

    @DynamoDBAttribute(attributeName = "ratingPercent")
    public double getRatingPercent() {
        return ratingPercent;
    }

    public void setRatingPercent(double ratingPercent) {
        this.ratingPercent = ratingPercent;
    }

    @DynamoDBAttribute(attributeName = "reviewCount")
    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    @DynamoDBAttribute(attributeName = "posts")
    public List<SourcePost> getPosts() {
        return posts;
    }

    public void setPosts(List<SourcePost> posts) {
        this.posts = posts;
    }

    @DynamoDBAttribute(attributeName = "comments")
    public List<SourceComment> getComments() {
        return comments;
    }

    public void setComments(List<SourceComment> comments) {
        this.comments = comments;
    }

    @DynamoDBAttribute(attributeName = "trackingId")
    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
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
}
