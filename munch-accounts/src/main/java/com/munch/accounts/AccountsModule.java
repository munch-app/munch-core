package com.munch.accounts;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.HibernateUtils;
import com.munch.hibernate.utils.TransactionProvider;
import com.munch.utils.spark.SparkController;
import org.pac4j.core.config.Config;

/**
 * Created By: Fuxing Loh
 * Date: 8/2/2017
 * Time: 3:37 PM
 * Project: munch-core
 */
public class AccountsModule extends AbstractModule {

    @Override
    protected void configure() {

    }

    @Provides
    TransactionProvider provideTransactionProvider() {
        return HibernateUtils.get("accountPersistenceUnit");
    }

    @Provides
    @Singleton
    Config providePacConfig(TransactionProvider transactionProvider) {
        return new PacConfigFactory(SparkController.templateEngine, transactionProvider).build();
    }

}
