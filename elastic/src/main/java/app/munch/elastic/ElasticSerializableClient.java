package app.munch.elastic;

import app.munch.elastic.serializer.PlaceSerializer;
import app.munch.model.ElasticDocument;
import app.munch.model.ElasticSerializable;
import app.munch.model.Place;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 9/9/19
 * Time: 10:06 pm
 */
@Singleton
public final class ElasticSerializableClient {

    private final PlaceSerializer placeSerializer;
    private final ElasticDocumentClient documentClient;

    @Inject
    ElasticSerializableClient(PlaceSerializer placeSerializer, ElasticDocumentClient documentClient) {
        this.placeSerializer = placeSerializer;
        this.documentClient = documentClient;
    }

    public void put(ElasticSerializable serializable) {
        ElasticDocument document = serialize(serializable);
        documentClient.put(document);
    }

    private ElasticDocument serialize(ElasticSerializable serializable) {
        if (serializable instanceof Place) {
            return placeSerializer.serialize((Place) serializable);
        }

        throw new IllegalStateException("No serializer found for: " + serializable.getClass().getSimpleName());
    }
}
