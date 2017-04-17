package munch.data;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import munch.restful.server.WaitFor;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:32 AM
 * Project: munch-core
 */
public class PostgresModule extends AbstractModule {

    protected static final String UnitName = "munchPersistenceUnit";

    @Override
    protected void configure() {
        setupDatabase();
    }

    /**
     * Wait for database to be read before starting postgres module
     * Setup postgres database module
     */
    private void setupDatabase() {
        Config config = ConfigFactory.load().getConfig("postgres");
        String url = config.getString("url");
        WaitFor.host(url.substring(5), Duration.ofSeconds(30));

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "munch.data.hibernate.JsonPostgreSQLDialect");
        properties.put("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");

        properties.put("hibernate.hikari.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.put("hibernate.hikari.dataSource.url", url);
        properties.put("hibernate.hikari.dataSource.user", config.getString("username"));
        properties.put("hibernate.hikari.dataSource.password", config.getString("password"));

        // Disable by default due to this error: found [bpchar (Types#CHAR)], but expecting [char(36) (Types#VARCHAR)]
        properties.put("hibernate.hbm2ddl.auto", config.getBoolean("autoCreate") ? "update" : "none");
        properties.put("hibernate.hikari.maximumPoolSize", String.valueOf(config.getInt("maxPoolSize")));

        HibernateUtils.setupFactory(UnitName, properties);
    }

    @Provides
    @Singleton
    TransactionProvider provideTransactionProvider() {
        return HibernateUtils.get(UnitName);
    }
}
