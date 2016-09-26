package com.munch.core.struct.nosql;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.s3.AmazonS3;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 27/9/2016
 * Time: 12:56 AM
 * Project: struct
 */
public class PlaceImageModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    PlaceImageManager providePlaceImageManager(AmazonS3 amazonS3, DynamoDBMapper mapper) {
        return new PlaceImageManager(amazonS3, mapper, "munch.place.images");
    }
}
