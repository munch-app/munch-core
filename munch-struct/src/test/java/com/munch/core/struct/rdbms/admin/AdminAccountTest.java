package com.munch.core.struct.rdbms.admin;

import com.munch.core.essential.security.PasswordUtil;
import com.munch.core.struct.util.HibernateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Created By: Fuxing Loh
 * Date: 2/9/2016
 * Time: 2:29 PM
 * Project: struct
 */
public class AdminAccountTest {

    EntityManager entityManager;
    PasswordUtil passwordUtil;
    AdminAccount account;

    @Before
    public void setUp() throws Exception {
        passwordUtil = new PasswordUtil();
        entityManager = HibernateUtil.createEntityManager();
    }

    @After
    public void tearDown() throws Exception {
        entityManager.getTransaction().begin();
        entityManager.remove(account);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Test
    public void createAccount() throws Exception {
        account = new AdminAccount();
        account.setType(AdminAccount.TYPE_BOT);
        account.setEmail("lohfuxing@gmail.com");
        account.setHashSaltPassword(passwordUtil.hashPassword("My Awesome Password"));
        account.setName("Fuxing");
        account.setPhoneNumber("1234 5678");

        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();

        assertThat(account.getId()).isNotNull();
        AdminAccount getAccount = entityManager.find(AdminAccount.class, account.getId());

        assertThat(getAccount.getEmail()).isEqualTo(account.getEmail());
        assertThat(passwordUtil.check("My Awesome Password", getAccount.getHashSaltPassword())).isEqualTo(true);
        assertThat(getAccount.getType()).isEqualTo(account.getType());
        assertThat(getAccount.getName()).isEqualTo(account.getName());
        assertThat(getAccount.getPhoneNumber()).isEqualTo(account.getPhoneNumber());

        assertThat(getAccount.getLastActiveDate()).isNotNull();
        assertThat(getAccount.getLastLoginDate()).isNotNull();
        assertThat(getAccount.getCreatedDate()).isNotNull();
        assertThat(getAccount.getUpdatedDate()).isNotNull();
    }
}