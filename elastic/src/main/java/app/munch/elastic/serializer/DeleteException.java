package app.munch.elastic.serializer;

import app.munch.model.ElasticDocument;
import app.munch.model.ElasticDocumentType;

/**
 * @author Fuxing Loh
 * @since 2019-11-30 at 03:03
 */
public final class DeleteException extends Exception {
    private final ElasticDocumentType type;
    private final String key;

    public DeleteException(ElasticDocument document) {
        this(document.getElasticType(), document.getElasticKey());
    }

    public DeleteException(ElasticDocumentType type, String key) {
        this.type = type;
        this.key = key;
    }

    public ElasticDocumentType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }
}
