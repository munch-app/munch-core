package munch.api.search.plugin.home;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.api.search.SearchQuery;
import munch.api.search.cards.SearchHomeAwardCollectionCard;
import munch.api.search.plugin.SearchCardPlugin;
import munch.data.client.PlaceClient;
import munch.data.place.Place;
import munch.file.Image;
import munch.user.client.UserPlaceCollectionClient;
import munch.user.data.UserPlaceCollection;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 10/12/18
 * Time: 7:59 PM
 * Project: munch-core
 */
@Singleton
public final class SearchHomeAwardCollectionPlugin implements SearchCardPlugin {

    private final UserPlaceCollectionClient collectionClient;
    private final PlaceClient placeClient;
    private final LoadingCache<String, Optional<UserPlaceCollection>> loadingCache;

    @Inject
    public SearchHomeAwardCollectionPlugin(UserPlaceCollectionClient collectionClient, PlaceClient placeClient) {
        this.collectionClient = collectionClient;
        this.loadingCache = CacheBuilder.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build(CacheLoader.from(input -> {
                    UserPlaceCollection collection = collectionClient.get(input);
                    if (collection == null) return Optional.empty();

                    resolveImage(collection);
                    return Optional.of(collection);
                }));
        this.placeClient = placeClient;
    }

    @Nullable
    @Override
    public List<Position> load(Request request) {
        if (!request.isFirstPage()) return null;
        if (!request.getRequest().isFeature(SearchQuery.Feature.Home)) return null;

        List<UserPlaceCollection> collections = load().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        return of(-1, new SearchHomeAwardCollectionCard(collections));
    }

    private List<Optional<UserPlaceCollection>> load() {
        return List.of(
                // Bib Gourmand 2018
                loadingCache.getUnchecked("5219a732-1f42-4af4-a432-031e3397978b"),
                // One Michelin Star 2018
                loadingCache.getUnchecked("313d7952-3d45-4457-b20d-06ac7fd7e95e"),
                // Two Michelin Star 2018
                loadingCache.getUnchecked("60cd9bef-7a5e-4ab4-b4ad-cd10c829a48f"),
                // Asia's 50 Best Restaurants 2018
                loadingCache.getUnchecked("f6289dd9-f3fd-40f0-9977-1cef5ab30298"),
                // G Restaurant Awards 2018
                loadingCache.getUnchecked("46bc1ae0-6df1-4bac-97e0-39e734ba8e28"),
                // T.Dining Best Restaurants 2017/18
                loadingCache.getUnchecked("1280f846-ef05-42f6-a868-e13024790c6e"),
                // Best Asian Restaurant 2017
                loadingCache.getUnchecked("965a6aaf-39cf-47ec-9f41-52924478dd24"),
                // World Gourmet Summit Awards 2018
                loadingCache.getUnchecked("713f1cb5-7e9e-4a0d-a9e2-b0151d0bdb34"),
                // Best Asian Restaurants Gold Award 2018
                loadingCache.getUnchecked("0501cdbd-cd50-43cd-a2cc-de6ff53fda51"),
                // Best Asian Restaurants Silver Award 2018
                loadingCache.getUnchecked("9cfdab08-6f51-4f28-803f-5b92ee0f7e8b")
        );
    }

    /**
     * @param collection add image if don't exist
     */
    @SuppressWarnings("Duplicates")
    public void resolveImage(UserPlaceCollection collection) {
        if (collection == null) return;
        if (collection.getImage() != null) return;

        for (UserPlaceCollection.Item item : collectionClient.listItems(collection.getCollectionId(), null, 10)) {
            Place place = placeClient.get(item.getPlaceId());
            if (place == null) continue;

            List<Image> images = place.getImages();
            if (!images.isEmpty()) {
                collection.setImage(images.get(0));
                return;
            }
        }
    }
}
