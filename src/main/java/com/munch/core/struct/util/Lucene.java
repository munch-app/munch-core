package com.munch.core.struct.util;

import com.spatial4j.core.shape.Point;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.dsl.Unit;

import javax.persistence.EntityManager;

/**
 * Lucene helper
 */
public class Lucene {

    protected EntityManager entityManager;

    protected Lucene(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static Lucene init(EntityManager entityManager) {
        return new Lucene(entityManager);
    }

    protected FullTextEntityManager getFullTextEntityManager() {
        return org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
    }

    protected QueryBuilder buildQuery(FullTextEntityManager manager, Class clazz) {
        return manager.getSearchFactory().buildQueryBuilder().forEntity(clazz).get();
    }

    /**
     * Build index and wait for it to complete
     *
     * @throws InterruptedException
     */
    public void buildIndexWait() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }

    /**
     * Build index async
     */
    public void buildIndexAsync() {
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().start();
    }

    public interface Distance<T> {

        /**
         * Location
         *
         * @param point  4326
         * @param radius km
         * @return Query
         */
        default T distance(Point point, double radius) {
            return distance(point.getY(), point.getX(), radius);
        }

        /**
         * @param lat    4326
         * @param lng    4326
         * @param radius km
         * @return Query
         */
        T distance(double lat, double lng, double radius);

        default org.apache.lucene.search.Query distanceQuery(QueryBuilder builder, double lat, double lng, double radius) {
            return builder.spatial()
                    .within(radius, Unit.KM)
                    .ofLatitude(lat).andLongitude(lng)
                    .createQuery();
        }
    }
}
