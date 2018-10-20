package munch.api.core;

import munch.api.ObjectCleaner;
import munch.data.place.Place;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 19/10/18
 * Time: 6:38 PM
 * Project: munch-core
 */
@Singleton
public final class PlaceCleaner extends ObjectCleaner<Place> {

    @Override
    protected Class<Place> getClazz() {
        return Place.class;
    }

    @Override
    protected void clean(Place place) {
        place.setNames(null);
    }
}
