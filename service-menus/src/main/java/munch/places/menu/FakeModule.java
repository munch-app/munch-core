package munch.places.menu;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.utils.file.FakeS3Mapper;
import com.munch.utils.file.FileEndpoint;
import com.munch.utils.file.FileMapper;
import com.typesafe.config.Config;

/**
 * Created By: Fuxing Loh
 * Date: 20/4/2017
 * Time: 7:29 PM
 * Project: munch-core
 */
public class FakeModule extends AbstractModule {

    @Override
    protected void configure() {
        requestInjection(this);
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
    FileEndpoint provideFileEndpoint(Config config) {
        String bucket = config.getString("aws.bucket");
        if (config.hasPath("fake.s3.public-endpoint")) {
            // Some bug, too lazy to debug
            String endpoint = config.getString("fake.s3.public-endpoint");
            return key -> endpoint + "/" + key;
        }
        String endpoint = config.getString("fake.s3.endpoint");
        return key -> endpoint + "/" + bucket + "/" + key;
    }
}
