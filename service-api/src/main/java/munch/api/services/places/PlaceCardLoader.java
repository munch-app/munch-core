package munch.api.services.places;

import munch.awards.PlaceAward;
import munch.awards.PlaceAwardClient;
import munch.data.structure.Place;
import munch.data.structure.PlaceCard;
import munch.data.structure.PlaceJsonCard;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collections;
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

    private final PlaceAwardClient placeAwardClient;

    @Inject
    public PlaceCardLoader(PlaceAwardClient placeAwardClient) {
        this.placeAwardClient = placeAwardClient;
    }

    public List<PlaceCard> load(Place place) {
        List<PlaceAward> placeAwardList = placeAwardClient.list(place.getId(), null, 10);
        if (placeAwardList.isEmpty()) return Collections.emptyList();

        return List.of(
                new PlaceAwardCard(placeAwardList)
        );
    }

    public class PlaceAwardCard extends PlaceJsonCard {
        public PlaceAwardCard(List<PlaceAward> placeAwardList) {
            super("extended_PlaceAward_20180305", placeAwardList);
        }
    }
}
