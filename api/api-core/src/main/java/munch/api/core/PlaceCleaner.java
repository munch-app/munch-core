package munch.api.core;

import munch.api.ObjectCleaner;
import munch.data.location.Landmark;
import munch.data.location.Location;
import munch.data.place.Place;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 19/10/18
 * Time: 6:38 PM
 * Project: munch-core
 */
@Singleton
@Deprecated
public final class PlaceCleaner extends ObjectCleaner<Place> {

    private final LandmarkCleaner landmarkCleaner;
    private final AreaCleaner areaCleaner;

    @Inject
    public PlaceCleaner(LandmarkCleaner landmarkCleaner, AreaCleaner areaCleaner) {
        this.landmarkCleaner = landmarkCleaner;
        this.areaCleaner = areaCleaner;
    }

    @Override
    protected Class<Place> getClazz() {
        return Place.class;
    }

    @Override
    public void clean(Place place) {
        place.setNames(null);
        place.setUpdatedMillis(null);
        place.setCreatedMillis(null);

        @NotNull @Valid Location location = place.getLocation();
        List<Landmark> landmarks = location.getLandmarks();
        if (landmarks != null) landmarks.forEach(landmarkCleaner::clean);

        place.getAreas().forEach(areaCleaner::clean);
    }
}
