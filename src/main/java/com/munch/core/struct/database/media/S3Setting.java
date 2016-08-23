package com.munch.core.struct.database.media;

import com.amazonaws.services.s3.AmazonS3;
import com.munch.core.essential.util.ConfigReader;

/**
 * Created by: Fuxing
 * Date: 11/2/2016
 * Time: 4:53 PM
 * Project: vital-core
 */
public interface S3Setting {

    String getBucket();

    String getRegion();

    AmazonS3 getAmazonS3();

    ConfigReader getConfig();

}
