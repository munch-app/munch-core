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

import java.util.Optional;

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

        Optional<String> hashedPassword = provider.optional(em -> em.createQuery(
                "SELECT a.password FROM Account a WHERE a.email = :email", String.class)
                .setParameter("email", email)
                .getSingleResult());

        if (hashedPassword.isPresent()) {
            if (encoder.matches(password, hashedPassword.get())) {
                CommonProfile profile = new CommonProfile();
                profile.setId(email);
                credentials.setUserProfile(profile);
            } else {
                throwsMessage("Email or Password does not match.");
            }
        } else {
            // Password is not yet set. User probably using other auth provider
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
