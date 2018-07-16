package munch.api.search.assumption;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.data.client.AreaClient;
import munch.data.client.TagClient;

import javax.inject.Inject;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 26/2/18
 * Time: 6:51 PM
 * Project: munch-data
 */
public class CachedAssumptionDatabase extends AssumptionDatabase {

    private final LoadingCache<String, Map<String, Assumption>> cache = CacheBuilder.newBuilder()
            .maximumSize(2)
            .build(new CacheLoader<>() {
                public Map<String, Assumption> load(String key) {
                    return CachedAssumptionDatabase.super.get();
                }
            });

    @Inject
    public CachedAssumptionDatabase(AreaClient areaClient, TagClient tagClient) {
        super(areaClient, tagClient);
        cache.refresh("cache");
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(() -> {
            cache.refresh("cache");
        }, 8, 8, TimeUnit.HOURS);
    }

    @Override
    public Map<String, Assumption> get() {
        try {
            return cache.get("cache");
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
