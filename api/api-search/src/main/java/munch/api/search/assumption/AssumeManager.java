package munch.api.search.assumption;

import com.google.inject.ImplementedBy;
import munch.api.search.assumption.plugin.LocationAssumePlugin;
import munch.api.search.assumption.plugin.TagAssumePlugin;
import munch.api.search.assumption.plugin.TimingAssumePlugin;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:37 AM
 * Project: munch-core
 */
@ImplementedBy(AssumeCachedManager.class)
public class AssumeManager {
    private final LocationAssumePlugin locationPlugin;
    private final TimingAssumePlugin timingPlugin;
    private final TagAssumePlugin tagAssumePlugin;

    @Inject
    public AssumeManager(LocationAssumePlugin locationPlugin, TimingAssumePlugin timingPlugin, TagAssumePlugin tagAssumePlugin) {
        this.locationPlugin = locationPlugin;
        this.timingPlugin = timingPlugin;
        this.tagAssumePlugin = tagAssumePlugin;
    }

    public Map<String, Assumption> get() {
        Map<String, Assumption> map = new HashMap<>();
        locationPlugin.get().forEach(assumption -> map.put(assumption.getToken(), assumption));
        timingPlugin.get().forEach(assumption -> map.put(assumption.getToken(), assumption));
        tagAssumePlugin.get().forEach(assumption -> map.put(assumption.getToken(), assumption));
        return map;
    }
}
