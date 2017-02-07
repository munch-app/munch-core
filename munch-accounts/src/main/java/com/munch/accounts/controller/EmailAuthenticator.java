package com.munch.accounts.controller;

import com.munch.hibernate.utils.HibernateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.credentials.password.ShiroPasswordEncoder;
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
public class EmailAuthenticator implements Authenticator<UsernamePasswordCredentials> {

    private final PasswordEncoder encoder;

    public EmailAuthenticator() {
        PasswordService service = new DefaultPasswordService();
        this.encoder = new ShiroPasswordEncoder(service);
    }

    @Override
    public void validate(UsernamePasswordCredentials credentials, WebContext context) throws HttpAction {
        String email = credentials.getUsername();
        String password = credentials.getPassword();
        if (StringUtils.isBlank(email)) {
            throwsException("Email cannot be blank.");
        }
        if (StringUtils.isBlank(password)) {
            throwsException("Password cannot be blank.");
        }

        Optional<String> hashedPassword = HibernateUtils.optional(em -> em.createQuery(
                "SELECT a.password FROM Account a WHERE a.email = :email", String.class)
                .setParameter("email", email)
                .getSingleResult());

        if (hashedPassword.isPresent()) {
            if (encoder.matches(password, hashedPassword.get())) {
                CommonProfile profile = new CommonProfile();
                profile.setId(email);
                credentials.setUserProfile(profile);
            } else {
                throwsException("Email or Password does not match.");
            }
        } else {
            // TODO something if no password
        }
    }

    /**
     * @param message failure message to throw
     */
    protected void throwsException(final String message) {
        throw new CredentialsException(message);
    }
}
