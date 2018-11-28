package munch.api.search.assumption;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import munch.api.search.assumption.plugin.LocationAssumePlugin;
import munch.api.search.assumption.plugin.TagAssumePlugin;
import munch.api.search.assumption.plugin.TimingAssumePlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:37 AM
 * Project: munch-core
 */
@Singleton
public final class AssumeCachedManager extends AssumeManager {

    private final Supplier<Map<String, Assumption>> supplier;

    @Inject
    public AssumeCachedManager(LocationAssumePlugin locationAssumer, TimingAssumePlugin timingAssumer, TagAssumePlugin tagAssumePlugin) {
        super(locationAssumer, timingAssumer, tagAssumePlugin);
        this.supplier = Suppliers.memoizeWithExpiration(AssumeCachedManager.super::get,
                8, TimeUnit.HOURS);
    }

    @Override
    public Map<String, Assumption> get() {
        return supplier.get();
    }
}
