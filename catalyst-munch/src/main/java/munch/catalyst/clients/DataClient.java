package munch.catalyst.clients;

import munch.data.Article;
import munch.data.InstagramMedia;
import munch.data.Place;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Date;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 9:44 PM
 * Project: munch-core
 */
@Singleton
public class DataClient extends RestfulClient {

    /**
     * Look at data service package to api service settings
     */
    @Inject
    public DataClient(@Named("services.data.url") String url) {
        super(url);
    }

    public void put(Place place) {
        doPut("/places/{id}")
                .path("id", place.getId())
                .body(place)
                .asResponse()
                .hasCode(200);
    }

    public void deleteBefore(Date updatedDate) {
        doDelete("/places/before/{updatedDate}")
                .path("updatedDate", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }

    public InstagramMedia put(InstagramMedia media) {
        return doPut("/places/{placeId}/instagram/medias/{mediaId}")
                .path("placeId", media.getPlaceId())
                .path("mediaId", media.getMediaId())
                .body(media)
                .asResponse()
                .asDataObject(InstagramMedia.class);
    }

    public void deleteInstagram(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/instagram/medias/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }

    public Article put(Article article) {
        return doPost("/places/{placeId}/articles/put")
                .path("placeId", article.getPlaceId())
                .body(article)
                .asResponse()
                .asDataObject(Article.class);
    }

    public void deleteArticle(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/articles/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
