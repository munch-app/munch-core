package munch.images;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.utils.block.AwsStoreMapper;
import com.munch.utils.block.BlockConverter;
import com.munch.utils.block.BlockStoreMapper;
import com.munch.utils.file.FakeS3Mapper;
import com.munch.utils.file.FakeS3Utils;
import com.munch.utils.file.FileEndpoint;
import com.munch.utils.file.FileMapper;
import com.typesafe.config.Config;
import munch.images.database.JacksonConvertor;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 7:29 PM
 * Project: munch-core
 */
public class FakeModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BlockConverter.class).to(JacksonConvertor.class);
    }

    @Provides
    @Singleton
    FileEndpoint provideFileEndpoint(Config config) {
        String bucket = config.getString("aws.bucket");
        if (config.hasPath("fake.s3.public-endpoint")) {
            // Some bug, too lazy to debug
            String endpoint = config.getString("fake.s3.public-endpoint");
            return key -> endpoint + "/fake-s3/" + bucket + "/" + key;
        }
        String endpoint = config.getString("fake.s3.endpoint");
        return key -> endpoint + "/" + bucket + "/" + key;
    }

    @Provides
    @Singleton
    FileMapper provideFileMapper(final Config config) {
        String endpoint = config.getString("fake.s3.endpoint");
        String bucket = config.getString("aws.bucket");
        return new FakeS3Mapper(endpoint, bucket);
    }

    @Provides
    @Singleton
    BlockStoreMapper provideMetaMapper(Config config, BlockConverter converter) {
        String endpoint = config.getString("fake.s3.endpoint");
        String bucket = config.getString("aws.bucket");
        return new AwsStoreMapper(bucket, FakeS3Utils.createClient(endpoint), converter);
    }
}
