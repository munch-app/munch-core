package app.munch.api;

import app.munch.elastic.ElasticIndex;
import app.munch.elastic.ElasticQueryClient;
import app.munch.elastic.ElasticSerializableClient;
import app.munch.controller.PlaceController;
import app.munch.model.*;
import app.munch.query.MentionQuery;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportResult;
import dev.fuxing.transport.service.TransportService;
import dev.fuxing.utils.JsonUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Tuple;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Place service is not grouped into api-data because there are too many complex operations that are coupled with multiple services.
 * <p>
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 2:42 pm
 */
@Singleton
public final class PlaceService implements TransportService {
    private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    private final ElasticQueryClient queryClient;
    private final ElasticSerializableClient serializableClient;
    private final PlaceController placeController;

    private final TransactionProvider provider;
    private final MentionQuery mentionQuery;

    @Inject
    PlaceService(ElasticQueryClient queryClient, ElasticSerializableClient serializableClient, PlaceController placeController, TransactionProvider provider, MentionQuery mentionQuery) {
        this.queryClient = queryClient;
        this.serializableClient = serializableClient;
        this.placeController = placeController;
        this.provider = provider;
        this.mentionQuery = mentionQuery;
    }

    @Override
    public void route() {
        GET("/places-cid/:cid", this::cidGet);

        PATH("/places", () -> {
            GET("/suggest", this::suggest);
            GET("/search", this::search);
            GET("/affiliates", this::affiliates);
            POST("", this::post);

            PATH("/:id", () -> {
                GET("", this::idGet);
                POST("/revisions", this::idRevisionsPost);
                GET("/mentions", this::idMentionsQuery);
            });
        });
    }

    /**
     * Required for /places/:uuid -> /:slug-:id redirect
     *
     * @param ctx transport context
     * @return id and slug of the new Place
     */
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
        ElasticQueryClient.Response response = queryClient.search(ElasticIndex.PLACE, builder -> {
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

        ElasticQueryClient.Response response = queryClient.search(ElasticIndex.PLACE, builder -> {
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

    public Map<String, List<PlaceAffiliate>> affiliates(TransportContext ctx) {
        String[] ids = ctx.queryString("ids").split(", *");

        return provider.reduce(true, entityManager -> {
            List<PlaceAffiliate> affiliates = entityManager.createQuery("FROM PlaceAffiliate " +
                    "WHERE affiliate.place.id IN (:ids)", PlaceAffiliate.class)
                    .setParameter("ids", Arrays.asList(ids))
                    .getResultList();

            return affiliates.stream().collect(Collectors.groupingBy(o -> {
                return o.getPlace().getId();
            }));
        });
    }

    public TransportResult idGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        Set<String> fields = ctx.queryFields();

        return provider.reduce(true, entityManager -> {
            Place place = entityManager.find(Place.class, id);
            if (place == null) return null;

            TransportResult.Builder builder = TransportResult.builder();

            Hibernate.initialize(place.getImage());
            Hibernate.initialize(place.getCreatedBy());
            ObjectNode node = JsonUtils.valueToTree(place);

            if (fields.contains("affiliates")) {
                EntityStream.of(() -> {
                    return entityManager.createQuery("FROM PlaceAffiliate " +
                            "WHERE place = :place " +
                            "ORDER BY uid DESC", PlaceAffiliate.class)
                            .setParameter("place", place)
                            .setMaxResults(20)
                            .getResultList();
                }).cursor(20, (placeAffiliate, c) -> {
                    c.put("uid", placeAffiliate.getUid());
                }).consume((affiliates, affiliatesCursor) -> {
                    node.set("affiliates", JsonUtils.valueToTree(affiliates));

                    if (affiliatesCursor != null) {
                        builder.cursor("next.affiliates", affiliatesCursor.get("next"));
                    }
                });
            }

            if (fields.contains("images")) {
                // Deprecated this
                EntityStream.of(() -> {
                    return entityManager.createQuery("SELECT " +
                            "pi.uid AS uid, " +
                            "pi.image AS image " +
                            "FROM PlaceImage pi " +
                            "WHERE pi.place = :place " +
                            "ORDER BY pi.uid DESC", Tuple.class)
                            .setParameter("place", place)
                            .setMaxResults(8)
                            .getResultList();
                }).cursor(8, (tuple, c) -> {
                    c.put("uid", tuple.get("uid"));
                }).map(tuple -> {
                    return tuple.get("image", Image.class);
                }).consume((images, imagesCursor) -> {
                    node.set("images", JsonUtils.valueToTree(images));
                    if (imagesCursor != null) {
                        builder.cursor("next.images", imagesCursor.get("next"));
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
                }).cursor(8, (tuple, c) -> {
                    c.put("uid", tuple.get("c_uid", String.class));
                    c.put("position", tuple.get("c_position", Long.class));
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
                        builder.cursor("next.articles", articleCursor.get("next"));
                    }
                });
            }

            if (fields.contains("extra.mentions")) {
                TransportList mentions = mentionQuery.queryByPlace(id, TransportCursor.EMPTY);
                builder.extra("mentions", mentions);

                if (mentions.getCursorNext() != null) {
                    builder.cursor("next.mentions", mentions.getCursorNext());
                }
            }

            return builder
                    .data(node)
                    .build();
        });
    }

    public Place post(TransportContext ctx) {
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        // Have to use PlaceStruct or else reference chain will be picked up
        PlaceStruct struct = ctx.bodyAsObject(PlaceStruct.class);

        Place place = placeController.create(struct, PlaceEditableField.ALL, entityManager -> {
            return Profile.findByAccountId(entityManager, accountId);
        });
        serializableClient.put(place);
        return place;
    }

    public Place idRevisionsPost(TransportContext ctx) {
        String id = ctx.pathString("id");
        String accountId = ctx.get(ApiRequest.class).getAccountId();
        // Have to use PlaceStruct or else reference chain will be picked up
        PlaceStruct struct = ctx.bodyAsObject(PlaceStruct.class);

        Place place = placeController.update(id, struct, PlaceEditableField.ALL, entityManager -> {
            return Profile.findByAccountId(entityManager, accountId);
        });
        serializableClient.put(place);
        return place;
    }

    public TransportList idMentionsQuery(TransportContext ctx) {
        final String id = ctx.pathString("id");
        TransportCursor cursor = ctx.queryCursor();
        return mentionQuery.queryByPlace(id, cursor);
    }
}
