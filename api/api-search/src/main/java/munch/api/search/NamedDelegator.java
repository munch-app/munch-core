package munch.api.search;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.api.search.data.NamedSearchQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 24/9/18
 * Time: 7:23 PM
 * Project: munch-core
 */
@Singleton
public final class NamedDelegator {
    private static final Logger logger = LoggerFactory.getLogger(NamedDelegator.class);

    private final LoadingCache<String, NamedSearchQuery> cache;

    @Inject
    public NamedDelegator(NamedQueryDatabase database) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(30, TimeUnit.DAYS)
                .build(CacheLoader.from(database::get));
    }

    /**
     * @param named to find
     * @return named SearchQuery or null
     */
    public NamedSearchQuery delegate(String named) {
        try {
            return cache.getUnchecked(named);
        } catch (Exception e) {
            return null;
        }
    }
}
