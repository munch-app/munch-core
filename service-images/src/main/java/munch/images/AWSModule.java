package munch.images;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.utils.AWSUtils;
import com.munch.utils.block.AwsStoreMapper;
import com.munch.utils.block.BlockConverter;
import com.munch.utils.block.BlockStoreMapper;
import com.munch.utils.file.AwsFileEndpoint;
import com.munch.utils.file.AwsFileMapper;
import com.munch.utils.file.FileMapper;
import com.typesafe.config.Config;
import munch.images.database.JacksonConvertor;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 7:17 PM
 * Project: munch-core
 */
public class AWSModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BlockConverter.class).to(JacksonConvertor.class);
    }

    @Provides
    @Singleton
    AmazonS3 provideAmazonS3(Config config) {
        AWSCredentialsProvider credentials = AWSUtils.getCredentials(config.getString("aws.credentials"));
        Regions regions = Regions.fromName(config.getString("aws.region"));
        return AmazonS3Client.builder()
                .withCredentials(credentials)
                .withRegion(regions)
                .build();
    }

    @Provides
    @Singleton
    AwsFileEndpoint provideFileEndpoint(Regions regions, Config config) {
        String bucket = config.getString("aws.bucket");
        return new AwsFileEndpoint(regions, bucket);
    }

    @Provides
    @Singleton
    FileMapper provideFileMapper(AmazonS3 amazonS3, AwsFileEndpoint fileEndpoint) {
        return new AwsFileMapper(amazonS3, fileEndpoint);
    }

    @Provides
    @Singleton
    BlockStoreMapper provideMetaMapper(Config config, AmazonS3 amazonS3, BlockConverter converter) {
        String bucket = config.getString("aws.bucket");
        return new AwsStoreMapper(bucket, amazonS3, converter);
    }
}
