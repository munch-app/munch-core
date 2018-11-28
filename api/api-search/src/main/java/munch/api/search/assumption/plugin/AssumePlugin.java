package munch.api.search.assumption.plugin;

import munch.api.search.assumption.Assumption;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 15/9/18
 * Time: 1:45 AM
 * Project: munch-core
 */
public abstract class AssumePlugin {

    public abstract List<Assumption> get();
}
