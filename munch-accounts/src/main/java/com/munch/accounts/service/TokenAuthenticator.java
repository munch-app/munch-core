package com.munch.accounts.service;

import org.pac4j.core.context.WebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.authenticator.Authenticator;
import org.pac4j.core.exception.HttpAction;

/**
 * Created by: Fuxing
 * Date: 6/2/2017
 * Time: 8:39 PM
 * Project: munch-core
 */
public class TokenAuthenticator implements Authenticator<TokenCredentials> {

    @Override
    public void validate(TokenCredentials credentials, WebContext context) throws HttpAction {
        String token = credentials.getToken();
        // TODO validate token
    }

}
