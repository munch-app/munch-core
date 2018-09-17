package munch.api.landing.collections;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.api.landing.LandingRequest;
import munch.api.user.UserPlaceCollectionService;
import munch.user.client.UserPlaceCollectionClient;
import munch.user.data.UserPlaceCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 17/9/18
 * Time: 3:04 PM
 * Project: munch-core
 */
@Singleton
public final class CollectionProvider {
    private static final Logger logger = LoggerFactory.getLogger(CollectionProvider.class);

    private final UserPlaceCollectionService userPlaceCollectionService;
    private final UserPlaceCollectionClient userPlaceCollectionClient;
    private final LoadingCache<String, UserPlaceCollection> loadingCache = CacheBuilder.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<>() {
                public UserPlaceCollection load(String collectionId) {
                    UserPlaceCollection collection = userPlaceCollectionClient.get(collectionId);
                    userPlaceCollectionService.resolveImage(collection);
                    return collection;
                }
            });

    @Inject
    public CollectionProvider(UserPlaceCollectionService userPlaceCollectionService, UserPlaceCollectionClient userPlaceCollectionClient) {
        this.userPlaceCollectionService = userPlaceCollectionService;
        this.userPlaceCollectionClient = userPlaceCollectionClient;
    }

    /**
     * @param request landing request
     * @return List of UserPlaceCollection relevant to the request
     */
    public List<UserPlaceCollection> get(LandingRequest request) {
        return compile("5219a732-1f42-4af4-a432-031e3397978b",
                "60cd9bef-7a5e-4ab4-b4ad-cd10c829a48f",
                "313d7952-3d45-4457-b20d-06ac7fd7e95e",
                "46bc1ae0-6df1-4bac-97e0-39e734ba8e28",
                "90a0899f-e4d3-4d6c-806a-2b9d8742a8c8",
                "181139be-d187-4d99-97d4-dee4a801f663",
                "482c7dd0-d2d3-4536-acba-ec419127f8a9",
                "37e68201-795c-44bc-8c74-05179a709cc5"
        );
    }

    /**
     * @param collectionIds list of id collection
     * @return List of UserPlaceCollection
     */
    public List<UserPlaceCollection> compile(String... collectionIds) {
        return Arrays.stream(collectionIds)
                .map(s -> {
                    try {
                        return loadingCache.get(s);
                    } catch (Exception e) {
                        logger.warn("Failed to load Collection {}", s, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
