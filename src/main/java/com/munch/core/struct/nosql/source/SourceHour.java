package com.munch.core.struct.nosql.source;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;

import java.time.LocalTime;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 5:27 PM
 * Project: struct
 */
@DynamoDBDocument
public class SourceHour {

    private int day;
    private String open;
    private String close;

    @DynamoDBAttribute(attributeName = "day")
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @DynamoDBAttribute(attributeName = "open")
    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    @DynamoDBAttribute(attributeName = "close")
    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    @DynamoDBIgnore
    public LocalTime getOpenLocal() {
        return LocalTime.parse(open);
    }

    public void setOpen(LocalTime open) {
        this.open = open.toString();
    }

    @DynamoDBIgnore
    public LocalTime getCloseLocal() {
        return LocalTime.parse(close);
    }

    public void setClose(LocalTime close) {
        this.close = close.toString();
    }
}
