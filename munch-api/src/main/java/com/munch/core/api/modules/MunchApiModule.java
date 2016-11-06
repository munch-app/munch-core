package com.munch.core.api.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.munch.core.api.MunchApiConfiguration;
import com.scottescue.dropwizard.entitymanager.EntityManagerBundle;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

/**
 * Created by: Fuxing
 * Date: 6/11/2016
 * Time: 10:33 PM
 * Project: munch-core
 */
public class MunchApiModule extends AbstractModule {

    private final EntityManagerBundle<MunchApiConfiguration> entityManagerBundle;

    public MunchApiModule(EntityManagerBundle<MunchApiConfiguration> entityManagerBundle) {
        this.entityManagerBundle = entityManagerBundle;
    }

    @Override
    protected void configure() {

    }

    @Provides
    @Singleton
    EntityManagerBundle<MunchApiConfiguration> providesEntityManagerBundle() {
        return entityManagerBundle;
    }

    @Provides
    EntityManager providesEntityManager(EntityManagerBundle<MunchApiConfiguration> entityManagerBundle) {
        return entityManagerBundle.getSharedEntityManager();
    }

}
