package munch.sitemap.sgp;

import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import munch.api.search.data.NamedSearchQuery;
import munch.sitemap.SitemapProvider;

import javax.inject.Inject;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by: Fuxing
 * Date: 30/10/18
 * Time: 8:53 AM
 * Project: munch-core
 */
public final class SGPSearchSitemap implements SitemapProvider {

    private final SGPAnywherePermutation anywherePermutation;
    private final SGPLocationPermutation locationPermutation;

    @Inject
    public SGPSearchSitemap(SGPAnywherePermutation anywherePermutation, SGPLocationPermutation locationPermutation) {
        this.anywherePermutation = anywherePermutation;
        this.locationPermutation = locationPermutation;
    }

    @Override
    public Iterator<WebSitemapUrl> provide() {
        Iterator<NamedSearchQuery> joined = Iterators.concat(
                anywherePermutation.get(),
                locationPermutation.get()
        );

        return Iterators.transform(joined, input -> {
            sleep();
            Objects.requireNonNull(input);
            return build("/search/" + input.getName(), 0.5, ChangeFreq.MONTHLY);
        });
    }

    private void sleep() {
        try {
            Thread.sleep(300);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
