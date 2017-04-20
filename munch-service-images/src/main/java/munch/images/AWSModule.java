package munch.images;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.utils.AWSUtils;
import com.munch.utils.file.AwsFileEndpoint;
import com.munch.utils.file.AwsFileMapper;
import com.munch.utils.file.FileMapper;
import com.typesafe.config.Config;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 7:17 PM
 * Project: munch-core
 */
public class AWSModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    AWSCredentialsProvider provideCredentials(Config config) {
        return AWSUtils.getCredentials(config.getString("aws.credentials"));
    }

    @Provides
    @Singleton
    Regions provideRegion(Config config) {
        return Regions.fromName(config.getString("aws.region"));
    }

    @Provides
    @Singleton
    DynamoDB provideDynamoDB(AWSCredentialsProvider credentials, Regions regions) {
        AmazonDynamoDB client = AmazonDynamoDBClient.builder()
                .withCredentials(credentials)
                .withRegion(regions)
                .build();
        return new DynamoDB(client);
    }

    @Provides
    @Singleton
    FileMapper provideFileMapper(AWSCredentialsProvider credentials, Regions regions, Config config) {
        String bucket = config.getString("aws.bucket");
        AmazonS3 amazonS3 = AmazonS3Client.builder()
                .withCredentials(credentials)
                .withRegion(regions)
                .build();
        return new AwsFileMapper(amazonS3, new AwsFileEndpoint(regions.getName(), bucket));
    }
}
