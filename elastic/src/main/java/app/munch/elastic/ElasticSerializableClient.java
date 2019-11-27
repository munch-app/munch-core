package app.munch.elastic;

import app.munch.elastic.serializer.Serializer;
import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;
import app.munch.model.ElasticSerializable;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 10:06 pm
 */
@Singleton
public final class ElasticSerializableClient {

    private final ElasticDocumentClient documentClient;
    private final Map<ElasticDocumentType, Serializer> serializers;

    @Inject
    ElasticSerializableClient(ElasticDocumentClient documentClient, Map<ElasticDocumentType, Serializer> serializers) {
        this.documentClient = documentClient;
        this.serializers = serializers;
    }

    public void put(ElasticSerializable serializable) {
        ElasticDocument document = serialize(serializable);
        documentClient.put(document);
    }

    @SuppressWarnings("unchecked")
    private ElasticDocument serialize(ElasticSerializable serializable) {
        Serializer serializer = serializers.get(serializable.getElasticDocumentType());
        if (serializer != null) {
            return serializer.serialize(serializable);
        }

        // TODO(fuxing): ability to delete data that already exist?
        // TODO(fuxing): can be done by throwing an exception

        throw new IllegalStateException("No serializer found for: " + serializable.getClass().getSimpleName());
    }
}
