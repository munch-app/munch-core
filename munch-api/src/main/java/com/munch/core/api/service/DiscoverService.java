package com.munch.core.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import spark.Request;
import spark.Response;

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

    private JsonNode discover(Request request, Response response, JsonNode node) {
        return null;
    }
}
