package app.munch.api;

import app.munch.elastic.ElasticQueryClient;
import app.munch.elastic.ElasticSerializableClient;
import app.munch.manager.PlaceEntityManager;
import app.munch.model.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.HibernateUtils;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;
import dev.fuxing.utils.JsonUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;

import javax.inject.Inject;
import javax.persistence.Tuple;
import java.util.*;

/**
 * Place service is not grouped into api-data because there are too many complex operations that are coupled with multiple services.
 * <p>
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 2:42 pm
 */
public final class PlaceService implements TransportService {

    private final ElasticQueryClient queryClient;
    private final ElasticSerializableClient serializableClient;
    private final PlaceEntityManager placeEntityManager;

    private final TransactionProvider provider;

    @Inject
    PlaceService(ElasticQueryClient queryClient, ElasticSerializableClient serializableClient, PlaceEntityManager placeEntityManager, TransactionProvider provider) {
        this.queryClient = queryClient;
        this.serializableClient = serializableClient;
        this.placeEntityManager = placeEntityManager;
        this.provider = provider;
    }

    @Override
    public void route() {
        GET("/places-cid/:cid", this::cidGet);

        PATH("/places", () -> {
            GET("/suggest", this::suggest);
            GET("/search", this::search);

            PATH("/:id", () -> {
                GET("", this::idGet);
                POST("/revisions", this::idRevisionPost);
            });
        });
    }

    public Map cidGet(TransportContext ctx) {
        String cid = ctx.pathString("cid");
        return provider.reduce(true, entityManager -> {
            Object[] objects = entityManager.createQuery("SELECT id, slug FROM Place " +
                    "WHERE cid = :cid", Object[].class)
                    .setParameter("cid", cid)
                    .getSingleResult();

            return Map.of("id", objects[0], "slug", objects[1]);
        });
    }

    public List<String> suggest(TransportContext ctx) {
        int size = ctx.querySize(20, 30);
        String text = ctx.queryString("text");

        String name = "suggest-places";
        ElasticQueryClient.Response response = queryClient.search(builder -> {
            SuggestionBuilder termSuggestionBuilder = SuggestBuilders
                    .completionSuggestion("suggest")
                    .prefix(text)
                    .size(size)
                    .contexts(Map.of("type", List.of(CategoryQueryContext.builder()
                            .setCategory(ElasticDocumentType.PLACE.toString())
                            .build())
                    ));

            builder.size(0);
            builder.suggest(new SuggestBuilder()
                    .addSuggestion(name, termSuggestionBuilder)
            );
        });

        return response.getSuggest(name);
    }

    public List<ElasticDocument> search(TransportContext ctx) {
        int from = ctx.queryInt("from", 0);
        int size = ctx.querySize(20, 30);
        String text = ctx.queryString("text");
        String fields = ctx.queryString("fields");

        if (from > 100) {
            return List.of();
        }

        ElasticQueryClient.Response response = queryClient.search(builder -> {
            builder.fetchSource(fields.split(", *"), null);
            builder.from(from);
            builder.size(size);
            builder.query(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("name", text))
                    .filter(QueryBuilders.termQuery("type", ElasticDocumentType.PLACE.toString()))
            );
        });

        return response.getDocuments();
    }

    public TransportResult idGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        Set<String> fields = Set.of(ctx.queryString("fields", "").split(", *"));

        return provider.reduce(true, entityManager -> {
            Place place = entityManager.find(Place.class, id);
            HibernateUtils.initialize(place.getImage());
            HibernateUtils.initialize(place.getCreatedBy());
            ObjectNode node = JsonUtils.valueToTree(place);

            Map<String, String> cursor = new HashMap<>();

            if (fields.contains("affiliates")) {
                EntityStream.of(() -> {
                    return entityManager.createQuery("FROM PlaceAffiliate " +
                            "WHERE place = :place " +
                            "ORDER BY uid DESC", PlaceAffiliate.class)
                            .setParameter("place", place)
                            .setMaxResults(20)
                            .getResultList();
                }).cursor(20, (placeAffiliate, builder) -> {
                    builder.put("uid", placeAffiliate.getUid());
                }).consume((affiliates, affiliatesCursor) -> {
                    node.set("affiliates", JsonUtils.valueToTree(affiliates));
                    if (affiliatesCursor != null) {
                        cursor.put("next.affiliates", affiliatesCursor.get("next"));
                    }
                });
            }

            if (fields.contains("images")) {
                EntityStream.of(() -> {
                    return entityManager.createQuery("SELECT pi.uid, pi.image FROM PlaceImage pi " +
                            "WHERE pi.place = :place " +
                            "ORDER BY uid DESC", Object[].class)
                            .setParameter("place", place)
                            .setMaxResults(8)
                            .getResultList();
                }).cursor(8, (objects, builder) -> {
                    builder.put("uid", objects[0]);
                }).map(objects -> {
                    return (PlaceImage) objects[1];
                }).consume((images, imagesCursor) -> {
                    node.set("images", JsonUtils.valueToTree(images));
                    if (imagesCursor != null) {
                        cursor.put("next.images", imagesCursor.get("next"));
                    }
                });
            }

            if (fields.contains("articles")) {
                EntityStream.of(() -> {
                    return entityManager.createQuery("SELECT " +
                            "ap.article.id AS a_id," +
                            "ap.article.slug AS a_slug," +
                            "ap.article.title AS a_title," +
                            "ap.article.description AS a_description," +
                            "ap.article.image AS a_image," +
                            "ap.article.profile AS a_profile," +
                            "ap.article.publishedAt AS a_publishedAt," +

                            "ap.position AS c_uid, " +
                            "ap.uid AS c_position " +
                            "FROM ArticlePlace ap " +
                            "WHERE ap.place = :place " +
                            "ORDER BY ap.position DESC, ap.uid DESC", Tuple.class)
                            .setParameter("place", place)
                            .setMaxResults(8)
                            .getResultList();
                }).cursor(8, (tuple, builder) -> {
                    builder.put("uid", tuple.get("c_uid", String.class));
                    builder.put("position", tuple.get("c_position", Long.class));
                }).map(tuple -> {
                    Article article = new Article();
                    article.setId(tuple.get("a_id", String.class));
                    article.setSlug(tuple.get("a_slug", String.class));
                    article.setTitle(tuple.get("a_title", String.class));
                    article.setDescription(tuple.get("a_description", String.class));
                    article.setImage(tuple.get("a_image", Image.class));
                    article.setProfile(tuple.get("a_profile", Profile.class));
                    article.setPublishedAt(tuple.get("a_publishedAt", Date.class));
                    return article;
                }).consume((articles, articleCursor) -> {
                    node.set("articles", JsonUtils.valueToTree(articles));
                    if (articleCursor != null) {
                        cursor.put("next.articles", articleCursor.get("next"));
                    }
                });
            }

            return TransportResult.ok(node)
                    .put("cursor", cursor);
        });
    }

    public Place idRevisionPost(TransportContext ctx) {
        String id = ctx.pathString("id");
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        PlaceModel model = ctx.bodyAsObject(PlaceModel.class);

        Place place = placeEntityManager.update(id, model, PlaceEditableField.ALL, entityManager -> {
            return entityManager.createQuery("SELECT a.profile FROM Account a " +
                    "WHERE a.id = :id", Profile.class)
                    .setParameter("id", accountId)
                    .getSingleResult();
        });
        serializableClient.put(place);
        return place;
    }
}
