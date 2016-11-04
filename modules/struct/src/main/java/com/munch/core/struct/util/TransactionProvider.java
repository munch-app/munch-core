
package com.munch.core.struct.util;

import javax.persistence.EntityManager;

/**
 * Created by Fuxing
 * Date: 8/7/2015
 * Time: 4:07 PM
 * Project: PuffinCore
 */
public class TransactionProvider {

    private static TransactionProvider provider = new TransactionProvider();

    /**
     * @return get the current provider
     */
    public static TransactionProvider getProvider() {
        return provider;
    }

    /**
     * @param provider provider to override with
     */
    public static void setProvider(TransactionProvider provider) {
        TransactionProvider.provider = provider;
    }

    /**
     * Run jpa style transaction in functional style
     *
     * @param transaction transaction to run
     */
    public void with(Transaction transaction) {
        // Create and start
        EntityManager entityManager = HibernateUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            // Run
            transaction.run(entityManager);
            // Close
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    /**
     * Run jpa style transaction in functional style with reduce
     *
     * @param reduceTransaction reduce transaction to run
     * @param <T>               type of object
     * @return object
     */
    public <T> T reduce(ReduceTransaction<T> reduceTransaction) {
        T object = null;
        // Create and start
        EntityManager entityManager = HibernateUtil.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            // Run
            object = reduceTransaction.run(entityManager);
            // Close
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
        return object;
    }

    @FunctionalInterface
    public interface Transaction {
        void run(EntityManager em);
    }

    @FunctionalInterface
    public interface ReduceTransaction<T> {
        T run(EntityManager em);
    }

}
