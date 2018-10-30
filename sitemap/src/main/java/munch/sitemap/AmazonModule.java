package munch.sitemap;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.munch.utils.file.AwsFileEndpoint;
import com.munch.utils.file.AwsFileMapper;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 9:15 AM
 * Project: munch-core
 */
public final class AmazonModule extends AbstractModule {

    @Singleton
    @Provides
    AwsFileMapper provideOMapper(AmazonS3 amazonS3) {
        AwsFileEndpoint endpoint = new AwsFileEndpoint(Regions.AP_SOUTHEAST_1, "www.munch.app-sitemap");
        return new AwsFileMapper(amazonS3, endpoint);
    }

    @Provides
    @Singleton
    AmazonS3 provideAmazonS3() {
        AmazonS3ClientBuilder standard = AmazonS3ClientBuilder.standard();
        return standard.build();
    }
}
