package munch.gallery;

import com.google.inject.Singleton;
import munch.restful.server.JsonService;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 7:00 PM
 * Project: munch-core
 */
@Singleton
public class GalleryService implements JsonService {

    @Override
    public void route() {
        PATH("/gallery", () -> {

        });
    }

}
