package munch.api.core;

import munch.api.ObjectCleaner;
import munch.data.location.Area;

/**
 * Created by: Fuxing
 * Date: 14/6/18
 * Time: 12:10 AM
 * Project: munch-core
 */
public final class AreaCleaner extends ObjectCleaner<Area> {
    @Override
    protected Class<Area> getClazz() {
        return Area.class;
    }

    @Override
    protected void clean(Area data) {
        data.setLocationCondition(null);

        data.setCreatedMillis(null);
        data.setUpdatedMillis(null);
        data.setNames(null);
    }
}
