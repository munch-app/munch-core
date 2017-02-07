package com.munch.accounts.instagram;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.scribejava.core.builder.api.BaseApi;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.oauth.OAuth20Service;
import org.pac4j.core.exception.HttpAction;
import org.pac4j.oauth.client.BaseOAuth20StateClient;
import org.pac4j.oauth.profile.JsonHelper;

/**
 * Created by: Fuxing
 * Date: 7/2/2017
 * Time: 2:17 PM
 * Project: munch-core
 */
public class InstagramClient extends BaseOAuth20StateClient<InstagramProfile> {

    private String scope = "basic";

    public InstagramClient(final String key, final String secret) {
        setKey(key);
        setSecret(secret);
    }

    @Override
    protected BaseApi<OAuth20Service> getApi() {
        return InstagramApi20.instance();
    }

    @Override
    protected String getOAuthScope() {
        return this.scope;
    }

    @Override
    protected String getProfileUrl(OAuth2AccessToken accessToken) {
        return "https://api.instagram.com/v1/users/self/?access_token=" + accessToken.getAccessToken();
    }

    @Override
    protected InstagramProfile extractUserProfile(String body) throws HttpAction {
        InstagramProfile profile = new InstagramProfile();
        final JsonNode json = JsonHelper.getFirstNode(body);

        return profile;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
