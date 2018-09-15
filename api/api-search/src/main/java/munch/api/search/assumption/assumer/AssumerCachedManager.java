package munch.api.search.assumption.assumer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:37 AM
 * Project: munch-core
 */
@Singleton
public final class AssumerCachedManager extends AssumerManager {

    private final LoadingCache<String, Map<String, Assumption>> cache = CacheBuilder.newBuilder()
            .maximumSize(2)
            .build(new CacheLoader<>() {
                public Map<String, Assumption> load(String key) {
                    return AssumerCachedManager.super.get();
                }
            });

    @Inject
    public AssumerCachedManager(LocationAssumer locationAssumer, TimingAssumer timingAssumer, TagAssumer tagAssumer) {
        super(locationAssumer, timingAssumer, tagAssumer);

        cache.refresh("cache");
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            cache.refresh("cache");
        }, 8, 8, TimeUnit.HOURS);
    }

    @Override
    public Map<String, Assumption> get() {
        return cache.getUnchecked("cache");
    }
}
