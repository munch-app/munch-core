package munch.api.search.assumption.assumer;

import com.google.inject.ImplementedBy;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:37 AM
 * Project: munch-core
 */
@ImplementedBy(AssumerCachedManager.class)
public class AssumerManager {
    private final LocationAssumer locationAssumer;
    private final TimingAssumer timingAssumer;
    private final TagAssumer tagAssumer;

    @Inject
    public AssumerManager(LocationAssumer locationAssumer, TimingAssumer timingAssumer, TagAssumer tagAssumer) {
        this.locationAssumer = locationAssumer;
        this.timingAssumer = timingAssumer;
        this.tagAssumer = tagAssumer;
    }

    public Map<String, Assumption> get() {
        Map<String, Assumption> map = new HashMap<>();
        locationAssumer.get().forEach(assumption -> map.put(assumption.getToken(), assumption));
        timingAssumer.get().forEach(assumption -> map.put(assumption.getToken(), assumption));
        tagAssumer.get().forEach(assumption -> map.put(assumption.getToken(), assumption));
        return map;
    }
}
