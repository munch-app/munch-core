package com.munch.core.struct.nosql.image;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;

/**
 * Sorted Image, and random image class built mainly for locality package
 * However it can be use to store any stored image
 * <p>
 * Created By: Fuxing Loh
 * Date: 27/9/2016
 * Time: 1:22 AM
 * Project: struct
 */
@DynamoDBTable(tableName = "SortedImage")
public class SortedImage {

    private String objectKey;
    private String sort;

    private String caption;

    /**
     * @return unique object key to identify object
     */
    @DynamoDBHashKey(attributeName = "k")
    public String getObjectKey() {
        return objectKey;
    }

    /**
     * @param objectKey a unique object key for storing sorted image
     * @see SortedImage#setClassObjectKey(String, String)
     */
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    /**
     * @param clazz     class or model name of a object
     * @param objectKey unique object key of the class
     * @see SortedImage#setObjectKey(String)
     */
    public void setClassObjectKey(String clazz, String objectKey) {
        setObjectKey(clazz + ":" + objectKey);
    }

    @DynamoDBRangeKey(attributeName = "s")
    public String getSort() {
        return sort;
    }

    /**
     * Typically you should use time series data like created date
     * However if you want to override the order, use a number greater then sort date
     *
     * @param sort sort of image
     */
    public void setSort(String sort) {
        this.sort = sort;
    }

    /**
     * @return Caption of sorted image
     */
    @DynamoDBAttribute(attributeName = "c")
    public String getCaption() {
        return caption;
    }

    /**
     * @param caption caption of image, aka description
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * @param date set date as millis (sorted in String)
     * @see SortedImage#setSort(String)
     */
    public void setSortDate(Date date) {
        setSort(String.valueOf(date.getTime()));
    }
}
