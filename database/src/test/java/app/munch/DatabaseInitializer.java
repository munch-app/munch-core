package app.munch;

import app.munch.database.DatabaseModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.fuxing.jpa.TransactionProvider;

/**
 * Created by: Fuxing
 * Date: 20/8/19
 * Time: 6:08 pm
 */
public class DatabaseInitializer {

    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new DatabaseModule());
        TransactionProvider provider = injector.getInstance(TransactionProvider.class);
    }
}
