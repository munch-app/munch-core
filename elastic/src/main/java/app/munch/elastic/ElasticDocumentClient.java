package app.munch.elastic;

import app.munch.elastic.err.ElasticException;
import app.munch.model.ElasticDocument;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.JsonUtils;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:27 PM
 * Project: munch-core
 */
@Singleton
public final class ElasticDocumentClient {
    private final RestHighLevelClient client;

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
     *
     * @param client injected rest client
     */
    @Inject
    ElasticDocumentClient(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * @param object elastic object to persist
     * @throws ElasticException                 wrapped exception
     * @throws dev.fuxing.err.NotFoundException when object cannot be found
     */
    public void put(ElasticDocument object) throws ElasticException {
        ValidationException.validate(object);

        try {
            String source = JsonUtils.toString(object);
            IndexRequest request = new IndexRequest(ElasticMapping.INDEX_NAME);
            request.id(object.getKey());
            request.source(source, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            switch (response.getResult()) {
                case CREATED:
                case UPDATED:
                    return;

                case DELETED:
                case NOOP:
                case NOT_FOUND:
                default:
                    throw new ElasticException(response.getResult().toString());
            }
        } catch (Exception e) {
            throw ElasticException.map(e);
        }
    }

    /**
     * @param key in the IndexObject
     * @return data if exists
     * @throws ElasticException                 wrapped exception
     * @throws dev.fuxing.err.NotFoundException when object cannot be found
     */
    @Nullable
    public ElasticDocument get(String key) {
        try {
            GetRequest request = new GetRequest(ElasticMapping.INDEX_NAME, key);
            GetResponse response = client.get(request, RequestOptions.DEFAULT);

            if (response.isExists()) {
                byte[] bytes = response.getSourceAsBytes();
                return JsonUtils.toObject(bytes, ElasticDocument.class);
            }

            throw new NotFoundException();
        } catch (Exception e) {
            throw ElasticException.map(e);
        }
    }

    /**
     * @param key in the IndexObject
     * @throws ElasticException                 wrapped exception
     * @throws dev.fuxing.err.NotFoundException when object cannot be found
     */
    public void delete(String key) {
        try {
            DeleteRequest request = new DeleteRequest(ElasticMapping.INDEX_NAME, key);
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            switch (response.getResult()) {
                case DELETED:
                    return;

                case NOT_FOUND:
                    throw new NotFoundException();

                default:
                    throw new ElasticException(response.getResult().toString());
            }
        } catch (Exception e) {
            throw ElasticException.map(e);
        }
    }
}
