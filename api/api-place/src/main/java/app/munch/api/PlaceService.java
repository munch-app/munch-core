package app.munch.api;

import app.munch.elastic.ElasticQueryClient;
import app.munch.elastic.ElasticSerializableClient;
import app.munch.manager.PlaceEntityManager;
import app.munch.model.*;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.transport.service.TransportService;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.SuggestionBuilder;
import org.elasticsearch.search.suggest.completion.context.CategoryQueryContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

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

    @Inject
    PlaceService(ElasticQueryClient queryClient, ElasticSerializableClient serializableClient, PlaceEntityManager placeEntityManager) {
        this.queryClient = queryClient;
        this.serializableClient = serializableClient;
        this.placeEntityManager = placeEntityManager;
    }

    @Override
    public void route() {
        PATH("/places", () -> {
            GET("/suggest", this::suggest);
            GET("/search", this::search);

            PATH("/:id", () -> {
                GET("", this::idGet);
                POST("/revisions", this::idRevisionPost);
            });
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

    public Place idGet(TransportContext ctx) {
        String id = ctx.pathString("id");
        return placeEntityManager.get(id);
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
