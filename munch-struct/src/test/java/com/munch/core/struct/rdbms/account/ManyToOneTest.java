package com.munch.core.struct.rdbms.account;

import com.munch.core.struct.rdbms.locality.Country;
import com.munch.core.struct.rdbms.locality.CountryTest;
import com.munch.core.struct.util.HibernateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created By: Fuxing Loh
 * Date: 29/9/2016
 * Time: 7:42 PM
 * Project: struct
 */
public class ManyToOneTest {

    private long countryId = 999_999_999_999_999L;
    private String accountId;

    @Before
    public void setUp() throws Exception {
        Country country = CountryTest.create(countryId);
        country.setName("Singapore Island");
        country.setCode("ZZZ");
        country.setIso("ZZZ");

        HibernateUtil.with(em -> em.persist(country));
    }

    @After
    public void tearDown() throws Exception {
        if (accountId != null) {
            HibernateUtil.with(em -> em.remove(em.find(Account.class, accountId)));
        }
        HibernateUtil.with(em -> em.remove(em.find(Country.class, countryId)));
    }

    /**
     * Test mapped by work as expected
     */
    @Test
    public void mappedBy() throws Exception {
        accountId = HibernateUtil.reduce(em -> {
            Account account = new Account();
            account.setEmail("lohfuxing@munch.com");
            account.setCountry(em.find(Country.class, countryId));
            account.setType(Account.TYPE_NORMAL);
            account.setFirstName("Fuxing");

            AccountAccessToken accessToken = new AccountAccessToken();
            accessToken.setType(AccountAccessToken.TYPE_IOS);
            accessToken.setUid("uid");
            accessToken.setDescription("iOS Fuxing");
            account.getAccessTokens().add(accessToken);

            em.persist(account);
            return account.getId();
        });

        HibernateUtil.with(em -> {
            Account account = em.find(Account.class, accountId);
            assertThat(account.getAccessTokens()).hasSize(1);

            assertThat(account.getAccessTokens().iterator().next().getDescription())
                    .isEqualTo("iOS Fuxing");
        });
    }
}