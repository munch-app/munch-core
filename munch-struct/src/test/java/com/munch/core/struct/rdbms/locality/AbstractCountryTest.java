package com.munch.core.struct.rdbms.locality;

import com.munch.core.struct.rdbms.EntityTestInterface;
import com.munch.core.struct.util.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.persistence.EntityManager;

/**
 * Created by: Fuxing
 * Date: 13/11/2016
 * Time: 12:20 AM
 * Project: munch-core
 */
public abstract class AbstractCountryTest implements EntityTestInterface {

    private static long countryId = 999_999_999_999_999L;

    @BeforeAll
    public static void setUpCountry() throws Exception {
        HibernateUtil.with(em -> {
            if (em.find(Country.class, countryId) == null) {
                Country country = new Country();
                country.setId(countryId);
                country.setName("Singapore Islands");
                country.setCode("ZZZ");
                country.setIso("ZZZ");
                em.persist(country);
            }
        });
    }

    @AfterAll
    public static void tearDownCountry() throws Exception {
        HibernateUtil.with(em -> {
            Country country = em.find(Country.class, countryId);
            if (country != null) {
                em.remove(country);
            }
        });
    }

    public static Country getCountry(EntityManager entityManager) {
        return entityManager.find(Country.class, countryId);
    }

}
