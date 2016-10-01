package com.munch.core.struct.nosql.place;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 * Created By: Fuxing Loh
 * Date: 1/10/2016
 * Time: 7:09 PM
 * Project: struct
 */
@DynamoDBTable(tableName = "munch.place.PlacePrice")
public class PlacePrice {

    public static final String SOURCE_WEB = "w";
    public static final String SOURCE_MENU = "m";
    public static final String SOURCE_REVIEW = "r";

    private String placeId;
    private String sort;

    private Double price;
    private String name;
    private String source;

    @DynamoDBIndexHashKey(attributeName = "p")
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @DynamoDBRangeKey(attributeName = "s")
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    @DynamoDBAttribute(attributeName = "c")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @DynamoDBAttribute(attributeName = "n")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "e")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
