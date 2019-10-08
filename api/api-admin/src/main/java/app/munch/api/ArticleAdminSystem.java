package app.munch.api;

import app.munch.model.Article;
import app.munch.model.ArticleStatus;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Singleton;
import java.util.Date;

/**
 * Created by: Fuxing
 * Date: 22/8/19
 * Time: 7:02 pm
 */
@Singleton
public final class ArticleAdminSystem extends AdminService {

    @Override
    public void route() {
        PATH("/admin/articles", () -> {
            GET("", this::list);
        });
    }

    public TransportList list(TransportContext ctx) {
        final int size = ctx.querySize(100, 200);
        final ArticleStatus status = ctx.queryEnum("status", ArticleStatus.class);

        TransportCursor cursor = ctx.queryCursor();
        String cursorId = cursor.get("id");
        Long cursorUpdatedAt = cursor.getLong("updatedAt");

        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (cursorId != null && cursorUpdatedAt != null) {
                    return entityManager.createQuery("FROM Article " +
                            "WHERE status = :status " +
                            "AND (updatedAt < :cursorUpdatedAt OR (updatedAt = :cursorUpdatedAt AND id < :cursorId)) " +
                            "ORDER BY updatedAt DESC, id DESC", Article.class)
                            .setParameter("status", status)
                            .setParameter("cursorUpdatedAt", new Date(cursorUpdatedAt))
                            .setParameter("cursorId", cursorId)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("FROM Article " +
                        "WHERE status = :status " +
                        "ORDER BY updatedAt DESC, id DESC", Article.class)
                        .setParameter("status", status)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (article, builder) -> {
                builder.put("id", article.getId());
                builder.put("updatedAt", article.getUpdatedAt().getTime());
            }).asTransportList();
        });
    }
}
