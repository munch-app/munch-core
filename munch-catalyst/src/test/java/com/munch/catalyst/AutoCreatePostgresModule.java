package com.munch.catalyst;

import com.munch.hibernate.utils.HibernateUtils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 15/3/2017
 * Time: 6:08 PM
 * Project: munch-core
 */
class AutoCreatePostgresModule extends com.munch.struct.module.PostgresModule {
    @Override
    protected void setupDatabase() {
        Config config = ConfigFactory.load().getConfig("munch.struct.document");

        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "com.munch.struct.hibernate.JsonPostgreSQLDialect");
        properties.put("hibernate.connection.provider_class", "com.zaxxer.hikari.hibernate.HikariConnectionProvider");

        properties.put("hibernate.hikari.dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
        properties.put("hibernate.hikari.dataSource.url", config.getString("url"));
        properties.put("hibernate.hikari.dataSource.user", config.getString("username"));
        properties.put("hibernate.hikari.dataSource.password", config.getString("password"));

        properties.put("hibernate.hbm2ddl.auto", "update");

        properties.put("hibernate.hikari.maximumPoolSize", String.valueOf(config.getInt("maxPoolSize")));

        HibernateUtils.setupFactory(UnitName, properties);
    }
}
