package com.munch.core.struct.rdbms.admin;

import com.munch.core.struct.util.HibernateUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created By: Fuxing Loh
 * Date: 2/9/2016
 * Time: 2:29 PM
 * Project: struct
 */
public class AdminAccountTest {

    PasswordUtil passwordUtil = new PasswordUtil();
    String accountId;

    @AfterEach
    public void tearDown() throws Exception {
        HibernateUtil.with(em -> em.remove(em.find(AdminAccount.class, accountId)));
    }

    @Test
    public void createAccount() throws Exception {
        accountId = HibernateUtil.reduce(em -> {
            AdminAccount account = new AdminAccount();
            account.setType(AdminAccount.TYPE_BOT);
            account.setEmail("lohfuxing@gmail.com");
            account.setHashSaltPassword(passwordUtil.hashPassword("My Awesome Password"));
            account.setName("Fuxing");
            account.setPhoneNumber("1234 5678");

            em.persist(account);
            return account.getId();
        });

        assertThat(accountId).isNotNull();

        HibernateUtil.with(em -> {
            AdminAccount account = em.find(AdminAccount.class, accountId);

            assertThat(account.getEmail()).isEqualTo("lohfuxing@gmail.com");
            assertThat(passwordUtil.check("My Awesome Password", account.getHashSaltPassword()))
                    .isEqualTo(true);
            assertThat(account.getType()).isEqualTo(AdminAccount.TYPE_BOT);
            assertThat(account.getName()).isEqualTo("Fuxing");
            assertThat(account.getPhoneNumber()).isEqualTo("1234 5678");

            assertThat(account.getLastActiveDate()).isNotNull();
            assertThat(account.getLastLoginDate()).isNotNull();
            assertThat(account.getCreatedDate()).isNotNull();
            assertThat(account.getUpdatedDate()).isNotNull();
        });
    }
}