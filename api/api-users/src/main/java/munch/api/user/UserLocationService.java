package munch.api.user;

import munch.api.ApiService;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 2019-02-12
 * Time: 00:59
 * Project: munch-core
 */
@Singleton
public final class UserLocationService extends ApiService {
    @Override
    public void route() {
        PATH("/users/locations", () -> {

        });
    }
}
