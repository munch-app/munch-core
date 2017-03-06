package com.munch.core.api.service;

import com.munch.utils.spark.JsonService;
import spark.Spark;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
public class DiscoverService implements JsonService {

    @Override
    public void route() {
        Spark.path("/v1/places", () -> {
            Spark.post("/discover", APP_JSON, (request, response) -> {
                return null;
            }, toJson);
        });

        // TODO develop a structured communication
    }

}
