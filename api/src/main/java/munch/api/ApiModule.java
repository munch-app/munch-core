package munch.api;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.api.core.CoreModule;
import munch.api.landing.LandingModule;
import munch.api.place.PlaceModule;
import munch.api.search.SearchModule;
import munch.api.user.UserModule;
import munch.restful.server.firebase.FirebaseAuthenticationModule;
import org.apache.commons.io.IOUtils;

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by: Fuxing
 * Date: 25/3/2017
 * Time: 7:45 PM
 * Project: munch-core
 */
public class ApiModule extends AbstractModule {

    @Override
    protected void configure() {
        install(getAuthenticationModule());

        // Service Module
        install(new CoreModule());
        install(new UserModule());
        install(new PlaceModule());
        install(new SearchModule());
        install(new LandingModule());
    }

    private FirebaseAuthenticationModule getAuthenticationModule() {
        Config config = ConfigFactory.load();
        final String projectId = config.getString("services.firebase.projectId");
        final String ssmKey = config.getString("services.firebase.ssmKey");

        try {
            AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();
            GetParameterResult result = client.getParameter(new GetParameterRequest()
                    .withName(ssmKey)
                    .withWithDecryption(true)
            );

            String value = result.getParameter().getValue();
            InputStream inputStream = IOUtils.toInputStream(value, "utf-8");
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return new FirebaseAuthenticationModule(projectId, credentials);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Provides
    @Singleton
    DynamoDB provideDynamoDB() {
        return new DynamoDB(AmazonDynamoDBClientBuilder.defaultClient());
    }

    @Provides
    Config provideConfig() {
        return ConfigFactory.load();
    }

    /**
     * Start api server with predefined modules
     *
     * @param args not required
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ApiModule());
        injector.getInstance(ApiServer.class).start();
    }
}