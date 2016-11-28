package com.munch.core.essential.block;

import com.amazonaws.services.s3.AmazonS3;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 1:16 AM
 * Project: essential
 */
public class AwsPersistClient implements PersistClient {

    protected final String bucketName;
    protected final AmazonS3 amazonS3;

    public AwsPersistClient(String bucketName, AmazonS3 amazonS3) {
        this.bucketName = bucketName;
        this.amazonS3 = amazonS3;
    }

    @Override
    public void save(String key, String content) {
        amazonS3.putObject(bucketName, key, content);
    }

    @Override
    public String load(String key) {
        return amazonS3.getObjectAsString(bucketName, key);
    }
}
