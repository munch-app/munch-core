package app.munch.elastic;

import app.munch.elastic.err.ElasticException;
import app.munch.model.ElasticDocument;
import dev.fuxing.utils.JsonUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.suggest.Suggest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 10:25 pm
 */
@Singleton
public final class ElasticQueryClient {

    private final RestHighLevelClient client;

    @Inject
    ElasticQueryClient(RestHighLevelClient client) {
        this.client = client;
    }

    public Response search(ElasticIndex index, Consumer<SearchSourceBuilder> consumer) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        consumer.accept(builder);
        return search(index, builder);
    }

    public Response search(ElasticIndex index, SearchSourceBuilder builder) {
        SearchRequest request = new SearchRequest(index.getValue());
        request.source(builder);

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            return new Response(response);
        } catch (IOException e) {
            throw ElasticException.map(e);
        }
    }

    public Long count(ElasticIndex index, Consumer<SearchSourceBuilder> consumer) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        consumer.accept(builder);
        return count(index, builder);
    }

    public Long count(ElasticIndex index, SearchSourceBuilder builder) {
        CountRequest request = new CountRequest(index.getValue());
        request.source(builder);

        try {
            CountResponse response = client.count(request, RequestOptions.DEFAULT);
            return response.getCount();
        } catch (IOException e) {
            throw ElasticException.map(e);
        }
    }

    public static class Response {
        private final SearchResponse response;

        public Response(SearchResponse response) {
            this.response = response;
        }

        public SearchResponse getRaw() {
            return response;
        }

        public List<ElasticDocument> getDocuments() {
            SearchHit[] hits = response.getHits().getHits();
            return Arrays.stream(hits)
                    .map(hit -> JsonUtils.toObject(hit.getSourceAsString(), ElasticDocument.class))
                    .collect(Collectors.toList());
        }

        public List<String> getSuggest(String name) {
            List<? extends Suggest.Suggestion.Entry.Option> options = response.getSuggest()
                    .getSuggestion(name)
                    .getEntries().get(0)
                    .getOptions();


            if (options.isEmpty()) return List.of();

            return options.stream()
                    .map(Suggest.Suggestion.Entry.Option::getText)
                    .map(Text::string)
                    .collect(Collectors.toList());
        }
    }
}
