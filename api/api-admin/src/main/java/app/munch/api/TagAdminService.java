package app.munch.api;

import app.munch.model.Tag;
import app.munch.model.TagType;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 14:34
 */
@Singleton
public final class TagAdminService extends AdminService {

    @Override
    public void route() {
        PATH("/admin/tags", () -> {
            GET("", this::list);
            POST("", this::post);

            PATH("/:id", () -> {
                GET("", this::get);
                PATCH("", this::patch);
            });
        });
    }

    public TransportList list(TransportContext ctx) {
        int size = ctx.querySize(100, 100);

        @NotNull TransportCursor cursor = ctx.queryCursor();
        String cursorId = cursor.get("id");

        return provider.reduce(true, entityManager -> EntityStream.of(() -> {
            if (cursorId != null) {
                return entityManager.createQuery("FROM Tag " +
                        "WHERe id < :id " +
                        "ORDER BY id DESC ", Tag.class)
                        .setParameter("id", cursorId)
                        .setMaxResults(size)
                        .getResultList();
            }

            return entityManager.createQuery("FROM Tag " +
                    "ORDER BY id DESC ", Tag.class)
                    .setMaxResults(size)
                    .getResultList();

        }).cursor(size, (tag, builder) -> {
            builder.put("id", tag.getId());
        }).asTransportList());
    }

    public Tag post(TransportContext ctx) {
        Tag tag = ctx.bodyAsObject(Tag.class);

        return provider.reduce(entityManager -> {
            entityManager.persist(tag);
            return tag;
        });
    }

    public Tag get(TransportContext ctx) {
        String id = ctx.pathString("id");
        return provider.reduce(true, entityManager -> {
            return entityManager.find(Tag.class, id);
        });
    }

    public Tag patch(TransportContext ctx) {
        String id = ctx.pathString("id");
        JsonNode body = ctx.bodyAsJson();

        return provider.reduce(entityManager -> {
            Tag tag = entityManager.find(Tag.class, id);

            return EntityPatch.with(entityManager, tag, body)
                    .lock()
                    .patch("name", Tag::setName)
                    .patch("type", TagType.class, Tag::setType)
                    .persist();
        });
    }
}
