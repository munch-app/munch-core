package com.munch.core.struct.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

/**
 * Created By: Fuxing Loh
 * Date: 12/9/2016
 * Time: 11:23 PM
 * Project: struct
 */
public class SpatialTest {

    EntityManager entityManager;

    @Before
    public void setUp() throws Exception {
        entityManager = HibernateUtil.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        entityManager.close();
    }

    @Test
    public void testBuild() throws Exception {
        Lucene.init(entityManager).buildIndexWait();
    }
}