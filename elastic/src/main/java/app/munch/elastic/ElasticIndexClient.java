package app.munch.elastic;

import app.munch.elastic.err.ElasticException;
import app.munch.model.IndexObject;
import dev.fuxing.err.ValidationException;
import dev.fuxing.utils.JsonUtils;
import io.searchbox.client.JestClient;
import io.searchbox.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

/**
 * Created By: Fuxing Loh
 * Date: 9/3/2017
 * Time: 3:27 PM
 * Project: munch-core
 */
@Singleton
public final class ElasticIndexClient {
    private static final Logger logger = LoggerFactory.getLogger(ElasticIndexClient.class);

    private final JestClient client;

    /**
     * https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/java-docs-index.html
     *
     * @param client injected rest client
     */
    @Inject
    ElasticIndexClient(JestClient client) {
        this.client = client;
    }

    /**
     * @param object elastic object to persist
     * @throws ElasticException                 wrapped exception
     * @throws dev.fuxing.err.NotFoundException when object cannot be found
     */
    public void put(IndexObject object) throws ElasticException {
        ValidationException.validate(object);

        try {
            String json = JsonUtils.toString(object);
            DocumentResult result = client.execute(new Index.Builder(json)
                    .index(ElasticMapping.INDEX_NAME)
                    .type(ElasticMapping.TABLE_NAME)
                    .id(object.getKey())
                    .build());

            ElasticException.parse(result);
        } catch (IOException e) {
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
    public IndexObject get(String key) {
        try {
            Get get = new Get.Builder(ElasticMapping.INDEX_NAME, key)
                    .type(ElasticMapping.TABLE_NAME)
                    .build();
            DocumentResult result = client.execute(get);
            ElasticException.parse(result);

            return JsonUtils.toObject(result.getJsonString(), IndexObject.class);
        } catch (IOException e) {
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
            DocumentResult result = client.execute(new Delete.Builder(key)
                    .index(ElasticMapping.INDEX_NAME)
                    .type(ElasticMapping.TABLE_NAME)
                    .build());

            ElasticException.parse(result);
        } catch (IOException e) {
            throw ElasticException.map(e);
        }
    }
}
