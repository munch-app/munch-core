package com.munch.accounts.service;

import spark.Filter;

/**
 * Created By: Fuxing Loh
 * Date: 5/2/2017
 * Time: 3:44 PM
 * Project: munch-core
 */
public interface Service {

    Filter before = (request, response) -> {
        // TODO token validation
    };

    default Filter validateToken() {
        return before;
    }
}
