package munch.api;

import munch.restful.server.JsonRoute;
import munch.restful.server.JsonService;
import munch.restful.server.JsonTransformer;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class ApiService implements JsonService<JsonRoute> {

    private CleanerTransformer transformer;

    /**
     * @param transformer whitelist transformer to clean data
     */
    @Inject
    void injectWhitelist(CleanerTransformer transformer) {
        this.transformer = transformer;
    }

    /**
     * A Cleaner Transformer cleans the data for transit
     * The purpose of a cleaner transformer is to reduce unwanted data that user will never need therefore
     * reducing bandwidth and increasing performance.
     * Note: This should not be used for security purpose
     *
     * @return Whitelist Json Transformer
     */
    @Override
    public JsonTransformer toJson() {
        return transformer;
    }

    public void GET(String path, ApiRoute route) {
        GET(path, (JsonRoute) route);
    }

    public void POST(String path, ApiRoute route) {
        POST(path, (JsonRoute) route);
    }

    public void POST(String path, String acceptType, ApiRoute route) {
        POST(path, acceptType, (JsonRoute) route);
    }

    public void PUT(String path, ApiRoute route) {
        PUT(path, (JsonRoute) route);
    }

    public void PUT(String path, String acceptType, ApiRoute route) {
        PUT(path, acceptType, (JsonRoute) route);
    }

    public void DELETE(String path, ApiRoute route) {
        DELETE(path, (JsonRoute) route);
    }

    public void HEAD(String path, ApiRoute route) {
        HEAD(path, (JsonRoute) route);
    }

    public void PATCH(String path, ApiRoute route) {
        PATCH(path, (JsonRoute) route);
    }
}
