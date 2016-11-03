package com.munch.core.struct.nosql.place;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.munch.core.essential.file.ContentTypeException;
import com.munch.core.essential.file.FileMapper;
import com.munch.core.essential.file.FileSetting;
import com.munch.core.essential.util.DateTime;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

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

    private String placeId; // Hash Key
    private String sortKey; // Sort Key = Date+Random
    private String keyId; // Actual Image Bucket Key

    private String bannerSort; // Global Secondary Index Sort Key, null if not applicable

    // For resized Url, the sizes will be here 50x50 = width x height
    // Store resized image in another bucket. (key + "-w30h50" + .jpg)
    private Set<String> resizedSizes;

    // User That Uploaded It (Empty = From Dashboard, Look at Meta Data on AWS S3)
    private String userId;
    private String caption;
    private Date createdDate;

    @DynamoDBHashKey(attributeName = "p")
    @DynamoDBIndexHashKey(attributeName = "p")
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

    @DynamoDBAttribute(attributeName = "u")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @DynamoDBIndexRangeKey(attributeName = "b")
    public String getBannerSort() {
        return bannerSort;
    }

    public void setBannerSort(String bannerSort) {
        this.bannerSort = bannerSort;
    }

    public static class Setting implements FileSetting {

        @Override
        public String getBucket() {
            return "munch.place.images";
        }

    }

    public static class ResizedSetting implements FileSetting {

        @Override
        public String getBucket() {
            return "munch.place.images.resized";
        }

    }

    /**
     * Manager to Upload & Delete
     */
    public static class Manager {

        protected DynamoDBMapper dynamoMapper;
        protected FileMapper fileMapper;

        public Manager(DynamoDBMapper dynamoMapper, FileMapper fileMapper) {
            this.dynamoMapper = dynamoMapper;
            this.fileMapper = fileMapper;
        }

        /**
         * @param placeImage Remove Place Image From S3 & DynamoDB
         */
        public void delete(PlaceImage placeImage) {
            dynamoMapper.delete(placeImage);
            fileMapper.removeFile(placeImage.getKeyId());
        }

        protected void constraintCheck(PlaceImage placeImage) {
            // Check Must Not Null
            if (StringUtils.isAnyBlank(placeImage.getPlaceId(), placeImage.getUserId())
                    // Check Generate Value must be null
                    || StringUtils.isNoneBlank(placeImage.getSortKey(), placeImage.getKeyId())) {
                throw new IllegalArgumentException("PlaceImage: notNull: (placeId, userId) & null: (sortKey, keyId)");
            }

            // Auto Help Fill
            if (placeImage.getCreatedDate() == null)
                placeImage.setCreatedDate(DateTime.now());
        }

        protected void generateRandom(PlaceImage placeImage, String originalFileName) {
            // Create Key Id
            String keyId = RandomStringUtils.randomAlphanumeric(40) + "." + FilenameUtils.getExtension(originalFileName);
            placeImage.setKeyId(keyId);

            // Create Sort Key Id
            String sort = String.valueOf(DateTime.millisNow());
            String random = RandomStringUtils.randomAlphanumeric(20);
            //noinspection deprecation
            placeImage.setSortKey(sort + random);
        }

        /**
         * Add Place Image to S3 & DynamoDB
         *
         * @param placeImage       PlaceImage
         * @param file             file
         * @param originalFileName original file name
         */
        public void upload(PlaceImage placeImage, File file, String originalFileName) throws ContentTypeException {
            constraintCheck(placeImage);
            generateRandom(placeImage, originalFileName);

            // Create Meta Data
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.addUserMetadata("original-name", originalFileName);
            metadata.addUserMetadata("place-id", placeImage.getPlaceId());
            metadata.addUserMetadata("sort-key", placeImage.getSortKey());

            // Do actual upload
            fileMapper.putFile(placeImage.getKeyId(), file, metadata, CannedAccessControlList.PublicRead);
            dynamoMapper.save(placeImage);
        }

    }
}
