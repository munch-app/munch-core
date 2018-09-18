package munch.api;

import munch.restful.server.JsonTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 13/6/18
 * Time: 7:37 PM
 * Project: munch-core
 */
@Singleton
public final class CleanerTransformer extends JsonTransformer {

    private final Map<String, ObjectCleaner> cleanerMap;

    @Inject
    public CleanerTransformer(Set<ObjectCleaner> cleaners) {
        this.cleanerMap = cleaners.stream()
                .collect(Collectors.toMap(o -> o.getClazz().getName(), o -> o));
    }

    @Override
    protected void clean(Object object) {
        if (object instanceof Collection) {
            ((Collection<?>) object).forEach(this::cleanEach);
        } else if (object instanceof Map) {
            ((Map<?, ?>) object).forEach((k, v) -> {
                if (v instanceof Collection) {
                    ((Collection<?>) v).forEach(this::cleanEach);
                } else {
                    cleanEach(v);
                }
            });
        } else {
            cleanEach(object);
        }
    }

    @SuppressWarnings("unchecked")
    private void cleanEach(Object object) {
        String name = object.getClass().getSimpleName();
        ObjectCleaner cleaner = cleanerMap.get(name);
        if (cleaner == null) return;

        cleaner.clean(object);
    }
}