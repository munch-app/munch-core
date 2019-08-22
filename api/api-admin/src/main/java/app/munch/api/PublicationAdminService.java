package app.munch.api;

import app.munch.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import dev.fuxing.err.ConflictException;
import dev.fuxing.jpa.EntityPatch;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;
import dev.fuxing.utils.JsonUtils;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 14:34
 */
@Singleton
public final class PublicationAdminService extends AdminService {

    @Inject
    PublicationAdminService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/admin/publications", () -> {
            GET("", this::list);
            POST("", this::post);

            PATH("/:id", () -> {
                GET("", this::get);
                PATCH("", this::patch);

                PATH("/articles", () -> {
                    GET("", this::articleList);

                    PATH("/:articleId", () -> {
                        PUT("", this::articlePut);
                        PATCH("", this::articlePatch);
                        DELETE("", this::articleDelete);
                    });
                });
            });
        });
    }

    public TransportList list(TransportContext ctx) {
        int size = ctx.querySize(100, 100);

        TransportCursor cursor = ctx.queryCursor();
        String cursorId = cursor.get("id");

        return provider.reduce(true, entityManager -> EntityStream.of(() -> {
            if (cursorId != null) {
                return entityManager.createQuery("FROM Publication " +
                        "where id < :cursorId " +
                        "ORDER BY id DESC ", Publication.class)
                        .setParameter("cursorId", cursorId)
                        .setMaxResults(size)
                        .getResultList();
            }

            return entityManager.createQuery("FROM Publication " +
                    "ORDER BY id DESC", Publication.class)
                    .setMaxResults(size)
                    .getResultList();
        }).cursor(size, (pa, builder) -> {
            builder.put("id", pa.getId());
        }).asTransportList());
    }

    public Publication get(TransportContext ctx) {
        String id = ctx.pathString("id");

        return provider.reduce(true, entityManager -> {
            return entityManager.find(Publication.class, id);
        });
    }

    public Publication post(TransportContext ctx) {
        Publication publication = ctx.bodyAsObject(Publication.class);

        return provider.reduce(entityManager -> {
            Image.EntityUtils.map(entityManager, publication.getImage(), publication::setImage);

            entityManager.persist(publication);
            return publication;
        });
    }

    public Publication patch(TransportContext ctx) {
        String id = ctx.pathString("id");
        JsonNode body = ctx.bodyAsJson();
        return provider.reduce(entityManager -> {
            Publication publication = entityManager.find(Publication.class, id);
            entityManager.persist(publication);
            EntityPatch.with(entityManager, publication, body)
                    .lock()
                    .patch("name", Publication::setName)
                    .patch("description", Publication::setDescription)
                    .patch("body", Publication::setBody)
                    .patch("image", (EntityPatch.NodeConsumer<Publication>) (pub, json) -> {
                        Image.EntityUtils.map(entityManager, json, pub::setImage);
                    })
                    .patch("tags", (EntityPatch.NodeConsumer<Publication>) (pub, json) -> {
                        pub.setTags(JsonUtils.toSet(json, Tag.class));
                    })
                    .persist();
            return publication;
        });
    }

    public TransportList articleList(TransportContext ctx) {
        String publicationId = ctx.pathString("id");
        int size = ctx.querySize(100, 100);

        TransportCursor cursor = ctx.queryCursor();
        String uid = cursor.get("uid");
        Long position = cursor.getLong("position");

        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (position != null && uid != null) {
                    return entityManager.createQuery("FROM PublicationArticle " +
                            "WHERE publication.id = :id " +
                            "AND (position < :position OR (position = :position AND uid < :uid)) " +
                            "ORDER BY position DESC, uid DESC ", PublicationArticle.class)
                            .setParameter("id", publicationId)
                            .setParameter("position", position)
                            .setParameter("uid", uid)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM PublicationArticle " +
                        "WHERE publication.id = :id " +
                        "ORDER BY position DESC, uid DESC", PublicationArticle.class)
                        .setParameter("id", publicationId)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (pa, builder) -> {
                builder.put("uid", pa.getUid());
                builder.put("position", pa.getPosition());
            }).asTransportList();
        });
    }

    public PublicationArticle articlePut(TransportContext ctx) {
        String publicationId = ctx.pathString("id");
        String articleId = ctx.pathString("articleId");

        PublicationArticle article = ctx.bodyAsObject(PublicationArticle.class);
        return provider.reduce(entityManager -> {
            List<PublicationArticle> list = entityManager.createQuery("FROM PublicationArticle " +
                    "WHERE publication.id = :publicationId AND article.id = :articleId", PublicationArticle.class)
                    .setParameter("publicationId", publicationId)
                    .setParameter("articleId", articleId)
                    .getResultList();

            if (!list.isEmpty()) {
                throw new ConflictException("Article already inside publication.");
            }

            article.setPublication(entityManager.find(Publication.class, publicationId));
            article.setArticle(entityManager.find(Article.class, article.getArticle().getId()));
            entityManager.persist(article);
            return article;
        });
    }

    public PublicationArticle articlePatch(TransportContext ctx) {
        String publicationId = ctx.pathString("id");
        String articleId = ctx.pathString("articleId");
        JsonNode body = ctx.bodyAsJson();

        return provider.reduce(entityManager -> {
            PublicationArticle article = entityManager.createQuery("FROM PublicationArticle " +
                    "WHERE publication.id = :publicationId AND article.id = :articleId", PublicationArticle.class)
                    .setParameter("publicationId", publicationId)
                    .setParameter("articleId", articleId)
                    .getSingleResult();

            return EntityPatch.with(entityManager, article, body)
                    .patch("position", PublicationArticle::setPosition)
                    .persist();
        });
    }

    public PublicationArticle articleDelete(TransportContext ctx) {
        String publicationId = ctx.pathString("id");
        String articleId = ctx.pathString("articleId");

        return provider.reduce(entityManager -> {
            PublicationArticle article = entityManager.createQuery("FROM PublicationArticle " +
                    "WHERE publication.id = :publicationId AND article.id = :articleId", PublicationArticle.class)
                    .setParameter("publicationId", publicationId)
                    .setParameter("articleId", articleId)
                    .getSingleResult();

            entityManager.remove(article);
            return article;
        });
    }
}
