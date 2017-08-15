package munch.api.clients;

import com.google.inject.Singleton;
import com.typesafe.config.Config;
import munch.data.InstagramMedia;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:37 AM
 * Project: munch-core
 */
@Singleton
public class InstagramClient extends RestfulClient {

    @Inject
    public InstagramClient(Config config) {
        super(config.getString("services.instagram.url"));
    }

    /**
     * Query Medias that belong to place id
     *
     * @param placeId place id
     * @param from    from
     * @param size    size
     * @return List of Media
     */
    public List<InstagramMedia> list(String placeId, int from, int size) {
        return doGet("/places/{placeId}/instagram/medias/list")
                .path("placeId", placeId)
                .queryString("from", from)
                .queryString("size", size)
                .asDataList(InstagramMedia.class);
    }
}
