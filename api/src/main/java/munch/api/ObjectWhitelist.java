package munch.api;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 3/6/18
 * Time: 3:37 PM
 * Project: munch-core
 */
@Singleton
public final class ObjectWhitelist {

    /**
     * @param object to purge
     */
    public void purge(Object object) {
        if (object instanceof Collection) {
            ((Collection<?>) object).forEach(this::purgeEach);
        } else if (object instanceof Map) {
            ((Map<?, ?>) object).forEach((k, v) -> purgeEach(v));
        } else {
            purgeEach(object);
        }
    }

    private void purgeEach(Object object) {
        // TODO MunchData, UserData to purge
    }
}
