package com.munch.core.struct.util;

import org.junit.Test;

import javax.persistence.EntityManager;

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

}
