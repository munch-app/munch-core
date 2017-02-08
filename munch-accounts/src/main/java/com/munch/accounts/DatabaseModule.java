package com.munch.accounts;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 7:25 PM
 * Project: munch-core
 */
public abstract class DatabaseModule extends AbstractModule {

    @Provides
    TransactionProvider provideTransactionProvider() {
        return HibernateUtils.get("accountPersistenceUnit");
    }

}
