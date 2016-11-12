package com.munch.core.struct.util;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 7:06 PM
 * Project: struct
 */
public class EntityManagerTest {

    @Test
    public void test() throws Exception {
        EntityManager entityManager = HibernateUtil.createEntityManager();
        entityManager.close();
    }

    @Test
    @Disabled
    public void updateDatabase() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("defaultPersistenceUnit", properties);
        EntityManager entityManager = factory.createEntityManager();
        entityManager.close();
        factory.close();
    }
}
