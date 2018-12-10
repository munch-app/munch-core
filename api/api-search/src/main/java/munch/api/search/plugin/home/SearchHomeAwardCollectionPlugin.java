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
                loadingCache.getUnchecked("0501cdbd-cd50-43cd-a2cc-de6ff53fda51"),
                loadingCache.getUnchecked("d559afb0-9207-40ec-9455-15a9556210f6"),
                loadingCache.getUnchecked("e021df92-5a9e-4f7a-9da2-b58036f0f795"),
                loadingCache.getUnchecked("2e67856c-a50b-4e39-9945-d7668c9161ca"),
                loadingCache.getUnchecked("8ae830ab-9885-4359-b3f7-60973c7c8152")
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
