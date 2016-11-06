package com.munch.core.struct.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Thread-Safe Singleton Hibernate Util
 * Only use this if there is no default implementation available on your platform.
 * Created by Fuxing
 * Date: 1/1/2015
 * Time: 5:49 PM
 * Project: PuffinCore
 */
public final class HibernateUtil {
    private static EntityManagerFactory entityManagerFactory = null;

    private HibernateUtil() {/* NOT Suppose to init */}

    /**
     * Override the factory for testing
     * Factory overriding also allow source code to determine which factory to use. aka injecting
     *
     * @param factory factory to override
     */
    static void overrideFactory(EntityManagerFactory factory) {
        if (entityManagerFactory == null) {
            entityManagerFactory = factory;
        } else {
            throw new RuntimeException(new IllegalStateException("Factory already initialized."));
        }
    }

    /**
     * Thread-Safe Create
     *
     * @return EntityFactory Singleton
     */
    private static EntityManagerFactory getEntityInstance() {
        if (entityManagerFactory == null) {
            synchronized (HibernateUtil.class) {
                if (entityManagerFactory == null) {
                    entityManagerFactory = Persistence.createEntityManagerFactory("defaultPersistenceUnit");
                }
            }
        }
        return entityManagerFactory;
    }

    /**
     * Get a entity factory
     *
     * @return EntityFactory Singleton
     */
    public static EntityManagerFactory getEntityFactory() {
        return getEntityInstance();
    }

    /**
     * Build a entity manager from the existing factory
     *
     * @return EntityManager
     */
    public static EntityManager createEntityManager() {
        return getEntityFactory().createEntityManager();
    }

    /**
     * Shutdown the instance
     * Thread-safe
     */
    public static void shutdown() {
        if (entityManagerFactory != null) {
            synchronized (HibernateUtil.class) {
                if (entityManagerFactory != null) {
                    getEntityFactory().close();
                    entityManagerFactory = null;
                }
            }
        }
    }

    /**
     * Run jpa style transaction in functional style
     * Using the default transaction provider
     *
     * @param transaction transaction to run
     */
    public static void with(TransactionProvider.Transaction transaction) {
        TransactionProvider.getProvider().with(transaction);
    }

    /**
     * Run jpa style transaction in functional style with reduce
     * Using the default transaction provider
     *
     * @param reduceTransaction reduce transaction to run
     * @param <T>               type of object
     * @return object
     */
    public static <T> T reduce(TransactionProvider.ReduceTransaction<T> reduceTransaction) {
        return TransactionProvider.getProvider().reduce(reduceTransaction);
    }

}

