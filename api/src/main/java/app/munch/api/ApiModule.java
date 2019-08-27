package app.munch.api;

import app.munch.api.migration.MigrationModule;
import app.munch.database.DatabaseModule;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterResult;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.api.core.CoreModule;
import munch.api.feed.FeedModule;
import munch.api.location.LocationModule;
import munch.api.place.PlaceModule;
import munch.api.search.SearchModule;
import munch.api.user.UserModule;
import munch.restful.server.firebase.FirebaseAuthenticationModule;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

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

        // Database Module
        install(new DatabaseModule());

        // Service Module
        install(new CoreModule());
        install(new DataModule());
        install(new AdminModule());

        install(new PageModule());
        install(new AccountModule());

        install(new MigrationModule());

        install(new UserModule());
        install(new PlaceModule());
        install(new SearchModule());
        install(new FeedModule());
        install(new LocationModule());
    }

    /**
     * @return Firebase Authentication Module
     */
    private static FirebaseAuthenticationModule getAuthenticationModule() {
        Config config = ConfigFactory.load().getConfig("services.firebase");
        final String projectId = config.getString("projectId");

        try {
            if (config.hasPath("base64")) {
                byte[] bytes = Base64.getDecoder().decode(config.getString("base64"));
                InputStream inputStream = new ByteArrayInputStream(bytes);
                GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
                return new FirebaseAuthenticationModule(projectId, credentials);
            }

            String ssmKey = config.getString("ssmKey");
            AWSSimpleSystemsManagement client = AWSSimpleSystemsManagementClientBuilder.defaultClient();
            GetParameterResult result = client.getParameter(
                    new GetParameterRequest().withName(ssmKey).withWithDecryption(true)
            );

            String value = result.getParameter().getValue();
            InputStream inputStream = IOUtils.toInputStream(value, "utf-8");
            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            return new FirebaseAuthenticationModule(projectId, credentials);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
