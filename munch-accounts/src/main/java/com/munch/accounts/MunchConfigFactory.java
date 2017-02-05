package com.munch.accounts;

import org.pac4j.core.client.Clients;
import org.pac4j.core.config.Config;
import org.pac4j.core.config.ConfigFactory;
import org.pac4j.core.matching.ExcludedPathMatcher;
import org.pac4j.http.client.indirect.FormClient;
import org.pac4j.oauth.client.FacebookClient;
import spark.TemplateEngine;

/**
 * Created by: Fuxing
 * Date: 5/2/2017
 * Time: 1:39 AM
 * Project: munch-core
 */
public class MunchConfigFactory implements ConfigFactory {

    private final TemplateEngine templateEngine;

    public MunchConfigFactory(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Config build() {
        final FacebookClient facebookClient = new FacebookClient("145278422258960", "be21409ba8f39b5dae2a7de525484da8");
        final FormClient formClient = new FormClient("http://localhost:8080/loginForm", new EmailAuthenticator());

        final Clients clients = new Clients("http://localhost:8080/callback", facebookClient, formClient);

        final Config config = new Config(clients);
        config.addMatcher("excludedPath", new ExcludedPathMatcher("^/facebook/notprotected$"));
        config.setHttpActionAdapter(new WebActionAdapter(templateEngine));
        return config;
    }

}
