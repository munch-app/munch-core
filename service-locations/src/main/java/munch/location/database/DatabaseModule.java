package munch.location.database;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;
import munch.location.Location;
import munch.location.reader.MrtReader;
import org.hibernate.search.jpa.Search;

import java.io.IOException;
import java.util.List;


/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 3:29 PM
 * Project: munch-core
 */
public class DatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        setupDatabase();
        requestInjection(this);
    }

    /**
     * Setup database for geo functions
     */
    private void setupDatabase() {
        TransactionProvider provider = HibernateUtils.setupFactory("locationPersistenceUnit", null);
        provider.with(em -> {
            //noinspection SqlNoDataSourceInspection
            em.createNativeQuery("CREATE ALIAS ST_Within FOR \"geodb.GeoDB.ST_Within\"").executeUpdate();
            try {
                Search.getFullTextEntityManager(em).createIndexer().startAndWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Load all features into database
     *
     * @param provider provider
     * @throws IOException exceptions
     */
    @Inject
    private void loadFeatures(TransactionProvider provider) throws IOException {
        MrtReader reader = new MrtReader();
        List<Location> list = reader.read();

        // Persist all Mrt Location
        provider.with(em -> list.forEach(em::persist));
    }

    /**
     * @return default provider for location database
     */
    @Provides
    TransactionProvider provideTransaction() {
        return HibernateUtils.get("locationPersistenceUnit");
    }
}
