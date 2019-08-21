package app.munch.api;

import app.munch.model.ManagedPage;
import dev.fuxing.jpa.TransactionProvider;
import dev.fuxing.transport.service.TransportContext;

import javax.inject.Inject;

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
        // TODO(fuxing): Path is not url safe
        PATH("/admin/managed/pages", () -> {
            PATH("/:path", () -> {
                POST("", this::post);

                PATH("/latest", () -> {
                    GET("", this::getLatest);
                });
            });
        });
    }

    public ManagedPage post(TransportContext ctx) {
        String path = ctx.pathString("path");

        ManagedPage page = ctx.bodyAsObject(ManagedPage.class);
        page.setPath(path);

        return provider.reduce(true, entityManager -> {
            entityManager.persist(page);
            return page;
        });
    }

    public ManagedPage getLatest(TransportContext ctx) {
        String path = ctx.pathString("path");
        boolean published = ctx.queryBool("published");

        return provider.reduce(true, entityManager -> {
            return entityManager.createQuery("FROM ManagedPage " +
                    "WHERE path = :path AND published = :published " +
                    "ORDER BY createdAt DESC", ManagedPage.class)
                    .setParameter("path", path)
                    .setParameter("published", published)
                    .setMaxResults(1)
                    .getSingleResult();
        });
    }
}
