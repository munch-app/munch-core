package munch.api.services;

import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 3/6/18
 * Time: 3:37 PM
 * Project: munch-core
 */
@Singleton
public final class ObjectWhitelist {

    public <T> T purge(T object) {
        // TODO Object data to purge
        return object;
    }
}
