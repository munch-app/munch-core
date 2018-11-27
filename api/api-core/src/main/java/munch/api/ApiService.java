package munch.api;

import munch.restful.server.JsonService;
import munch.restful.server.JsonTransformer;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 5:04 PM
 * Project: munch-core
 */
public abstract class ApiService implements JsonService {

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

}
