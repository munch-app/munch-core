package com.munch.core.struct.nosql.place;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.google.common.base.Splitter;
import com.munch.core.essential.source.DataSource;
import com.munch.core.essential.util.DateTime;

import java.util.Date;
import java.util.Iterator;

/**
 * Created By: Fuxing Loh
 * Date: 26/9/2016
 * Time: 10:06 PM
 * Project: struct
 */
@DynamoDBTable(tableName = "PlaceLocationImage")
public class PlaceLocationImage {

    public static final int SOURCE_FACEBOOK = DataSource.FACEBOOK;
    public static final int SOURCE_RESTAURANT = DataSource.RESTAURANT;
    public static final int SOURCE_BLOG = DataSource.BLOG;

    // 2 letter spacing different
    public static final String SORT_TYPE_RESTAURANT = "AR";
    public static final String SORT_TYPE_BLOG = "AO";
    public static final String SORT_TYPE_USER_INSTAGRAM = "AL";
    public static final String SORT_TYPE_RESTAURANT_INSTAGRAM = "AI";
    public static final String SORT_TYPE_REVIEW = "AF";


    // Source Link Tracking of Images
    // Facebook  = Seed Restaurant [Owner Control] :
    // Website = Seed Restaurant [Owner Control] :
    // Blog : Blog Article Link
    // Review : Review Article
    // Instagram: Instagram Link

    private String placeLocationId;
    // Owner Control : Priority : Time Series : Hash (UserId)
    // 00000:AR:2147483647:8a80cb8157381d0f0157381d9c4602dc
    private String sortTypeTimeUserId;

    private String keyId; // Generated
    private String description; // Captions

    // Required
    private Integer finnType;// If null means not run through finn yet
    private String username;


    // Images Tag / Finn Type and stored separately on another nosql table
    // Stored on S3 Meta Data
    // originalName, source url, sourceType
    // everything else also

    // TRANSIENT
    private String userId;
    private Date createdDate;
    private String sort;
    private String sortType;


    @DynamoDBHashKey(attributeName = "p")
    public String getPlaceLocationId() {
        return placeLocationId;
    }

    public void setPlaceLocationId(String placeLocationId) {
        this.placeLocationId = placeLocationId;
    }

    @DynamoDBRangeKey(attributeName = "s")
    public String getSortTypeTimeUserId() {
        return sortTypeTimeUserId;
    }

    @Deprecated
    public void setSortTypeTimeUserId(String sortTypeTimeUserId) {
        this.sortTypeTimeUserId = sortTypeTimeUserId;
    }

    @DynamoDBAttribute(attributeName = "i")
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @DynamoDBAttribute(attributeName = "d")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @DynamoDBAttribute(attributeName = "u")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "f")
    public Integer getFinnType() {
        return finnType;
    }

    public void setFinnType(Integer finnType) {
        this.finnType = finnType;
    }

    /**
     * 1 time operation to fill user id, date, sort, sortType
     */
    private void fillSortTypeTimUserId() {
        Iterator<String> split = Splitter.on(':').split(sortTypeTimeUserId).iterator();
        if (split.hasNext())
            sort = split.next();
        if (split.hasNext())
            sortType = split.next();
        if (split.hasNext())
            createdDate = new Date(Long.parseLong(split.next()));
        if (split.hasNext())
            userId = split.next();
    }

    @DynamoDBIgnore
    public String getUserId() {
        if (userId == null) {
            fillSortTypeTimUserId();
        }
        return userId;
    }

    @DynamoDBIgnore
    public Date getCreatedDate() {
        if (createdDate == null) {
            fillSortTypeTimUserId();
        }
        return createdDate;
    }

    @DynamoDBIgnore
    public String getSort() {
        if (sort == null) {
            fillSortTypeTimUserId();
        }
        return sort;
    }

    @DynamoDBIgnore
    public String getSortType() {
        if (sortType == null) {
            fillSortTypeTimUserId();
        }
        return sortType;
    }

    public static class Builder {

        private final String placeLocationId;

        public Builder(String placeLocationId) {
            this.placeLocationId = placeLocationId;
        }

        /**
         * Take note of this function when trying to port this to lambda
         * I am using millis not epoch
         */
        private String createSort(String sort, String type, String userId) {
            return sort + ":" + type + ":" + String.valueOf(DateTime.millisNow()) + ":" + userId;
        }

        public PlaceLocationImage createMedia(String sort, String type, String userId) {
            PlaceLocationImage media = new PlaceLocationImage();
            media.placeLocationId = this.placeLocationId;
            media.sortTypeTimeUserId = createSort(sort, type, userId);
            return media;
        }

        public PlaceLocationImage createMedia(String type, String userId) {
            return createMedia("00000", type, userId);
        }
    }
}
