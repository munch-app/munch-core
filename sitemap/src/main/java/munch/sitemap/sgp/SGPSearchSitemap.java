package munch.sitemap.sgp;

import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.ChangeFreq;
import com.redfin.sitemapgenerator.WebSitemapUrl;
import munch.api.search.data.NamedSearchQuery;
import munch.sitemap.SitemapProvider;

import javax.inject.Inject;
import java.util.Date;
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
    private final SGPTagLocationPermutation tagLocationPermutation;
    private final SGPTagPermutation tagPermutation;

    @Inject
    public SGPSearchSitemap(SGPAnywherePermutation anywherePermutation, SGPLocationPermutation locationPermutation, SGPTagLocationPermutation tagLocationPermutation, SGPTagPermutation tagPermutation) {
        this.anywherePermutation = anywherePermutation;
        this.locationPermutation = locationPermutation;
        this.tagLocationPermutation = tagLocationPermutation;
        this.tagPermutation = tagPermutation;
    }

    @Override
    public String name() {
        return "search-sgp";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() {
        Iterator<NamedSearchQuery> joined = Iterators.concat(
                anywherePermutation.get(),
                locationPermutation.get(),
                tagLocationPermutation.get(),
                tagPermutation.get()
        );

        return Iterators.transform(joined, query -> {
            sleep();
            Objects.requireNonNull(query);
            return build("/search/" + query.getName(), new Date(query.getUpdatedMillis()), 0.5, ChangeFreq.MONTHLY);
        });
    }

    private void sleep() {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
