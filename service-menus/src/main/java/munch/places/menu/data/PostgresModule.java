package munch.places.menu.data;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;
import com.typesafe.config.Config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 22/4/2017
 * Time: 5:50 PM
 * Project: munch-core
 */
public class PostgresModule extends AbstractModule {

    private static final String UnitName = "menuPersistenceUnit";

    @Override
    protected void configure() {
        requestInjection(this);
    }

    /**
     * Wait for database to be read before starting postgres module
     * Setup postgres database module
     */
    @Inject
    void setupDatabase(Config config) {
        Config postgres = config.getConfig("postgres");

        // Wait for url to be ready
        String url = postgres.getString("url");
        WaitFor.host(url.substring(5), Duration.ofSeconds(35));

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hikari.dataSource.url", url);
        properties.put("hibernate.hikari.dataSource.user", postgres.getString("username"));
        properties.put("hibernate.hikari.dataSource.password", postgres.getString("password"));

        properties.put("hibernate.hikari.maximumPoolSize", postgres.getString("maxPoolSize"));
        // Disable by default due to this error: found [bpchar (Types#CHAR)], but expecting [char(36) (Types#VARCHAR)]
        String autoCreate = postgres.getBoolean("autoCreate") ? "update" : "none";
        properties.put("hibernate.hbm2ddl.auto", autoCreate);

        HibernateUtils.setupFactory(UnitName, properties);
    }

    @Provides
    @Singleton
    TransactionProvider provideTransactionProvider() {
        return HibernateUtils.get(UnitName);
    }
}
