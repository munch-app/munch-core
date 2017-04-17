package munch.api;

import com.google.inject.AbstractModule;
import munch.data.PostgresModule;
import munch.search.ElasticModule;

import java.util.function.Supplier;

/**
 * Created By: Fuxing Loh
 * Date: 22/3/2017
 * Time: 9:54 PM
 * Project: munch-core
 */
public interface TestModules {

    Supplier<AbstractModule> Elastic = ElasticModule::new;
    Supplier<AbstractModule> Postgres = PostgresModule::new;

}
