/*
 * Copyright (c) 2015. This file is part of 3LINES PTE. LTD. property that is used for project Puffin which is not to be released for personal use.
 * Failing to do so will result in heavy penalty. However, if this individual file might need to be taken out of context for other purposes within the company,
 * please do ask for permission.
 */

package com.munch.core.essential.util;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import org.apache.commons.lang3.StringUtils;

/**
 * Everything here is thread safe singleton for AWS Util
 * Created by Fuxing
 * Date: 3/1/2015
 * Time: 6:06 PM
 * Project: PuffinCore
 */
public final class AWSUtil {

    private static AmazonS3 amazonS3 = null;
    private static AmazonDynamoDB amazonDynamoDB = null;

    private AWSUtil() {/* NOT Suppose to init */}

    /**
     * Get singleton AmazonS3 instance in region: ap-southeast-1 (SG)
     *
     * @return AmazonS3 Singleton
     */
    public static AmazonS3 getS3() {
        if (amazonS3 == null) {
            synchronized (AWSUtil.class) {
                if (amazonS3 == null) {
                    MunchConfig config = MunchConfig.getInstance();
                    if (config.isDev()) {
                        // Development Environment Setup
                        String accessKey = config.getString("development.aws.access-key");
                        String secretAccessKey = config.getString("development.aws.secret-key");
                        amazonS3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretAccessKey));

                        amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
                    } else {
                        amazonS3 = new AmazonS3Client();
                        // Set region to singapore
                        // https://s3-ap-southeast-1.amazonaws.com/
                        amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
                    }
                }
            }
        }
        return amazonS3;
    }

    /**
     * Get singleton AmazonDynamoDB instance in region: ap-southeast-1 (SG)
     *
     * @return AmazonDynamoDB Singleton
     */
    public static AmazonDynamoDB getDynamoDB() {
        if (amazonDynamoDB == null) {
            synchronized (AWSUtil.class) {
                if (amazonDynamoDB == null) {
                    MunchConfig config = MunchConfig.getInstance();
                    if (config.isDev()) {
                        String accessKey = config.getString("development.aws.access-key");
                        String secretKey = config.getString("development.aws.secret-key");
                        // Development environment endpoint
                        amazonDynamoDB = new AmazonDynamoDBClient(new BasicAWSCredentials(accessKey, secretKey));
                        amazonDynamoDB.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
                    } else {
                        // If decided to use keys
                        if (StringUtils.equals(config.getString("aws.provider"), "keys")) {
                            String accessKey = config.getString("development.aws.access-key");
                            String secretKey = config.getString("development.aws.secret-key");
                            amazonDynamoDB = new AmazonDynamoDBClient(new BasicAWSCredentials(accessKey, secretKey));
                        } else { // If decided to use EC2 Provider
                            amazonDynamoDB = new AmazonDynamoDBClient();
                        }
                        // Set region to singapore
                        // https://s3-ap-southeast-1.amazonaws.com/
                        amazonDynamoDB.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
                    }
                }
            }
        }
        return amazonDynamoDB;
    }

    /**
     * Force production mode on AWSUtils, some stupid injector problem
     */
    public static void setToProduction() {
        amazonDynamoDB = new AmazonDynamoDBClient();
        amazonDynamoDB.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        amazonS3 = new AmazonS3Client();
        amazonS3.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
    }

    public static void setToNull() {
        synchronized (AWSUtil.class) {
            amazonDynamoDB = null;
            amazonS3 = null;
        }
    }
}
