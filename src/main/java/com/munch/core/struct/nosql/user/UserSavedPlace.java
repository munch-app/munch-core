package com.munch.core.struct.nosql.user;

import com.amazonaws.services.dynamodbv2.datamodeling.*;

/**
 * Created By: Fuxing Loh
 * Date: 26/9/2016
 * Time: 9:30 PM
 * Project: struct
 */
@DynamoDBTable(tableName = "munch.user.UserSavedPlace")
public class UserSavedPlace {

    public static final String REL_LIKE = "l";

    private String userId;
    private String placeLocationId;

    // Liked, Disliked or star ratings
    // Only use liked for now, in the future might have other values
    private String relation;

    @DynamoDBHashKey(attributeName = "u")
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "p-u-index", attributeName = "u")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBRangeKey(attributeName = "p")
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "p-u-index", attributeName = "p")
    public String getPlaceLocationId() {
        return placeLocationId;
    }

    public void setPlaceLocationId(String placeLocationId) {
        this.placeLocationId = placeLocationId;
    }

    @DynamoDBAttribute(attributeName = "r")
    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
