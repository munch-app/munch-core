package com.munch.core.struct.nosql.place;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.munch.core.essential.util.DateTime;
import com.munch.core.struct.util.object.StorageMapper;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.util.Date;
import java.util.Set;

/**
 * Created By: Fuxing Loh
 * Date: 28/9/2016
 * Time: 10:08 PM
 * Project: struct
 */
@DynamoDBTable(tableName = "munch.place.PlaceImage")
public class PlaceImage {

    // Hash Key
    private String placeId;
    // Sort Key = Date+Random
    private String sortKey;

    // If null = apply to all location id,
    // If not null = only apply to given location id
    // placeLocationIds NOT_EXIST OR placeLocationIds CONTAINS(placeLocationIds)
    private Set<String> placeLocationIds;

    // User That Uploaded It (Empty = From Dashboard, Look at Meta Data on AWS S3)
    private String userId;
    private Date createdDate;

    // For resized Url, the sizes will be here key + "-w30h50" + .jpg
    // Store resized image in another bucket.
    private Set<String> resizedSizes;

    private String title;
    private String caption;
    private String keyId;

    @DynamoDBHashKey(attributeName = "p")
    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    @DynamoDBRangeKey(attributeName = "s")
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @DynamoDBAttribute(attributeName = "l")
    public Set<String> getPlaceLocationIds() {
        return placeLocationIds;
    }

    public void setPlaceLocationIds(Set<String> placeLocationIds) {
        this.placeLocationIds = placeLocationIds;
    }

    @DynamoDBAttribute(attributeName = "u")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @DynamoDBAttribute(attributeName = "t")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @DynamoDBAttribute(attributeName = "c")
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @DynamoDBAttribute(attributeName = "d")
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @DynamoDBAttribute(attributeName = "k")
    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    @DynamoDBAttribute(attributeName = "r")
    public Set<String> getResizedSizes() {
        return resizedSizes;
    }

    public void setResizedSizes(Set<String> resizedSizes) {
        this.resizedSizes = resizedSizes;
    }

    /**
     * Manager to Upload & Delete
     */
    public static class Manager {

        private DynamoDBMapper dynamoMapper;
        private StorageMapper storageMapper;

        public Manager(DynamoDBMapper dynamoMapper, StorageMapper storageMapper) {
            this.dynamoMapper = dynamoMapper;
            this.storageMapper = storageMapper;
        }

        /**
         * @param placeImage Remove Place Image From S3 & DynamoDB
         */
        public void delete(PlaceImage placeImage) {
            dynamoMapper.delete(placeImage);
            storageMapper.removeObject(placeImage.getKeyId());
        }

        /**
         * Add Place Image to S3 & DynamoDB
         *
         * @param placeImage PlaceImage
         * @param file       file
         * @param fileName   file name
         * @param sourceUrl  source url
         */
        public void upload(PlaceImage placeImage, File file, String fileName, String sourceUrl) {
            // Check Must Not Null
            assert placeImage.getPlaceId() != null;
            assert placeImage.getUserId() != null;

            // Auto Help Fill
            if (placeImage.getCreatedDate() == null)
                placeImage.setCreatedDate(DateTime.now());

            // Create Key Id
            String keyId = RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(fileName);
            placeImage.setKeyId(keyId);

            // Create Sort Key Id
            String sort = String.valueOf(DateTime.millisNow());
            String random = RandomStringUtils.randomAlphanumeric(20);
            //noinspection deprecation
            placeImage.setSortKey(sort + random);

            // Create Meta Data
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("originalFileName", fileName);
            if (sourceUrl != null)
                metadata.addUserMetadata("sourceUrl", sourceUrl);

            // Do actual upload
            storageMapper.putObject(keyId, file, metadata, CannedAccessControlList.PublicRead);
            dynamoMapper.save(placeImage);
        }

    }
}
