package munch.location.database;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.munch.hibernate.utils.TransactionProvider;
import munch.location.reader.FeatureReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 14/4/2017
 * Time: 6:42 PM
 * Project: munch-core
 */
class FeatureReaderTest {

    FeatureReader importer;
    TransactionProvider provider;

    @BeforeEach
    void setUp() throws Exception {
        Injector injector = Guice.createInjector(new DatabaseModule());
        importer = injector.getInstance(FeatureReader.class);
        provider = injector.getInstance(TransactionProvider.class);
    }

    @Test
    void importRegion() throws Exception {
        importer.importSubzone();
        provider.with(em -> {
            List<Region> list = em.createQuery("SELECT r FROM Region r", Region.class).getResultList();
//            for (Region region : list) {
//                System.out.println(region);
//            }
            System.out.println(list.get(0).getGeometry());
        });
    }


    @Test
    void importMrt() throws Exception {
        importer.importMrt();
        provider.with(em -> {
            List<Region> list = em.createQuery("SELECT r FROM Region r", Region.class).getResultList();
            for (Region region : list) {
                System.out.println(region.getGeometry());
            }
        });
    }
}