package com.munch.core.struct.nosql.website;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.HashSet;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 26/8/2016
 * Time: 9:28 PM
 * Project: struct
 */
@DynamoDBTable(tableName = SourceWebsite.Table.TableName)
public class SourceWebsite {

    // Separate from main Seed Place due to future proofing it from max 400kb limit
    public static class Table {
        public static final String TableName = "SourceWebsite";
        public static final String SeedUrl = "seedUrl";

        /**
         * Create an expression with just food venue id key
         *
         * @param seedUrl foodVenueId Key
         * @return expression with key
         */
        public static DynamoDBQueryExpression<SourceWebsite> expressKey(String seedUrl) {
            SourceWebsite data = new SourceWebsite();
            data.setSeedUrl(seedUrl);
            return new DynamoDBQueryExpression<SourceWebsite>()
                    .withHashKeyValues(data).withConsistentRead(false);
        }
    }

    private String seedUrl;
    private Set<SourceWebPage> webPages = new HashSet<>();
    private Set<String> urls = new HashSet<>();

    private Set<String> mediaUrls = new HashSet<>();
    private Set<String> pdfUrls = new HashSet<>();

    @DynamoDBHashKey(attributeName = Table.SeedUrl)
    public String getSeedUrl() {
        return seedUrl;
    }

    public void setSeedUrl(String seedUrl) {
        this.seedUrl = seedUrl;
    }

    @DynamoDBAttribute(attributeName = "webPages")
    public Set<SourceWebPage> getWebPages() {
        return webPages;
    }

    public void setWebPages(Set<SourceWebPage> webPages) {
        this.webPages = webPages;
    }

    @DynamoDBAttribute(attributeName = "urls")
    public Set<String> getUrls() {
        return urls;
    }

    public void setUrls(Set<String> urls) {
        this.urls = urls;
    }

    @DynamoDBAttribute(attributeName = "mediaUrls")
    public Set<String> getMediaUrls() {
        return mediaUrls;
    }

    public void setMediaUrls(Set<String> mediaUrls) {
        this.mediaUrls = mediaUrls;
    }

    @DynamoDBAttribute(attributeName = "")
    public Set<String> getPdfUrls() {
        return pdfUrls;
    }

    public void setPdfUrls(Set<String> pdfUrls) {
        this.pdfUrls = pdfUrls;
    }
}
