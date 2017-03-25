package munch.document;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:32 AM
 * Project: munch-core
 */
public class PostgresModule extends AbstractModule {

    protected static final String UnitName = "munchStructPersistenceUnit";

    @Override
    protected void configure() {
        setupDatabase();
        bind(DocumentStore.class).to(PostgresStore.class);
        bind(DocumentQuery.class).to(PostgresQuery.class);
    }

    /**
     * Setup postgres database module
     */
    protected void setupDatabase() {
        Config config = ConfigFactory.load().getConfig("munch.document");

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "munch.document.hibernate.JsonPostgreSQLDialect");
        properties.put("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");

        properties.put("hibernate.hikari.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.put("hibernate.hikari.dataSource.url", config.getString("url"));
        properties.put("hibernate.hikari.dataSource.user", config.getString("username"));
        properties.put("hibernate.hikari.dataSource.password", config.getString("password"));

        // Disable by default due to this error: found [bpchar (Types#CHAR)], but expecting [char(36) (Types#VARCHAR)]
        properties.put("hibernate.hbm2ddl.auto", config.getBoolean("autoCreate") ? "update" : "none");
        properties.put("hibernate.hikari.maximumPoolSize", String.valueOf(config.getInt("maxPoolSize")));

        HibernateUtils.setupFactory(UnitName, properties);
    }

    @Provides
    @Singleton
    @Named("struct")
    TransactionProvider provideTransactionProvider() {
        return HibernateUtils.get(UnitName);
    }
}