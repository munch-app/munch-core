package com.munch.core.struct.nosql.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * @deprecated not ready to use yet
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 8:38 PM
 * Project: struct
 */
@DynamoDBTable(tableName = "munch.user.UserPlaceReview")
@Deprecated
public class UserPlaceReview {

    private String placeLocationId;
    private String timeUserId;

    // Ratings
    private Double overallExperience;
    private Double qualityOfFood;
    private Double qualityOfService;
    private Double atmosphere;
    private Double price;

    private String comment;

    public String getPlaceLocationId() {
        return placeLocationId;
    }

    public void setPlaceLocationId(String placeLocationId) {
        this.placeLocationId = placeLocationId;
    }

    public String getTimeUserId() {
        return timeUserId;
    }

    public void setTimeUserId(String timeUserId) {
        this.timeUserId = timeUserId;
    }

    public Double getOverallExperience() {
        return overallExperience;
    }

    public void setOverallExperience(Double overallExperience) {
        this.overallExperience = overallExperience;
    }

    public Double getQualityOfFood() {
        return qualityOfFood;
    }

    public void setQualityOfFood(Double qualityOfFood) {
        this.qualityOfFood = qualityOfFood;
    }

    public Double getQualityOfService() {
        return qualityOfService;
    }

    public void setQualityOfService(Double qualityOfService) {
        this.qualityOfService = qualityOfService;
    }

    public Double getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Double atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
