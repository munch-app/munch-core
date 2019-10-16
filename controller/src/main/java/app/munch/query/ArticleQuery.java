package app.munch.query;

import app.munch.model.Article;
import app.munch.model.ArticleStatus;
import app.munch.model.Profile;
import dev.fuxing.err.ForbiddenException;
import dev.fuxing.jpa.EntityQuery;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Fuxing Loh
 * @since 2019-10-09 at 12:26 am
 */
@Singleton
public final class ArticleQuery extends Query {

    /**
     * @param cursor          for querying
     * @param profileSupplier to query from profile
     * @return TransportList with cursor information
     */
    public TransportList query(TransportCursor cursor, Function<EntityManager, Profile> profileSupplier) {
        ArticleStatus status = cursor.getEnum("status", ArticleStatus.class);
        return query(cursor, status, profileSupplier);
    }

    /**
     * @param cursor          for querying
     * @param status          explicit status to restrict querying to that status
     * @param profileSupplier to query from profile
     * @return TransportList with cursor information
     */
    public TransportList query(TransportCursor cursor, ArticleStatus status, Function<EntityManager, Profile> profileSupplier) {
        return provider.reduce(true, entityManager -> {
            Profile profile = profileSupplier.apply(entityManager);
            if (profile == null) throw new ForbiddenException();

            return query(entityManager, cursor, status, query -> {
                query.where("profile", profile);
            }).asTransportList((article, builder) -> {
                builder.putAll(cursor);
                builder.put("id", article.getId());
                builder.put("updatedAt", article.getUpdatedAt().getTime());
            });
        });
    }

    public static EntityQuery<Article>.EntityStream query(EntityManager entityManager, TransportCursor cursor, ArticleStatus status, Consumer<EntityQuery<Article>> consumer) {
        return EntityQuery.select(entityManager, "FROM Article", Article.class)
                .consume(consumer)
                .where("status = :status", "status", status)
                .predicate(cursor.has("updatedAt", "id"), query -> {
                    query.where("(updatedAt < :updatedAt OR (updatedAt = :updatedAt AND id < :id))",
                            "updatedAt", cursor.getDate("updatedAt"), "id", cursor.get("id")
                    );
                })
                .orderBy("updatedAt DESC, id DESC")
                .size(cursor.size(10, 33))
                .asStream()
                .peek(article -> {
                    article.setContent(null);
                });
    }
}
