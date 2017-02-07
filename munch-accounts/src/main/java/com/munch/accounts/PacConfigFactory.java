package com.munch.accounts;

import com.munch.accounts.controller.EmailAuthenticator;
import com.munch.accounts.controller.WebActionAdapter;
import com.munch.accounts.instagram.InstagramClient;
import com.munch.accounts.service.TokenAuthenticator;
import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.http.client.direct.HeaderClient;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.oauth.client.FacebookClient;
import spark.TemplateEngine;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 1:39 AM
 * Project: munch-core
 */
public class PacConfigFactory implements ConfigFactory {

    private final TemplateEngine templateEngine;

    public PacConfigFactory(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Config build() {
        com.typesafe.config.Config config = com.typesafe.config.ConfigFactory.load()
                .getConfig("munch.accounts");

        // Configure Facebook Login
        FacebookClient facebookClient = buildFacebook(config);

        // Configure Instagram Login
        InstagramClient instagramClient = buildInstagram(config);

        // Configure Email Login
        FormClient formClient = new FormClient("/login", new EmailAuthenticator());

        // Configure Token Authenticator
        HeaderClient tokenClient = new HeaderClient("Authorization", "Bearer ", new TokenAuthenticator());

        String baseUrl = config.getString("http.url");
        Clients clients = new Clients(baseUrl + "/callback", facebookClient, formClient, tokenClient);

        Config pacConfig = new Config(clients);
        pacConfig.setHttpActionAdapter(new WebActionAdapter(templateEngine));
        return pacConfig;
        // TODO custom session store?
    }

    /**
     * Build facebook client from application.conf
     *
     * @param config config from application.conf
     * @return Facebook Client
     */
    private FacebookClient buildFacebook(com.typesafe.config.Config config) {
        String key = config.getString("facebook.key");
        String secret = config.getString("facebook.secret");
        FacebookClient client = new FacebookClient(key, secret);
        client.setScope("public_profile,user_friends,email");
        return client;
    }

    private InstagramClient buildInstagram(com.typesafe.config.Config config) {
        String key = config.getString("instagram.key");
        String secret = config.getString("instagram.secret");
        InstagramClient client = new InstagramClient(key, secret);
        client.setScope("basic");
        return client;
    }

}
