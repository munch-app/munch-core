package com.munch.accounts.controller;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.core.profile.CommonProfile;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 5:41 AM
 * Project: munch-core
 */
@Singleton
public class EmailAuthenticator implements Authenticator<UsernamePasswordCredentials> {

    private final TransactionProvider provider;
    private final PasswordEncoder encoder;

    /**
     * Email authenticator with default password service from shiro, SHA-256
     *
     * @param provider transaction provider to read account from database
     */
    @Inject
    public EmailAuthenticator(TransactionProvider provider, PasswordEncoder encoder) {
        this.provider = provider;
        this.encoder = encoder;
    }

    /**
     * @param credentials credentials to validate
     * @param context     web context
     * @throws HttpAction           exception
     * @throws CredentialsException message to show user
     */
    @Override
    public void validate(UsernamePasswordCredentials credentials, WebContext context) throws HttpAction, CredentialsException {
        String email = credentials.getUsername();
        String password = credentials.getPassword();
        if (StringUtils.isBlank(email)) {
            throwsMessage("Email cannot be blank.");
        }
        if (StringUtils.isBlank(password)) {
            throwsMessage("Password cannot be blank.");
        }

        boolean success = provider.reduce(em -> {
            List<String> passwords = em.createQuery("SELECT a.password FROM Account a WHERE a.email = :email", String.class)
                    .setParameter("email", email)
                    .getResultList();

            if (passwords.isEmpty()) {
                return false;
            }
            if (!encoder.matches(password, passwords.get(0))) {
                return false;
            }

            // Update last login date
            em.createQuery("UPDATE Account a " +
                    "SET a.lastLoginDate = :timeNow WHERE a.email = :email")
                    .setParameter("timeNow", new Timestamp(System.currentTimeMillis()))
                    .setParameter("email", email)
                    .executeUpdate();

            CommonProfile profile = new CommonProfile();
            profile.setId(email);
            credentials.setUserProfile(profile);
            return true;
        });

        if (!success) {
            throwsMessage("Email or Password does not match.");
        }
    }

    /**
     * @param message failure message to throw
     */
    protected void throwsMessage(final String message) {
        throw new CredentialsException(message);
    }
}
