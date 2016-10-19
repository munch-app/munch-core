package com.munch.core.struct.nosql.place;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 1:36 AM
 * Project: struct
 */
@DynamoDBTable(tableName = "munch.place.PlaceImageLink")
public class PlaceImageLink {

    public static final String TYPE_BANNER = "b";

    private String placeId;
    private String type;
    private String keyId;

    @DynamoDBHashKey(attributeName = "p")
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @DynamoDBRangeKey(attributeName = "t")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @DynamoDBAttribute(attributeName = "k")
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }
}
