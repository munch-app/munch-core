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
        // Core Modules
        install(new FirebaseModule());
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
     * Start api server with predefined modules
     *
     * @param args not required
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ApiModule());
        injector.getInstance(ApiServer.class).start();
    }
}
