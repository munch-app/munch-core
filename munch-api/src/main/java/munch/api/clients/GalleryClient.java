package munch.api.clients;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.api.data.Media;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

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

    /**
     * Query Medias that belong to place id
     *
     * @param placeId place id
     * @param from    from
     * @param size    size
     * @return List of Media
     */
    public List<Media> list(String placeId, int from, int size) {
        return doGet("/places/{placeId}/gallery/list")
                .path("placeId", placeId)
                .queryString("from", from)
                .queryString("size", size)
                .asDataList(Media.class);
    }
}
