package com.munch.core.struct.module;

import com.munch.hibernate.utils.HibernateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 7:53 PM
 * Project: munch-core
 */
public class H2FileModule extends DatabaseModule {

    @Override
    protected void configure() {
        setupDatabase();
    }

    /**
     * Setup testing h2 database
     */
    private void setupDatabase() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.connection.driver_class", "org.h2.Driver");
        properties.put("hibernate.connection.username", "sa");
        properties.put("hibernate.connection.password", "");

        properties.put("hibernate.connection.url", "jdbc:h2:file:./var/h2/munch-core");
        properties.put("hibernate.hbm2ddl.auto", "update");

        HibernateUtils.setupFactory(UnitName, properties);
    }
}
