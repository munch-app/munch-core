package com.munch.accounts;

import com.munch.hibernate.utils.HibernateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 7:23 PM
 * Project: munch-core
 */
public class H2MemModule extends DatabaseModule {

    @Override
    protected void configure() {
        setupDatabase();
    }

    /**
     * Setup h2 database
     */
    private void setupDatabase() {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        properties.put("hibernate.connection.driver_class", "org.h2.Driver");
        properties.put("hibernate.connection.username", "sa");
        properties.put("hibernate.connection.password", "");

        properties.put("hibernate.connection.url", "jdbc:h2:mem:corpus-mem;DB_CLOSE_DELAY=-1");
        properties.put("hibernate.hbm2ddl.auto", "create-drop");

        HibernateUtils.setupFactory("accountPersistenceUnit", properties);
    }

}
