package app.munch.api;

import app.munch.api.migration.MigrationModule;
import app.munch.api.social.SocialModule;
import app.munch.database.DatabaseModule;
import app.munch.elastic.ElasticModule;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import munch.api.core.CoreModule;
import munch.api.feed.FeedModule;
import munch.api.location.LocationModule;
import munch.api.search.SearchModule;

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
        install(new ElasticModule());

        // Service Module
        install(new CoreModule());
        install(new DataModule());
        install(new AdminModule());

        install(new PageModule());
        install(new AccountModule());
        install(new PlaceModule());
        install(new SocialModule());

        install(new MigrationModule());

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
