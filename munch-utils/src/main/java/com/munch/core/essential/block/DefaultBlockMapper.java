package com.munch.core.essential.block;

import com.amazonaws.services.s3.AmazonS3;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 1:27 AM
 * Project: essential
 */
public class DefaultBlockMapper extends BlockMapper {

    public DefaultBlockMapper(String bucketName, AmazonS3 amazonS3, JsonConverter jsonConverter) {
        super(new AwsPersistClient(bucketName, amazonS3), jsonConverter);
    }
}
