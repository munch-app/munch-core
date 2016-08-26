package com.munch.core.struct.nosql.website;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 9:32 PM
 * Project: struct
 */
@DynamoDBDocument
public class SourceWebPage {

    private String url;
    private List<String> content = new ArrayList<>();

    @DynamoDBAttribute(attributeName = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @DynamoDBAttribute(attributeName = "content")
    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
