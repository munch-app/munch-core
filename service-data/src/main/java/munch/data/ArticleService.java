package munch.data;

import com.google.inject.Singleton;
import munch.data.database.ArticleEntity;
import munch.restful.server.JsonCall;

import javax.inject.Inject;
import java.util.List;
import java.util.function.Function;

/**
 * Created By: Fuxing Loh
 * Date: 17/4/2017
 * Time: 4:13 PM
 * Project: munch-core
 */
@Singleton
public final class ArticleService extends AbstractService<ArticleEntity> {

    @Inject
    public ArticleService() {
        super("/articles", ArticleEntity.class);
    }

    @Override
    public void route() {
        super.route();
        GET("/articles/list", this::list);
    }

    @Override
    protected Function<ArticleEntity, String> getKeyMapper() {
        return ArticleEntity::getArticleId;
    }

    @Override
    protected List<ArticleEntity> getList(List<String> keys) {
        return provider.reduce(em -> em.createQuery(
                "FROM ArticleEntity WHERE articleId IN (:keys)", ArticleEntity.class)
                .setParameter("keys", keys)
                .getResultList());
    }

    private List<ArticleEntity> list(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return provider.reduce(em -> em.createQuery("FROM ArticleEntity WHERE " +
                "placeId = :placeId ORDER BY createdDate desc", ArticleEntity.class)
                .setParameter("placeId", placeId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList());
    }
}
