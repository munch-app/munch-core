package munch.api.services;

import munch.api.ApiService;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 8/6/18
 * Time: 2:54 PM
 * Project: munch-core
 */
@Singleton
public final class UserPlaceCollectionService extends ApiService {

    @Override
    public void route() {
        PATH("/users/:userId/place/collections", () -> {

        });
    }

    // TODO FIRST
}