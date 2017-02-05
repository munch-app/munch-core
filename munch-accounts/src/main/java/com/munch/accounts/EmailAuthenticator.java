package com.munch.accounts;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.http.credentials.authenticator.test.SimpleTestUsernamePasswordAuthenticator;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 5:41 AM
 * Project: munch-core
 */
public class EmailAuthenticator implements Authenticator<UsernamePasswordCredentials> {
    SimpleTestUsernamePasswordAuthenticator authenticator = new SimpleTestUsernamePasswordAuthenticator();

    @Override
    public void validate(UsernamePasswordCredentials credentials, WebContext context) throws HttpAction {
        authenticator.validate(credentials, context);
    }
}
