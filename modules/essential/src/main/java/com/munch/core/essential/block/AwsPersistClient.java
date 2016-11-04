package com.munch.core.essential.block;

import com.amazonaws.services.s3.AmazonS3;
import com.munch.core.essential.util.AWSUtil;

/**
 * Created by: Fuxing
 * Date: 15/9/2016
 * Time: 1:16 AM
 * Project: essential
 */
public class AwsPersistClient implements PersistClient {

    private AmazonS3 amazonS3 = AWSUtil.getS3();

    private String bucketName;

    public AwsPersistClient(String bucketName) {
        this.bucketName = bucketName;
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
