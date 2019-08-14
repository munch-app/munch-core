package app.munch.api;

import app.munch.model.ManagedPageBlock;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 14:34
 */
public final class ManagedPageAdminService extends AdminService {

    @Inject
    ManagedPageAdminService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/admin/managed/pages", () -> {
            PATH("/:slug/blocks", () -> {
                GET("", this::list);
                POST("", this::post);

                PATH("/:id", () -> {
                    GET("", this::get);
                    PATCH("", this::patch);
                });
            });
        });
    }

    public TransportList list(TransportContext ctx) {
        String slug = ctx.pathString("slug");
        int size = ctx.querySize(20, 100);

        @NotNull TransportCursor cursor = ctx.queryCursor();
        Long position = cursor.getLong("position");
        String cursorId = cursor.get("id");

        return provider.reduce(true, entityManager -> EntityStream.of(() -> {
            if (position != null && cursorId != null) {
                return entityManager.createQuery("FROM ManagedPageBlock " +
                        "WHERE slug = :slug " +
                        "AND (position < :position OR (position = :position AND id < :id)) " +
                        "ORDER BY position DESC, id DESC ", ManagedPageBlock.class)
                        .setParameter("slug", slug)
                        .setParameter("position", position)
                        .setParameter("id", cursorId)
                        .setMaxResults(size)
                        .getResultList();
            }

            return entityManager.createQuery("FROM ManagedPageBlock " +
                    "WHERE slug = :slug " +
                    "ORDER BY position DESC, id DESC ", ManagedPageBlock.class)
                    .setParameter("slug", slug)
                    .setMaxResults(size)
                    .getResultList();
        }).cursor(size, (block, builder) -> {
            builder.put("position", block.getPosition());
            builder.put("id", block.getId());
        }).asTransportList());
    }

    public ManagedPageBlock post(TransportContext ctx) {
        String slug = ctx.pathString("slug");

        ManagedPageBlock block = ctx.bodyAsObject(ManagedPageBlock.class);
        block.setSlug(slug);

        return provider.reduce(true, entityManager -> {
            entityManager.persist(block);
            return block;
        });
    }

    public ManagedPageBlock get(TransportContext ctx) {
        String slug = ctx.pathString("slug");
        String id = ctx.pathString("id");

        return provider.reduce(true, entityManager -> {
            return entityManager.createQuery("FROM ManagedPageBlock " +
                    "WHERE slug = :slug AND id = :id", ManagedPageBlock.class)
                    .setParameter("id", id)
                    .setParameter("slug", slug)
                    .getSingleResult();
        });
    }

    public ManagedPageBlock patch(TransportContext ctx) {
        String slug = ctx.pathString("slug");
        String id = ctx.pathString("id");
        JsonNode body = ctx.bodyAsJson();

        return provider.reduce(true, entityManager -> {
            ManagedPageBlock block = entityManager.createQuery("FROM ManagedPageBlock " +
                    "WHERE slug = :slug AND id = :id", ManagedPageBlock.class)
                    .setParameter("id", id)
                    .setParameter("slug", slug)
                    .getSingleResult();

            return EntityPatch.with(entityManager, block, body)
                    .lock()
                    .patch("position", ManagedPageBlock::setPosition)
                    .patch("block", ManagedPageBlock::setBlock)
                    .persist();
        });
    }
}
