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
public final class ArticleService extends AbstractService<Article, ArticleEntity> {

    @Inject
    public ArticleService() {
        super("/places/:placeId/articles", Article.class, ArticleEntity.class);
    }

    @Override
    public void route() {
        super.route();
        GET("/places/:placeId/articles/list", this::list);
    }

    @Override
    protected ArticleEntity newEntity(Article data, long cycleNo) {
        ArticleEntity entity = new ArticleEntity();
        entity.setCycleNo(cycleNo);
        entity.setPlaceId(data.getPlaceId());
        entity.setArticleId(data.getArticleId());
        entity.setCreatedDate(data.getCreatedDate().getTime());
        entity.setData(data);
        return entity;
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
        long maxCreatedDate = call.queryLong("maxCreatedDate", Long.MAX_VALUE);
        int size = call.queryInt("size");

        return provider.reduce(em -> em.createQuery("FROM ArticleEntity WHERE " +
                "placeId = :placeId AND createdDate < :maxCreatedDate ORDER BY createdDate desc", ArticleEntity.class)
                .setParameter("placeId", placeId)
                .setParameter("maxCreatedDate", maxCreatedDate)
                .setMaxResults(size)
                .getResultList());
    }
}
