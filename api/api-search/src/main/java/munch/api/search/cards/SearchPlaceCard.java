package munch.api.search.cards;


import munch.api.ObjectCleaner;
import munch.api.core.PlaceCleaner;
import munch.data.place.Place;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 13/9/17
 * Time: 2:57 PM
 * Project: munch-core
 */
public final class SearchPlaceCard implements SearchCard {
    private final Place place;

    public SearchPlaceCard(Place place) {
        this.place = place;
    }

    @Override
    public String getCardId() {
        return "Place_2018-12-29";
    }

    @Override
    public String getUniqueId() {
        return place.getPlaceId();
    }

    public Place getPlace() {
        return place;
    }

    @Singleton
    public static class Cleaner extends ObjectCleaner<SearchPlaceCard> {

        private final PlaceCleaner placeCleaner;

        @Inject
        public Cleaner(PlaceCleaner placeCleaner) {
            this.placeCleaner = placeCleaner;
        }

        @Override
        protected Class<SearchPlaceCard> getClazz() {
            return SearchPlaceCard.class;
        }

        @Override
        protected void clean(SearchPlaceCard data) {
            placeCleaner.clean(data.getPlace());
        }
    }
}
