package app.munch.api;

import app.munch.model.Article;
import app.munch.model.Publication;
import dev.fuxing.jpa.EntityStream;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.TransportCursor;
import dev.fuxing.transport.TransportList;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;

/**
 * Created by: Fuxing
 * Date: 2019-08-12
 * Time: 05:45
 */
public final class PublicationService extends DataService {

    @Inject
    PublicationService(TransactionProvider provider) {
        super(provider);
    }

    @Override
    public void route() {
        PATH("/publications/:id", () -> {
            GET("", this::get);

            PATH("/articles", () -> {
                GET("", this::articles);
            });
        });
    }

    public Publication get(TransportContext ctx) {
        String id = ctx.pathString("id");

        return provider.reduce(true, entityManager -> {
            return entityManager.find(Publication.class, id);
        });
    }

    public TransportList articles(TransportContext ctx) {
        String id = ctx.pathString("id");
        int size = ctx.querySize(20, 20);

        TransportCursor cursor = ctx.queryCursor();
        String cursorId = cursor.get("id");
        Long position = cursor.getLong("position");

        return provider.reduce(true, entityManager -> {
            return EntityStream.of(() -> {
                if (position != null && cursorId != null) {
                    return entityManager.createQuery("SELECT pa.article,pa.position,pa.id FROM PublicationArticle pa " +
                            "WHERE pa.publication.id = :id " +
                            "AND (pa.position < :position OR (pa.position = :position AND pa.id < :cursorId)) " +
                            "ORDER BY pa.position DESC, pa.id DESC ", Object[].class)
                            .setParameter("id", id)
                            .setParameter("position", position)
                            .setParameter("cursorId", cursorId)
                            .setMaxResults(size)
                            .getResultList();
                }

                return entityManager.createQuery("SELECT pa.article,pa.position,pa.id FROM PublicationArticle pa " +
                        "WHERE pa.publication.id = :id " +
                        "ORDER BY pa.position DESC, pa.id DESC ", Object[].class)
                        .setParameter("id", id)
                        .setMaxResults(size)
                        .getResultList();
            }).cursor(size, (o, builder) -> {
                builder.put("position", o[1]);
                builder.put("id", o[2]);
            }).map(o -> {
                Article article = (Article) o[0];
                article.setContent(null);
                return article;
            }).asTransportList();
        });
    }
}
