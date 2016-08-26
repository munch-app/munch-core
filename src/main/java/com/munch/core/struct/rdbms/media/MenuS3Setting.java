package com.munch.core.struct.rdbms.media;

import com.amazonaws.services.s3.AmazonS3;
import com.munch.core.essential.util.AWSUtil;
import com.munch.core.essential.util.ConfigReader;
import com.munch.core.essential.util.CoreConfig;

/**
 * Created by: Fuxing
 * Date: 11/2/2016
 * Time: 4:53 PM
 * Project: vital-core
 */
public class MenuS3Setting implements S3Setting {

    private ConfigReader config = CoreConfig.getInstance();

    private String bucket = config.getString("aws.s3.bucket-menu");
    private String region = config.getString("aws.s3.region");

    @Override
    public String getBucket() {
        return bucket;
    }

    @Override
    public String getRegion() {
        return region;
    }

    @Override
    public AmazonS3 getAmazonS3() {
        return AWSUtil.getS3();
    }

    @Override
    public ConfigReader getConfig() {
        return config;
    }


}
