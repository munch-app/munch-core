package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticSerializable;

/**
 * Created by: Fuxing
 * Date: 10/9/19
 * Time: 4:23 pm
 */
public interface Serializer<T extends ElasticSerializable> {

    /**
     * @param data to serialize into document
     * @return serialized document
     */
    ElasticDocument serialize(T data);

}
