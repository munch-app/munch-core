package munch.api.search.named;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.data.client.NamedQueryClient;
import munch.data.named.NamedQuery;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Objects;
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

    private final LoadingCache<String, NamedQuery> cache;

    @Inject
    public NamedDelegator(NamedQueryClient client) {
        cache = CacheBuilder.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(30, TimeUnit.DAYS)
                .build(CacheLoader.from(slug -> {
                    return client.get(slug,"2018-11-28");
                }));
    }

    /**
     * @param slug of named object
     * @return NamedQuery
     */
    @Nullable
    public NamedQuery delegate(String slug) {
        try {
            return cache.getUnchecked(slug);
        } catch (Exception e) {
            return null;
        }
    }
}
