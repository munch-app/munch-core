package munch.articles;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.munch.hibernate.utils.TransactionProvider;
import munch.restful.server.JsonCall;
import munch.restful.server.JsonService;

import java.util.Date;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 18/4/2017
 * Time: 2:22 AM
 * Project: munch-core
 */
@Singleton
public final class ArticleService implements JsonService {

    private final PersistMapper mapper;
    private final TransactionProvider provider;

    @Inject
    public ArticleService(PersistMapper mapper, TransactionProvider provider) {
        this.mapper = mapper;
        this.provider = provider;
    }

    @Override
    public void route() {
        PATH("/places/:placeId/articles", () -> {
            GET("/list", this::list);
            POST("/get", this::get);

            // Management
            POST("/put", this::put);
            POST("/delete", this::delete);
            DELETE("/before/:timestamp", this::deleteBefore);
        });
    }

    private List<Article> list(JsonCall call) {
        String placeId = call.pathString("placeId");
        int from = call.queryInt("from");
        int size = call.queryInt("size");
        return provider.reduce(em -> em.createQuery("FROM Article WHERE placeId = :placeId", Article.class)
                .setParameter("placeId", placeId)
                .setFirstResult(from)
                .setMaxResults(size)
                .getResultList());
    }

    private Article get(JsonCall call, JsonNode request) {
        String articleId = request.get("articleId").asText();
        return provider.reduce(em -> em.find(Article.class, articleId));
    }

    private JsonNode put(JsonCall call) {
        Article article = call.bodyAsObject(Article.class);
        mapper.put(article);
        return Meta200;
    }

    private JsonNode delete(JsonCall call, JsonNode request) {
        String articleId = request.get("articleId").asText();
        Article article = provider.reduce(em -> em.find(Article.class, articleId));
        mapper.delete(article);
        return Meta200;
    }

    private JsonNode deleteBefore(JsonCall call) {
        String placeId = call.pathString("placeId");
        Date before = new Date(call.pathLong("timestamp"));
        mapper.deleteBefore(placeId, before);
        return Meta200;
    }
}
