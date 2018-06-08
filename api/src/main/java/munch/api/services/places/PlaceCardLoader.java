package munch.api.services.places;

import munch.api.services.places.loader.*;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Improvement Timeline
 * 0. Move all other cards from PlaceCardClient to use this Loader, slowly
 * 1. Mem Caching Support
 * 2. DynamoDB Caching Support
 * 3. Redis/DAX or Similar Caching Support
 * 4. Test case to check if loaded card is not required by sorter
 * <p>
 * Created by: Fuxing
 * Date: 5/3/2018
 * Time: 1:35 AM
 * Project: munch-core
 */
@Singleton
public final class PlaceCardLoader {
    private final List<PlaceDataCardLoader<?>> dataCardLoaders;

    @Inject
    public PlaceCardLoader(PlaceMenuCardLoader placeMenuCardLoader,
                           PlaceAwardCardLoader placeAwardCardLoader,

                           PlaceArticleCardLoader placeArticleCardLoader,
                           PlaceInstagramCardLoader placeInstagramCardLoader) {

        this.dataCardLoaders = List.of(
                placeMenuCardLoader,
                placeAwardCardLoader,
                placeArticleCardLoader,
                placeInstagramCardLoader
        );
    }

    public List<PlaceCard> load(Place place) {
        List<PlaceCard> cards = new ArrayList<>();
        for (PlaceDataCardLoader<?> loader : dataCardLoaders) {
            cards.addAll(loader.load(place));
        }

        return cards;
    }
}
