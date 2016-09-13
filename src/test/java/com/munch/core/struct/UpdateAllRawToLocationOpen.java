package com.munch.core.struct;

import com.munch.core.struct.rdbms.source.SeedPlaceTrack;
import com.munch.core.struct.util.HibernateUtil;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 13/9/2016
 * Time: 7:03 PM
 * Project: struct
 */
public class UpdateAllRawToLocationOpen {


    public static void main(String[] args) {
        EntityManager entityManager = HibernateUtil.createEntityManager();
        entityManager.getTransaction().begin();

        List<SeedPlaceTrack> list = entityManager
                .createQuery("SELECT spt FROM SeedPlaceTrack spt WHERE spt.status = 100", SeedPlaceTrack.class).getResultList();
        for (SeedPlaceTrack seedPlaceTrack : list) {
            seedPlaceTrack.setStatus(SeedPlaceTrack.STATUS_LOCATION_OPEN);
            entityManager.persist(seedPlaceTrack);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

}
