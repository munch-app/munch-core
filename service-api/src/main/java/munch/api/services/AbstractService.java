package munch.api.services;

import munch.restful.server.JsonService;
import spark.ResponseTransformer;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class AbstractService implements JsonService {

    private ObjectWhitelist objectWhitelist;

    @Inject
    void injectWhitelist(ObjectWhitelist whitelist) {
        this.objectWhitelist = whitelist;
    }

    @Override
    public ResponseTransformer toJson() {
        return model -> {
            model = objectWhitelist.purge(model);
            return toJson.render(model);
        };
    }
}
