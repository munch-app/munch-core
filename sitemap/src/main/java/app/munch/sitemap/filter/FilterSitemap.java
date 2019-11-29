package app.munch.sitemap.filter;

import app.munch.sitemap.SitemapProvider;
import app.munch.sitemap.filter.permutation.Permutation;
import com.google.common.collect.Iterators;
import com.redfin.sitemapgenerator.WebSitemapUrl;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Fuxing Loh
 * @since 2019-11-30 at 03:23
 */
@Singleton
public final class FilterSitemap implements SitemapProvider {

    private final Set<Permutation> permutations;

    @Inject
    FilterSitemap(Set<Permutation> permutations) {
        this.permutations = permutations;
    }

    @Override
    public String name() {
        return "filter";
    }

    @Override
    public Iterator<WebSitemapUrl> provide() throws MalformedURLException {
        Iterator<Iterator<Object>> iterators =
                Iterators.transform(permutations.iterator(), Permutation::get);
        Iterator<Object> joined = Iterators.concat(iterators);

        return Iterators.transform(joined, query -> {
//            return build("/filter/" + query.getSlug(), new Date(query.getUpdatedMillis()), 0.5, ChangeFreq.WEEKLY);
            return null;
        });
    }
}
