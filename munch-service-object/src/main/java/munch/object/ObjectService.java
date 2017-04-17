package munch.object;

import com.google.inject.Singleton;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:22 AM
 * Project: munch-core
 */
@Singleton
public class ObjectService implements JsonService {

    @Override
    public void route() {
        PATH("", () -> {
            // TODO what object are we storing here
        });
    }


}
