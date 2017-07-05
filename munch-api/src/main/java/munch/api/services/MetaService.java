package munch.api.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.server.JsonCall;

import java.util.List;

/**
 * Created by: Fuxing
 * Date: 23/4/2017
 * Time: 8:11 AM
 * Project: munch-core
 */
@Singleton
public class MetaService extends AbstractService {

    /**
     * Config inside meta only
     * <pre>
     * api{
     *     meta{
     *
     *     }
     * }
     * </pre>
     */
    private final Config metaConfig;

    @Inject
    public MetaService(Config config) {
        this.metaConfig = config.getConfig("meta");
    }

    @Override
    public void route() {
        // For use for alpha/beta channel testing
        PATH("/meta", () -> {
            GET("/builds", this::builds);
        });
    }

    /**
     * Get current supported builds
     *
     * @param call json call
     * @return list of supported builds for iOS
     */
    private List<String> builds(JsonCall call) {
        return metaConfig.getStringList("builds");
    }

}
