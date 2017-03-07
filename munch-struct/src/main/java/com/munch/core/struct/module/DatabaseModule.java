package com.munch.core.struct.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:32 AM
 * Project: munch-core
 */
public abstract class DatabaseModule extends AbstractModule {

    public static final String UnitName = "munchStructPersistenceUnit";

    @Provides
    @Singleton
    @Named("struct")
    TransactionProvider provideTransactionProvider() {
        return HibernateUtils.get(UnitName);
    }

}
