package app.munch.worker;

import app.munch.elastic.pubsub.IndexMessage;
import app.munch.model.*;
import dev.fuxing.err.NotFoundException;
import dev.fuxing.jpa.TransactionProvider;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Objects;

/**
 * @author Fuxing Loh
 * @since 2019-11-30 at 05:12
 */
@Singleton
public final class ElasticModelResolver {
    private final TransactionProvider provider;

    @Inject
    ElasticModelResolver(TransactionProvider provider) {
        this.provider = provider;
    }

    /**
     * @param message index message that contains type and keys
     * @return ElasticSerializable
     */
    @NotNull
    public ElasticSerializable resolve(IndexMessage message) {
        return resolve(message.getType(), message.getKeys());
    }

    /**
     * @param type of model to find
     * @param keys for finding model
     * @return ElasticSerializable
     * @throws IllegalStateException if ElasticDocumentType is not registered
     */
    @NotNull
    private ElasticSerializable resolve(ElasticDocumentType type, Map<String, String> keys) {
        switch (type) {
            case ARTICLE:
                return get(Article.class, keys.get("id"));

            case PLACE:
                return get(Place.class, keys.get("id"));

            case TAG:
                return get(Tag.class, keys.get("id"));

            case PUBLICATION:
                return get(Publication.class, keys.get("id"));

            case PROFILE:
                return get(Profile.class, keys.get("uid"));

            case LOCATION:
                return get(Location.class, keys.get("id"));

            case BRAND:
            case UNKNOWN_TO_SDK_VERSION:
            default:
                throw new NotFoundException("Not supported yet");
        }
    }

    private <T> T get(Class<T> clazz, String pk) {
        Objects.requireNonNull(pk);

        return provider.reduce(entityManager -> {
            return entityManager.find(clazz, pk);
        });
    }
}
