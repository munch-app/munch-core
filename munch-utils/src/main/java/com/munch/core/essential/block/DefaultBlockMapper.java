package com.munch.core.essential.block;

import com.amazonaws.services.s3.AmazonS3;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 1:27 AM
 * Project: essential
 */
public class DefaultBlockMapper extends BlockMapper {

    /**
     * Default block mapper to save json as block data to aws
     * AWS Amazon S3 Client is used because it can store up to 5gb of data in one single put
     *
     * @param bucketName Bucket Name to use with aws
     */
    public DefaultBlockMapper(String bucketName) {
        super(new AwsPersistClient(bucketName), new GsonConverter());
    }

    public DefaultBlockMapper(String bucketName, JsonConverter jsonConverter) {
        super(new AwsPersistClient(bucketName), jsonConverter);
    }

    public DefaultBlockMapper(String bucketName, AmazonS3 amazonS3, JsonConverter jsonConverter) {
        super(new AwsPersistClient(bucketName, amazonS3), jsonConverter);
    }
}