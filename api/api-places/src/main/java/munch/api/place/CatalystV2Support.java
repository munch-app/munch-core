package munch.api.place;

import catalyst.link.PlaceLink;
import catalyst.link.PlaceLinkClient;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import munch.article.clients.Article;
import munch.article.clients.ArticleClient;
import munch.restful.core.NextNodeList;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

/**
 * Created by: Fuxing
 * Date: 16/8/18
 * Time: 1:14 AM
 * Project: munch-core
 */
@Singleton
public final class CatalystV2Support {

    private final LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .maximumSize(35000)
            .expireAfterWrite(2, TimeUnit.DAYS)
            .build(new CacheLoader<>() {
                public String load(String placeId) {
                    return CatalystV2Support.this.load(placeId);
                }
            });

    private final PlaceLinkClient linkClient;
    private final ArticleClient articleClient;

    @Inject
    public CatalystV2Support(PlaceLinkClient linkClient, ArticleClient articleClient) {
        this.linkClient = linkClient;
        this.articleClient = articleClient;
    }

    public NextNodeList<Article> getArticles(String placeId, @Nullable String nextPlaceSort, int size) {
        return articleClient.list(resolve(placeId), nextPlaceSort, size);
    }

    private String resolve(String placeId) {
        return cache.getUnchecked(placeId);
    }

    private String load(String placeId) {
        NextNodeList<PlaceLink> links = linkClient.listPlaceIdSource(placeId, "v2.catalyst.munch.space", null, 1);
        for (PlaceLink link : links) {
            if (link.getSource().equals("v2.catalyst.munch.space")) return link.getId();
        }
        return placeId;
    }
}
