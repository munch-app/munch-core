package munch.api.core;

import munch.api.ObjectCleaner;
import munch.data.location.Landmark;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 29/11/18
 * Time: 8:05 AM
 * Project: munch-core
 */
@Singleton
@Deprecated
public final class LandmarkCleaner extends ObjectCleaner<Landmark> {

    @Override
    protected Class<Landmark> getClazz() {
        return Landmark.class;
    }

    @Override
    protected void clean(Landmark data) {
        data.setCreatedMillis(null);
        data.setUpdatedMillis(null);
    }
}
