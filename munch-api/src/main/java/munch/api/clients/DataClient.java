package munch.api.clients;

import com.typesafe.config.Config;
import munch.data.Article;
import munch.data.InstagramMedia;
import munch.data.Place;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 5:06 PM
 * Project: munch-core
 */
@Singleton
public class DataClient extends RestfulClient {

    @Inject
    public DataClient(Config config) {
        super(config.getString("services.data.url"));
    }

    /**
     * @param key key of place
     * @return single Place
     */
    public Place get(String key) {
        return doGet("/places/{key}")
                .path("key", key)
                .asDataObject(Place.class);
    }

    /**
     * @param keys list of keys for place
     * @return list of Place
     */
    public List<Place> get(List<String> keys) {
        return doPost("/places/get")
                .body(keys)
                .asDataList(Place.class);
    }

    public List<InstagramMedia> getInstagramMedias(String placeId, int from, int size) {
        return doGet("/places/{placeId}/instagram/medias/list")
                .path("placeId", placeId)
                .queryString("from", from)
                .queryString("size", size)
                .asDataList(InstagramMedia.class);
    }

    public List<Article> getArticles(String placeId, int from, int size) {
        return doGet("/places/{placeId}/articles/list")
                .path("placeId", placeId)
                .queryString("from", from)
                .queryString("size", size)
                .asDataList(Article.class);
    }
}
