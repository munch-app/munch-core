package munch.api;

import munch.restful.server.JsonService;
import munch.restful.server.JsonTransformer;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class ApiService implements JsonService {

    private WhitelistTransformer transformer;

    @Inject
    void injectWhitelist(WhitelistTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public JsonTransformer toJson() {
        return transformer;
    }

    @Singleton
    public static class WhitelistTransformer extends JsonTransformer {

        private final ObjectWhitelist objectWhitelist;

        @Inject
        public WhitelistTransformer(ObjectWhitelist objectWhitelist) {
            this.objectWhitelist = objectWhitelist;
        }

        @Override
        protected void clean(Object object) {
            objectWhitelist.purge(object);
        }
    }
}
