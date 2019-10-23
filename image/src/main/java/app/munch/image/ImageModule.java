package app.munch.image;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import javax.inject.Singleton;
import java.net.URI;

/**
 * Created by: Fuxing
 * Date: 2019-08-13
 * Time: 07:47
 */
public final class ImageModule extends AbstractModule {

    @Provides
    @Singleton
    S3Client provideS3Client() {
        Config config = ConfigFactory.load().getConfig("services.aws.s3");

        if (config.hasPath("url")) {
            return S3Client.builder()
                    .endpointOverride(URI.create(config.getString("url")))
                    .region(Region.US_WEST_2)
                    .credentialsProvider(() -> AwsBasicCredentials.create("foo", "bar"))
                    .build();
        } else {
            return S3Client.builder()
                    .build();
        }
    }
}
