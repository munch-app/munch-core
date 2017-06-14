package munch.location.database;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;
import org.hibernate.search.jpa.Search;


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
    }

    private void setupDatabase() {
        TransactionProvider provider = HibernateUtils
                .setupFactory("geocoderPersistenceUnit", null);
        provider.with(em -> {
            em.createNativeQuery("CREATE ALIAS ST_Within FOR \"geodb.GeoDB.ST_Within\"").executeUpdate();
            try {
                Search.getFullTextEntityManager(em).createIndexer().startAndWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * @return default provider for geocoder database
     */
    @Provides
    TransactionProvider provideTransaction() {
        return HibernateUtils.get("geocoderPersistenceUnit");
    }
}
