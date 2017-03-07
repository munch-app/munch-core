package com.munch.core.struct.module;

import com.munch.hibernate.utils.HibernateUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 7:22 PM
 * Project: munch-core
 */
public class PostgresModule extends DatabaseModule {

    @Override
    protected void configure() {
        setupDatabase();
    }

    /**
     * Setup testing h2 database
     */
    private void setupDatabase() {
        Config config = ConfigFactory.load().getConfig("munch.struct.database");

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
        properties.put("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");

        properties.put("hibernate.hikari.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.put("hibernate.hikari.dataSource.url", config.getString("url"));
        properties.put("hibernate.hikari.dataSource.user", config.getString("username"));
        properties.put("hibernate.hikari.dataSource.password", config.getString("password"));

        // Disable by default due to this error: found [bpchar (Types#CHAR)], but expecting [char(36) (Types#VARCHAR)]
        properties.put("hibernate.hbm2ddl.auto", "none");

        properties.put("hibernate.hikari.maximumPoolSize", String.valueOf(config.getInt("maxPoolSize")));

        HibernateUtils.setupFactory(UnitName, properties);
    }

}
