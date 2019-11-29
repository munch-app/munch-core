package app.munch.sitemap.filter;

import app.munch.sitemap.filter.permutation.LocationPermutation;
import app.munch.sitemap.filter.permutation.Permutation;
import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * @author Fuxing Loh
 * @since 2019-11-30 at 04:16
 */
public final class FilterModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Permutation> binder = Multibinder.newSetBinder(binder(), Permutation.class);
        binder.addBinding().to(LocationPermutation.class);
    }
}
