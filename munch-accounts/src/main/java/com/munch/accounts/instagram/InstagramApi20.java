package com.munch.accounts.instagram;

import com.github.scribejava.core.builder.api.DefaultApi20;
import com.github.scribejava.core.model.OAuthConfig;

/**
 * Created by: Fuxing
 * Date: 7/2/2017
 * Time: 2:27 PM
 * Project: munch-core
 */
public class InstagramApi20 extends DefaultApi20 {

    protected InstagramApi20() {
    }

    private static class InstanceHolder {
        private static final InstagramApi20 INSTANCE = new InstagramApi20();
    }

    public static InstagramApi20 instance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public String getAccessTokenEndpoint() {
        return "https://api.instagram.com/oauth/access_token";
    }

    @Override
    public String getAuthorizationUrl(OAuthConfig config) {
        return "https://api.instagram.com/oauth/authorize";
    }
}