package app.munch.elastic;

import com.google.common.io.Resources;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * This class is used to assist in the creation of elastic index.
 * For elastic v7.1, hosted on AWS.
 *
 * @author Fuxing Loh
 * @since 2017-03-10 at 23:22
 */
@Singleton
public final class ElasticMapping {
    private static final Logger logger = LoggerFactory.getLogger(ElasticMapping.class);

    /**
     * Current index name.
     * for v7.1
     */
    public static final String INDEX_NAME = "munch";

    private final RestHighLevelClient client;

    @Inject
    public ElasticMapping(RestHighLevelClient client) {
        this.client = client;
    }

    /**
     * @return result as JsonNode
     * @throws RuntimeException if failed to create or validate
     */
    public CreateIndexResponse create() throws RuntimeException, IOException {
        String json = getIndexJson();
        logger.info("Creating index: {}", json);

        CreateIndexRequest request = new CreateIndexRequest(INDEX_NAME);
        request.source(json, XContentType.JSON);

        return client.indices().create(request, RequestOptions.DEFAULT);
    }

    /**
     * Note: dynamic: false because only those present are indexed, the rest are ignored but still stored.
     *
     * @return elastic index in json
     * @throws IOException internal read error
     */
    @SuppressWarnings("UnstableApiUsage")
    private static String getIndexJson() throws IOException {
        URL url = Resources.getResource("index.json");
        return Resources.toString(url, StandardCharsets.UTF_8);
    }
}
