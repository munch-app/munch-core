package munch.api.user;

import munch.api.ApiService;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-12
 * Time: 00:58
 * Project: munch-core
 */
@Singleton
public final class UserRatedPlaceService extends ApiService {
    @Override
    public void route() {
        PATH("/users/ratings", () -> {

        });
    }
}
