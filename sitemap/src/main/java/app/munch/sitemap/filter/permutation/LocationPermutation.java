package app.munch.sitemap.filter.permutation;

import javax.inject.Singleton;
import java.util.Collections;
import java.util.Iterator;

/**
 * @author Fuxing Loh
 * @since 2019-11-30 at 03:24
 */
@Singleton
public final class LocationPermutation extends Permutation {
    @Override
    public Iterator<Object> get() {
        return Collections.emptyIterator();
    }
}
