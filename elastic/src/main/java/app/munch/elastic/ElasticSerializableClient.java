package app.munch.elastic;

import app.munch.elastic.serializer.DeleteException;
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
        try {
            ElasticDocument document = serialize(serializable);
            ElasticIndex.getIndexes(document.getElasticType()).forEach(index -> {
                documentClient.put(index, document);
            });
        } catch (DeleteException e) {
            ElasticIndex.getIndexes(e.getType()).forEach(index -> {
                documentClient.delete(index, e.getKey());
            });
        }
    }

    @SuppressWarnings("unchecked")
    private ElasticDocument serialize(ElasticSerializable serializable) throws DeleteException {
        Serializer serializer = serializers.get(serializable.getElasticDocumentType());
        if (serializer != null) {
            return serializer.serialize(serializable);
        }

        throw new IllegalStateException("No serializer found for: " + serializable.getClass().getSimpleName());
    }
}
