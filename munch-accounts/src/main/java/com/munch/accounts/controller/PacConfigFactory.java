package com.munch.accounts.controller;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
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

        // Configure Instagram Login TODO

        // Configure Email Login
        FormClient formClient = buildEmail();

        final Clients clients = new Clients("http://localhost:8080/callback", facebookClient, formClient);

        final Config pacConfig = new Config(clients);
        pacConfig.setHttpActionAdapter(new WebActionAdapter(templateEngine));
        return pacConfig;
    }

    /**
     * Build facebook client from application.conf
     *
     * @param config config from application.conf
     * @return Facebook Client
     */
    private FacebookClient buildFacebook(com.typesafe.config.Config config) {
        String facebookKey = config.getString("facebook.key");
        String facebookSecret = config.getString("facebook.secret");
        return new FacebookClient(facebookKey, facebookSecret);
    }

    /**
     * @return Email Form Client
     */
    private FormClient buildEmail() {
        return new FormClient("http://localhost:8080/login", new EmailAuthenticator());
    }
}
