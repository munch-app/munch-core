package app.munch.api;

import app.munch.model.UrlRedirection;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ParamException;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by: Fuxing
 * Date: 21/8/19
 * Time: 12:21 am
 */
@Singleton
public final class UrlRedirectionService extends DataService {

    @Inject
    UrlRedirectionService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/url/redirection/find", () -> {
            POST("", this::post);
        });
    }

    public UrlRedirection post(TransportContext ctx) {
        JsonNode body = ctx.bodyAsJson();
        String pathFrom = body.path("pathFrom").asText(null);
        ParamException.requireNonBlank("pathFrom", pathFrom);

        return provider.reduce(entityManager -> {
            return entityManager.createQuery("FROM UrlRedirection " +
                    "WHERE pathFrom = :pathFrom", UrlRedirection.class)
                    .setParameter("pathFrom", pathFrom)
                    .getSingleResult();
        });
    }
}
