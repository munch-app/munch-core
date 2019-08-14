package app.munch.database;

import com.google.inject.AbstractModule;
import dev.fuxing.postgres.PostgresModule;

/**
 * Created by: Fuxing
 * Date: 2019-07-15
 * Time: 21:32
 */
public final class DatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new PostgresModule("database.postgres"));
    }
}
