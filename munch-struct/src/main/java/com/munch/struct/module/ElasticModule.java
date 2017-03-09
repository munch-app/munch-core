package com.munch.struct.module;

import com.google.inject.AbstractModule;
import com.munch.struct.utils.ElasticSearchDatabase;
import com.munch.struct.utils.SearchDatabase;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:49 PM
 * Project: munch-core
 */
public class ElasticModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SearchDatabase.class).to(ElasticSearchDatabase.class);
    }

    // TODO setup es module

}
