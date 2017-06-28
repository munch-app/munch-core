package munch.catalyst.clients;

import com.google.inject.Singleton;
import munch.catalyst.data.Article;
import munch.restful.client.RestfulClient;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 31/5/2017
 * Time: 5:36 AM
 * Project: munch-core
 */
@Singleton
public class ArticleClient extends RestfulClient {

    @Inject
    public ArticleClient(@Named("services.articles.url") String url) {
        super(url);
    }

    public Article put(Article article) {
        return doPost("/places/{placeId}/articles/put")
                .path("placeId", article.getPlaceId())
                .body(article)
                .asResponse()
                .asDataObject(Article.class);
    }

    public void deleteBefore(String catalystId, Date updatedDate) {
        doDelete("/places/{placeId}/articles/before/{timestamp}")
                .path("placeId", catalystId)
                .path("timestamp", updatedDate.getTime())
                .asResponse()
                .hasCode(200);
    }
}
