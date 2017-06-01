package munch.catalyst.clients;

import catalyst.data.CatalystLink;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:37 AM
 * Project: munch-core
 */
@Singleton
public class GalleryClient extends RestfulClient {

    @Inject
    public GalleryClient(@Named("services") Config config) {
        super(config.getString("gallery.url"));
    }

    public void put(CatalystLink link, Date updatedDate) {
        // TODO restful
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        // TODO restful
    }

    public void delete(String catalystId) {
        // TODO delete all place with catalystId
    }

    public static class Graphic {
        private String placeId;

        // TODO data structure
    }
}
