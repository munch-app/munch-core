package com.munch.accounts;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.utils.spark.SparkController;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.pac4j.core.config.Config;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.credentials.password.ShiroPasswordEncoder;
import spark.TemplateEngine;

import java.util.regex.Pattern;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 3:37 PM
 * Project: munch-core
 */
public class AccountsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TemplateEngine.class).toInstance(SparkController.templateEngine);
    }

    @Provides
    @Singleton
    Config providePacConfig(PacConfigFactory configFactory) {
        return configFactory.build();
    }

    @Provides
    @Singleton
    PasswordEncoder providePasswordEncoder() {
        PasswordService service = new DefaultPasswordService();
        return new ShiroPasswordEncoder(service);
    }

    @Provides
    @Singleton
    @Named("PasswordPattern")
    Pattern providesPasswordPattern() {
        return Pattern.compile("^([0-9]+[a-zA-Z]+|[a-zA-Z]+[0-9]+)[0-9a-zA-Z]*$");
    }

    @Provides
    com.typesafe.config.Config provideConfig() {
        return com.typesafe.config.ConfigFactory.load().getConfig("munch.accounts");
    }
}
