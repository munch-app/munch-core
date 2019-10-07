package app.munch.api;

import app.munch.model.Tag;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ParamException;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 19/8/19
 * Time: 4:11 am
 */
@Singleton
public final class TagService extends ApiService {

    @Override
    public void route() {
        PATH("/tags", () -> {
            POST("/search", this::search);

            PATH("/:id", () -> {
                GET("", this::get);
            });
        });
    }

    public List<Tag> search(TransportContext ctx) {
        final int size = ctx.querySize(10, 20);
        final JsonNode body = ctx.bodyAsJson();

        String name = body.path("name").asText(null);
        ParamException.requireNonBlank("name", name);

        return provider.reduce(true, entityManager -> {
            return entityManager.createQuery("SELECT t FROM Tag t " +
                    "WHERE LOWER(t.name) LIKE :name", Tag.class)
                    .setParameter("name", "%" + name.toLowerCase() + "%")
                    .setMaxResults(size)
                    .getResultList();
        });
    }

    public Tag get(TransportContext ctx) {
        String id = ctx.pathString("id");
        return provider.reduce(true, entityManager -> {
            return entityManager.find(Tag.class, id);
        });
    }
}
