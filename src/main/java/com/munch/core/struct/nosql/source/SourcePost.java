package com.munch.core.struct.nosql.source;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 5:27 PM
 * Project: struct
 */
@DynamoDBDocument
public class SourcePost {
    // Post by the Owners of the Source

    // General Information
    private String title;
    private String message;
    private String imageUrl;

    private long shareCount;
    private long likeCount;

    private String trackingId;

    @DynamoDBAttribute(attributeName = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @DynamoDBAttribute(attributeName = "imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @DynamoDBAttribute(attributeName = "shareCount")
    public long getShareCount() {
        return shareCount;
    }

    public void setShareCount(long shareCount) {
        this.shareCount = shareCount;
    }

    @DynamoDBAttribute(attributeName = "likeCount")
    public long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    @DynamoDBAttribute(attributeName = "trackingId")
    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
