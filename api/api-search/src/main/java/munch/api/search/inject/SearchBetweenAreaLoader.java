package munch.api.search.inject;

import munch.api.search.cards.SearchBetweenAreaCard;
import munch.api.search.cards.SearchCard;
import munch.api.search.cards.SearchPlaceCard;
import munch.data.location.Area;
import munch.data.place.Place;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 25/9/18
 * Time: 2:12 PM
 * Project: munch-core
 */
@Singleton
public final class SearchBetweenAreaLoader implements SearchCardInjector.Loader {

    @Override
    public List<Position> load(Request request) {
        if (!request.isBetween()) return List.of();
        if (request.getAreas().size() <= request.getPage()) return List.of();

        Area area = request.getAreas().get(request.getPage());

        SearchBetweenAreaCard card = new SearchBetweenAreaCard();
        card.setArea(area);
        card.setIndex(request.getPage());
        card.setPlaces(getPlaces(request));

        request.clearCards();
        return of(-1, card);
    }

    private List<Place> getPlaces(Request request) {
        List<Place> places = new ArrayList<>();
        for (SearchCard card : request.getCards()) {
            if (card instanceof SearchPlaceCard) {
                places.add(((SearchPlaceCard) card).getPlace());
            }
        }
        return places;
    }
}
