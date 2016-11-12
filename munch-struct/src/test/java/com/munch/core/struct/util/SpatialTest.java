package com.munch.core.struct.util;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;

/**
 * Created By: Fuxing Loh
 * Date: 12/9/2016
 * Time: 11:23 PM
 * Project: struct
 */
public class SpatialTest {

    EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception {
        entityManager = HibernateUtil.createEntityManager();
    }

    @AfterEach
    public void tearDown() throws Exception {
        entityManager.close();
    }

    @Test
    public void testBuild() throws Exception {
        Lucene.init(entityManager).buildIndexWait();
    }
}