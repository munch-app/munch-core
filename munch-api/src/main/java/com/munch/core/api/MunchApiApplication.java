package com.munch.core.api;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.munch.core.api.modules.MunchApiModule;
import com.munch.core.struct.rdbms.admin.AdminAccount;
import com.scottescue.dropwizard.entitymanager.EntityManagerBundle;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

/**
 * Created by: Fuxing
 * Date: 6/11/2016
 * Time: 9:57 PM
 * Project: munch-core
 */
public class MunchApiApplication extends Application<MunchApiConfiguration> {

    public static void main(String[] args) throws Exception {
        new MunchApiApplication().run("server", "munch-api/src/main/resources/munch-api.yml");
    }

    @Override
    public String getName() {
        return "munch-api";
    }

    private final EntityManagerBundle<MunchApiConfiguration> entityManagerBundle =
            new EntityManagerBundle<MunchApiConfiguration>(AdminAccount.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(MunchApiConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    private final GuiceBundle<MunchApiConfiguration> guiceBundle = GuiceBundle.<MunchApiConfiguration>newBuilder()
            .addModule(new MunchApiModule(entityManagerBundle))
            .enableAutoConfig("com.munch.core.api.resources")
            .setConfigClass(MunchApiConfiguration.class)
            .build();

    @Override
    public void initialize(Bootstrap<MunchApiConfiguration> bootstrap) {
        bootstrap.addBundle(guiceBundle);
        bootstrap.addBundle(entityManagerBundle);
    }

    @Override
    public void run(MunchApiConfiguration configuration, Environment environment) {
        // don't need to add resources, tasks, healthchecks or providers
    }

}
