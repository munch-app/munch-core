package com.munch.core.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.munch.utils.spark.JsonCall;

/**
 * Created by: Fuxing
 * Date: 7/3/2017
 * Time: 3:18 AM
 * Project: munch-core
 */
public class DiscoverService extends MunchService {

    @Override
    public void route() {
        path("/v1/places", () -> {
            post("/discover", this::discover);
        });
    }

    private JsonNode discover(JsonCall call, JsonNode node) {
        return null;
    }
}
